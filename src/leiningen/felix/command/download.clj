(ns leiningen.felix.command.download
  (:require
    [clojure.java.io :as io]
    [leiningen.felix.data :as data]
    [leiningen.felix.util :as util]))

(def user-agent
  (str "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) "
       "AppleWebKit/534.30 (KHTML, like Gecko) "
       "Chrome/12.0.742.112 Safari/534.30"))

(defn- -download
  [proj args]
  (println "Downloading Felix ...")
  (util/sh (util/get-output-flag args)
           "curl"
           "-sO"
           "--ssl"
           "-A" user-agent
           (data/download-url proj)))

(defn -cache
  [proj args]
  (util/sh (util/get-output-flag args) "mkdir" "-pv" (data/cache-dir proj))
  (util/sh (util/get-output-flag args) "mv" "-v"
                                       (data/dist-filename proj)
                                       (data/cache-dir proj)))

(defn -download-to-cache
  [proj args]
  (when-not (.exists (io/as-file (data/cached-felix proj)))
    (-download proj args)
    (-cache proj args)))

(defn -copy
  [proj args]
  (util/sh (util/get-output-flag args) "mkdir" "-pv" (data/install-dir proj))
  (util/sh (util/get-output-flag args) "cp" "-v"
                                       (data/cached-felix proj)
                                       (data/zip-filename proj)))

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
  * :felix :cache-dir
  * :felix :install-dir"
  [proj args]
  (case (util/subcommand args)
    :help (util/help #'run)
    (do
      (-download-to-cache proj args)
      (-copy proj args))))
