(ns leiningen.felix.command.bundle
  (:require
    [leiningen.felix.command.pom :as pom]
    [leiningen.felix.data :as data]
    [leiningen.felix.util :as util]))

(defn create
  "Usage: lein felix bundle create

  Create an OSGi bundle for the project."
  [proj args]
  (case (util/subcommand args)
    :help (util/help #'create)
    (let [jar (first args)
          option [(second args)]]
      (pom/run proj args)
      (util/sh (util/get-output-flag option)
               "mvn" "-e" "org.apache.felix:maven-bundle-plugin:bundle"))))

(defn install
  "Usage: lein felix bundle install [OPTIONS|SUBCOMMANDS]

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
    (let [jar (format "%s/%s" (:target-path proj) (util/jarball proj))
          option args]
      (util/sh (util/get-output-flag option)
               "cp" "-v" jar (data/bundle-dir proj)))))

(defn uninstall
  "Usage: lein felix bundle uninstall [OPTIONS|SUBCOMMANDS]

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
    (let [option args]
      (util/sh (util/get-output-flag option)
               "rm" "-v" (format "%s/%s"
                                 (data/bundle-dir proj)
                                 (util/jarball proj))))))

(defn run
  "Usage: lein felix bundle [SUBCOMMAND]

  Perform various operations related to OSGi bundles.

  Allowed subcommands:
    create    - Create an OSGi bundle for the project.
    install   - Install the project JAR as an OSGi bundle into Felix.
    uninstall - Uninstall the project's OSGi bundle from the Felix bundle
                directory.
    help      - Display this help message.

  This command uses the following configuration options:

  * :felix :install-dir"
  [proj args]
  (case (util/subcommand args)
    :create (create proj args)
    :install (install proj args)
    :uninstall (uninstall proj args)
    (util/help #'run)))
