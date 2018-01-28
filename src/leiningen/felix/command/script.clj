(ns leiningen.felix.command.script
  (:require
    [clojure.java.io :as io]
    [leiningen.felix.data :as data]
    [leiningen.felix.util :as util]))

(defn- get-script-content
  [proj]
  (format (str "#!/bin/sh\n\n"
               "echo; echo \"Starting Felix shell ...\"\n"
               "echo \"To exit the Felix shell, type ^D\"\n"
               "echo; cd %s && java -jar %s \"$@\"\n")
          (data/working-dir proj)
          (data/felix-jar proj)))

(defn- create-script
  [proj args]
  (let [script (data/felix-script proj)]
    (util/sh (util/get-output-flag args)
             "mkdir" "-p"  (data/felix-script-dir proj))
    (spit script (get-script-content proj))
    (util/sh (util/get-output-flag args) "chmod" "755" script)
    (println (format "Created script '%s'." script))))

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
    (let [script (data/felix-script proj)
          file (io/file script)]
      (if (.exists file)
        (println (format "Skipping install of '%s'; file exists." script))
        (create-script proj (rest args))))))

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
      (util/sh (util/get-output-flag (rest args)) "rm" "-v" script))))
