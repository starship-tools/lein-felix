(ns leiningen.felix.command.cljosgi
  (:require
    [leiningen.core.classpath :as classpath]
    [clojure.java.io :as io]
    [clojure.string :as string]
    [leiningen.felix.data :as data]
    [leiningen.felix.util :as util]))

(def clojure-osgi-id 'com.theoryinpractise/clojure.osgi)

(defn- get-clojure-osgi-jarname
  [proj]
  (format "%s-%s.jar"
          (second (string/split (str clojure-osgi-id) #"/"))
          (util/get-dep-version proj clojure-osgi-id)))

(defn- get-clojure-osgi-jar
  [proj]
  (->> (classpath/get-classpath proj)
       (map #(when (string/ends-with? % (get-clojure-osgi-jarname proj)) %))
       (remove nil?)
       first))

(defn install
  "Usage: lein felix clojure-osgi install JAR|[OPTIONS|SUBCOMMANDS]

  Install the Clojure OSGi bundle into Felix. Note that this command ultimately
  depends upon the project which has included `lein-felix` as a plugin
  dependency: if that project does not also include the `clojure.osgi`
  dependency, this install step will fail.

  Allowed options:
    -v - Display verbose output of install operation

  Allowed subcommands:
    help - Display this help message.

  This command uses the following configuration options:

  * :felix :install-dir
  * :felix :bundle-dir"
  [proj [subcommand & args]]
  (case (util/subcommand subcommand)
    :help (util/help #'install)
    (let [version (get-clojure-osgi-version proj)
          jar (get-clojure-osgi-jar proj)
          option [(first args)]]
      (util/sh (util/get-output-flag option)
                 "cp" "-v" jar (data/bundle-dir proj)))))

(defn uninstall
  "Usage: lein felix clojure-osgi uninstall JAR|[OPTIONS|SUBCOMMANDS]

  Uninstall the Clojure OSGi bundle from the Felix bundle directory.

  Allowed options:
    -v - Display verbose output of uninstall operation

  Allowed subcommands:
    help - Display this help message.

  This command uses the following configuration options:

  * :felix :install-dir
  * :felix :bundle-dir"
  [proj [subcommand & args]]
  (case (util/subcommand subcommand)
    :help (util/help #'install)
    (let [jar (get-clojure-osgi-jarname proj)
          option [(first args)]]
      (util/sh (util/get-output-flag option)
               "rm" "-v" (format "%s/%s"
                                 (data/bundle-dir proj)
                                 jar)))))

(defn run
  "Usage: lein felix clojusc-orgi [SUBCOMMAND]

  Pergform various operations related to Clojure OSGi support.

  Allowed subcommands:
    help      - Display this help message.
    install   - Install the clojure.osgi bundle.
    uninstall - Uninstall the clojure.osgi bundle."
  [proj args]
  (case (util/subcommand args)
    :install (install proj args)
    :uninstall (uninstall proj args)
    (util/help #'run)))
