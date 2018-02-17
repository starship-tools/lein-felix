(ns leiningen.felix.command.clean
  (:require
    [leiningen.felix.data :as data]
    [leiningen.felix.util :as util]))

(defn run
  "Usage: lein felix clean

  Clean up emphemeral Felix files.

  Allowed options:
    -v - Display verbose output of clean operation

  Allowed subcommands:
    help - Display this help message.

  This command uses the following configuration options:

  * :felix :install-dir"
  [proj args]
  (case (util/subcommand args)
    :help (util/help #'run)
    (do
      (util/sh (util/get-output-flag args)
               "rm" "-rfv" (data/felix-cache proj)))))
