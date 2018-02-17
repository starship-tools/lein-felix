(ns leiningen.felix.command.uninstall
  (:require
    [leiningen.felix.data :as data]
    [leiningen.felix.command.script :as script]
    [leiningen.felix.util :as util]))

(defn run
  "Usage: lein felix uninstall [OPTIONS|SUBCOMMANDS]

  Allowed options:
    -v - Display verbose output of uninstall operation

  Allowed subcommands:
    help - Display this help message.

  Recursively remove the local Felix install dir.

  This command uses the following configuration options:

  * :felix :download :dist-name
  * :felix :version
  * :felix :install-dir"
  [proj args]
  (case (util/subcommand args)
    :help (util/help #'run)
    (let [dir (data/install-dir proj)]
      (println (format "Recursively removing the directory '%s' ..." dir))
      (util/sh (util/get-output-flag args) "rm" "-rfv" dir)
      (script/run proj (concat ["uninstall"] args)))))
