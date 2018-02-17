(ns leiningen.felix.command.cljosgi
  (:require
    [leiningen.core.classpath :as classpath]
    [clojure.java.io :as io]
    [clojure.string :as string]
    [leiningen.felix.data :as data]
    [leiningen.felix.util :as util]))

(def clojure-osgi-id 'com.theoryinpractise/clojure.osgi)

(defn- get-clojure-osgi-jarname
  [version]
  (format "%s-%s.jar"
          (second (string/split (str clojure-osgi-id) #"/"))
          version))

(defn- get-clojure-osgi-jar
  [proj version]
  (->> (classpath/get-classpath proj)
       (map #(when (string/ends-with? % (get-clojure-osgi-jarname version)) %))
       (remove nil?)
       first))

(defn- get-clojure-osgi-version
  [proj]
  (->> proj
       :dependencies
       (map #(when (= (first %) clojure-osgi-id) (second %)))
       (remove nil?)
       first))

(defn install
  "Usage: lein felix clojure-osgi install JAR|[OPTIONS|SUBCOMMANDS]

  Install the given OSGi bundle into Felix.

  Allowed options:
    -v - Display verbose output of install operation

  Allowed subcommands:
    help - Display this help message.

  This command uses the following configuration options:

  * :felix :install-dir
  * :felix :bundle-dir"
  [proj [subcommand & args]]
  (let [version (get-clojure-osgi-version proj)
        jar (get-clojure-osgi-jar proj version)
        option [(second args)]]
    (util/sh (util/get-output-flag option)
               "cp" "-v" jar (data/bundle-dir proj))))

(defn run
  "Usage: lein felix clojusc-orgi [SUBCOMMAND]

  Pergform various operations related to Clojure OSGi support.

  Allowed subcommands:
    help    - Display this help message.
    install - Install the clojure.osgi bundle."
  [proj args]
  (case (util/subcommand args)
    :install (install proj args)
    (util/help #'run)))
