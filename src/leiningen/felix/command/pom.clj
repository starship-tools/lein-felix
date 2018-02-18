(ns leiningen.felix.command.pom
  (:require
    [clojure.data.xml :as xml]
    [clojure.java.io :as io]
    [clojure.zip :as zip]
    [leiningen.core.main :as main]
    [leiningen.core.project :as project]
    [leiningen.felix.util :as util]
    [leiningen.pom :as pom]))

(defn get-merged-project
  [proj]
  (let [profile-kws (project/non-leaky-profiles proj)]
    (-> proj
        (project/unmerge-profiles profile-kws)
        (vary-meta assoc :leiningen.pom/original-project proj))))

(defn project->sexp
  [proj]
  (let [relativize #'pom/relativize
        xml-tags #'pom/xml-tags]
    (xml-tags :project (relativize proj))))

(defn update-xml
  [zipper matcher editor]
  (loop [loc zipper]
    (if (zip/end? loc)
      (zip/root loc)
      (if-let [matcher-result (matcher loc)]
        (let [new-loc (zip/edit loc editor)]
          (if (not (= (zip/node new-loc) (zip/node loc)))
            (recur (zip/next new-loc))))
        (recur (zip/next loc))))))

(defn plugins-tag? [loc]
  (let [tag (:tag (zip/node loc))]
    (= :plugins tag)))

(defn conj-plugin [plugin-elements node]
  (let [id (-> node :attrs :id keyword)
        new-content (conj (:content node) plugin-elements)]
    (assoc-in node [:content] new-content)))

(defn get-plugin-elements
  [proj]
  (xml/sexp-as-element
   (get-in proj [:felix :maven])))

(defn add-maven-bundle-plugin
  [elems proj]
  (let [plugin-elements (get-plugin-elements proj)
        zipped (zip/xml-zip elems)]
    (update-xml zipped
                plugins-tag?
                (partial conj-plugin plugin-elements))))

(defn make-pom
  [proj]
  (-> proj
      get-merged-project
      project->sexp
      xml/sexp-as-element
      (add-maven-bundle-plugin proj)
      xml/indent-str
      str))

(defn create
  [proj args]
  (let [pom-location-or-properties (io/file (:pom-location proj) "pom.xml")
        pom (make-pom proj)
        pom-file (io/file (:root proj) pom-location-or-properties)
        option [(second args)]]
    (.mkdirs (.getParentFile pom-file))
    (with-open [pom-writer (io/writer pom-file)]
      (.write pom-writer pom))
    (when (= :verbose (util/get-output-flag option))
      (main/info "Wrote" (str pom-file) "with OSGi bundle configuration."))))

(defn delete
  [proj args]
  (case (util/subcommand args)
    :help (util/help #'delete)
    (let [pom-file "pom.xml"]
      (println (format "Removing '%s' ..." pom-file))
      (util/sh (util/get-output-flag (rest args)) "rm" "-v" pom-file))))

(defn run
  "Usage: lein felix pom [SUBCOMMAND]

  Generate a pom.xml file suitable for creating OSGi bundles via the
  maven-bundle-plugin plugin. Note that if no subcommand is given,
  'create' is assumed.

  Supported subcommands:

    help      - Display this help message.
    delete    - Delete the pom.xml file.
    create    - Create a bundle-ready version of the pom file."
  [proj args]
  (case (util/subcommand args)
    :delete (delete proj args)
    :help (util/help #'run)
    (create proj args)))
