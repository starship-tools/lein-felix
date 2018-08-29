(ns leiningen.felix.data
  (:require
    [clojure.string :as string]
    [leiningen.felix.util :as util]))

(defn cache-dir
  [proj]
  (string/replace
    (get-in proj [:felix :cache-dir])
    #"^~"
    (System/getProperty "user.home")))

(defn install-dir
  [proj]
  (get-in proj [:felix :install-dir]))

(defn felix-framework-id
  [proj]
  (get-in proj [:felix :framework :id]))

(defn felix-framework-version
  [proj]
  (util/get-dep-version proj (felix-framework-id proj)))

(defn working-dir
  [proj]
  (format (get-in proj [:felix :working-dir-tmpl])
          (get-in proj [:felix :install-dir])
          (felix-framework-version proj)))

(defn dist-filename
  [proj]
  (format (get-in proj [:felix :download :filename-tmpl])
          (get-in proj [:felix :download :dist-name])
          (felix-framework-version proj)))

(defn cached-felix
  [proj]
  (format "%s/%s" (cache-dir proj) (dist-filename proj)))

(defn download-url
  [proj]
  (format (get-in proj [:felix :download :url-tmpl])
          (get-in proj [:felix :download :host])
          (dist-filename proj)))

(defn zip-filename
  [proj]
  (format "%s/%s"
          (install-dir proj)
          (dist-filename proj)))

(defn felix-jar
  [proj]
  (get-in proj [:felix :jar]))

(defn felix-script-dir
  [proj]
  (get-in proj [:felix :script :install-dir]))

(defn felix-script
  [proj]
  (format "%s/%s"
          (get-in proj [:felix :script :install-dir])
          (get-in proj [:felix :script :name])))

(defn bundle-dir
  [proj]
  (format "%s/%s"
          (working-dir proj)
          (get-in proj [:felix :bundle-dir])))

(defn felix-cache
  [proj]
  (format "%s/felix-cache" (working-dir proj)))

(defn clojure-osgi-id
  [proj]
  (get-in proj [:felix :clojure-osgi :id]))

(defn clojure-osgi-version
  [proj]
  (util/get-dep-version proj (clojure-osgi-id proj)))
