(ns leiningen.felix.command.bundle
  (:require
    [leiningen.felix.data :as data]
    [leiningen.felix.util :as util]))

(defn create
  "Usage: lein felix bundle create

  Create an OSGi bundle for the project.

  Note that this command requires that a bundle-ready pom.xml file be present
  in the project directory. To create one, use the following comamnd:

    $ lein felix pom"
  [proj args]
  (case (util/subcommand args)
    :help (util/help #'create)
    (let [jar (first args)
          option [(second args)]]
      (util/sh (util/get-output-flag option)
               "mvn" "-e" "org.apache.felix:maven-bundle-plugin:bundle"))))

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

(defn run
  "Usage: lein felix bundle [SUBCOMMAND]

  Perform various operations related to OSGi bundles.

  Allowed subcommands:
    create        - Create an OSGi bundle for the project.
    install JAR   - Install the given OSGi bundle into Felix.
    uninstall JAR - Uninstall the given OSGi bundle from the Felix bundle
                    directory.
    help          - Display this help message.

  This command uses the following configuration options:

  * :felix :install-dir"
  [proj args]
  (case (util/subcommand args)
    :create (create proj args)
    :install (install proj args)
    :uninstall (uninstall proj args)
    (util/help #'run)))
