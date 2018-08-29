(ns leiningen.felix.command.unpack
  (:require
    [leiningen.felix.data :as data]
    [leiningen.felix.util :as util]))

(defn run
  "Usage: lein felix unpack

  Unzip a downloaded, compressed Apache Felix distribution file.

  Allowed options:
    -v - Display verbose output of unpack operation

  Allowed subcommands:
    help - Display this help message.

  This command uses the following configuration options:

  * :felix :download :dist-name
  * :felix :version
  * :felix :install-dir"
  [proj args]
  (case (util/subcommand args)
    :help (util/help #'run)
    (do
      (println "Unpacking Felix ...")
      (util/sh (util/get-output-flag args)
               "unzip"
               "-o"
               (data/zip-filename proj)
               "-d" (data/install-dir proj)))))
