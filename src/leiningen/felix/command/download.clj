(ns leiningen.felix.command.download
  (:require
    [leiningen.felix.data :as data]
    [leiningen.felix.util :as util]))

(defn- -download
  [proj args]
  (println "Downloading Felix ...")
  (util/sh (util/get-output-flag args) "curl" "-sO" (data/download-url proj)))

(defn -move
  [proj args]
  (util/sh (util/get-output-flag args) "mkdir" "-pv" (data/install-dir proj))
  (util/sh (util/get-output-flag args) "mv" "-v"
                                       (data/dist-filename proj)
                                       (data/install-dir proj)))

(defn run
  "Usage: lein felix download

  Download the Apache Felix distribution of the configured version from the
  configured mirror.

  Allowed options:
    -v - Display verbose output of download operation

  Allowed subcommands:
    help - Display this help message.

  This command uses the following configuration options:

  * :felix :download :host
  * :felix :download :dist-name
  * :felix :version
  * :felix :install-dir"
  [proj args]
  (case (util/subcommand args)
    :help (util/help #'run)
    (do
      (-download proj args)
      (-move proj args))))
