(ns leiningen.felix.script
  (:require
    [clojure.java.io :as io]
    [leiningen.felix.data :as data]
    [leiningen.felix.util :as util]))

(defn install
  "Usage: lein felix script install

  Create the 'felix' script in the configured directory.

  This command uses the following configuration options:

  * :felix :script :install-dir
  * :felix :script :name
  * :felix :install-dir
  * :felix :jar"
  [proj args]
  (case (util/subcommand args)
    :help (util/help #'install)
    (do
      )))

(defn uninstall
  "Usage: lein felix script uninstall

  Delete the 'felix' script from the configured directory.

  This command uses the following configuration options:

  * :felix :script :install-dir
  * :felix :script :name"
  [proj args]
  (case (util/subcommand args)
    :help (util/help #'uninstall)
    (let [script (data/felix-script proj)]
      (println (format "Removing '%s' ..." script))
      (util/sh :verbose "rm" "-v" script))))
