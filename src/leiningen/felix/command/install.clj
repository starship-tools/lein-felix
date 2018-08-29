(ns leiningen.felix.command.install
  (:require
    [leiningen.felix.data :as data]
    [leiningen.felix.command.cljosgi :as cljosgi]
    [leiningen.felix.command.download :as download]
    [leiningen.felix.command.script :as script]
    [leiningen.felix.command.unpack :as unpack]
    [leiningen.felix.util :as util]))

(defn run
  "Usage: lein felix install [OPTIONS|SUBCOMMANDS]

  Perform the 'download', 'unpack, 'script install' , and
  'clojure-osgi install' tasks by wrapping those commands.

  Allowed options:
    -v - Display verbose output of install operation

  Allowed subcommands:
    help - Display this help message.

  For configuration options, see the help for the wrapped commands."
  [proj args]
  (case (util/subcommand args)
    :help (util/help #'run)
    (do
      (download/run proj args)
      (unpack/run proj args)
      (cljosgi/run proj (concat ["install"] args))
      (script/run proj (concat ["install"] args))
      (println "Felix setup completed.")
      (println (format "You can now start the Felix shell with '%s'."
                       (data/felix-script proj))))))
