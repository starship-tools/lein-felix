(ns leiningen.felix.command.bundle
  (:require
    [leiningen.felix.data :as data]
    [leiningen.felix.util :as util]))

(defn create
  "Usage: lein felix bundle create

  Create an OSGi bundle for the project.

  This command uses the following configuration options:

  * :felix :install-dir"
  [proj args]
  (case (util/subcommand args)
    :help (util/help #'create)
    (do
      )))

(defn install
  "Usage: lein felix bundle install JAR|[OPTIONS|SUBCOMMANDS]

  Install the given OSGi bundle into Felix.

  Allowed options:
    -v - Display verbose output of install operation

  Allowed subcommands:
    help - Display this help message.

  This command uses the following configuration options:

  * :felix :install-dir
  * :felix :bundle-dir"
  [proj [subcommand & args]]
  (case (util/subcommand subcommand)
    :help (util/help #'install)
    (let [jar (first args)
          option [(second args)]]
      (util/sh (util/get-output-flag option)
               "cp" "-v" jar (data/bundle-dir proj)))))

(defn uninstall
  "Usage: lein felix bundle uninstall JAR|[OPTIONS|SUBCOMMANDS]

  Uninstall the given OSGi bundle from the Felix bundle directory.

  Allowed options:
    -v - Display verbose output of uninstall operation

  Allowed subcommands:
    help - Display this help message.

  This command uses the following configuration options:

  * :felix :install-dir
  * :felix :bundle-dir"
  [proj [subcommand & args]]
  (case (util/subcommand subcommand)
    :help (util/help #'install)
    (let [jar (first args)
          option [(second args)]]
      (util/sh (util/get-output-flag option)
               "rm" "-v" (format "%s/%s"
                                 (data/bundle-dir proj)
                                 jar)))))
