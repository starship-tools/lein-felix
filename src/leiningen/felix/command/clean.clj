(ns leiningen.felix.command.clean
  (:require
    [leiningen.felix.data :as data]
    [leiningen.felix.util :as util]))

(defn run
  "Usage: lein felix clean

  Completely remove the 'felix-cache' directory and files.

  Allowed options:
    -v - Display verbose output of clean operation

  Allowed subcommands:
    help - Display this help message.

  This command uses the following configuration options:

  * :felix :install-dir"
  [proj args]
  (case (util/subcommand args)
    :help (util/help #'run)
    (util/sh (util/get-output-flag args)
             "rm" "-rfv" (data/felix-cache proj))))
