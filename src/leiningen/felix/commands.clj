(ns leiningen.felix.commands
  (:require
    [leiningen.core.eval :as eval]
    [leiningen.felix.data :as data]
    [leiningen.felix.command.bundle :as bundle]
    [leiningen.felix.command.script :as script]
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

(defn download
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
    :help (util/help #'download)
    (do
      (-download proj args)
      (-move proj args))))

(defn unpack
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
    :help (util/help #'unpack)
    (do
      (println "Unpacking Felix ...")
      (util/sh (util/get-output-flag args)
               "unzip"
                (data/zip-filename proj)
                "-d" (data/install-dir proj)))))

(defn script
  "Usage: lein felix script [SUBCOMMAND]

  Manage the installation of a felix shell script that wraps calls to the
  felix.jar distribution file

  Supported subcommands:

    install   - Create the 'felix' script in the configured directory.
    uninstall - Delete the 'felix' script from the configured directory.
    help      - Display this help message.

  Additional help is available for each subcommand via the 'help'
  subcommand, e.g.:

    $ lein felix script install help"
  [proj args]
  (case (util/subcommand args)
    :install (script/install proj args)
    :uninstall (script/uninstall proj args)
    (util/help #'script)))

(defn install
  "Usage: lein felix install [OPTIONS|SUBCOMMANDS]

  Perform the 'download', 'unpack, and optionally, 'script install' tasks
  by wrapping those commands.

  Allowed options:
    -v - Display verbose output of clean operations

  Allowed subcommands:
    help - Display this help message.

  For configuration options, see the help for the wrapped commands."
  [proj args]
  (case (util/subcommand args)
    :help (util/help #'install)
    (do
      (download proj args)
      (unpack proj args)
      (script proj (concat ["install"] args))
      (println "Felix setup completed.")
      (println (format "You can now start the Felix shell with '%s'."
                       (data/felix-script proj))))))

(defn- shell
  "SUPPORT REMOVED in 0.2.0.

  This is actually broken for running felix's shell, as readline doens't
  behave ... nor, in fact, do simple operations like copy-and-paste into the
  shell. Keeping this here for now, just in case we come up with a lein-plugin
  way to addres this.

  The workaround for this is addressed in the following ticket:
  * https://github.com/starship-tools/lein-felix/issues/4"
  [proj args]
  (println "Starting Felix shell ...")
  (println "To stop the Felix shell, type ^D\n")
  (binding [eval/*dir* (data/working-dir proj)]
    (eval/sh "java" "-jar" (data/felix-jar proj))))

(defn clean
  "Usage: lein felix clean

  Clean up emphemeral Felix files.

  Allowed options:
    -v - Display verbose output of unpack operation

  Allowed subcommands:
    help - Display this help message.

  This command uses the following configuration options:

  * :felix :install-dir"
  [proj args]
  (case (util/subcommand args)
    :help (util/help #'clean)
    (do
      (util/sh (util/get-output-flag args)
               "rm" "-rfv" (data/felix-cache proj)))))

(defn bundle
  "Usage: lein felix bundle [SUBCOMMAND]

  Perform various operations related to OSGi bundles.

  Allowed subcommands:
    create        - NOT YET IMPLEMENTED - Create an OSGi bundle for the project.
    install JAR   - Install the given OSGi bundle into Felix.
    uninstall JAR - NOT YET IMPLEMENTED - Uninstall the given OSGi bundle from the Felix bundle
                    directory.
    help          - Display this help message.

  This command uses the following configuration options:

  * :felix :install-dir"
  [proj args]
  (case (util/subcommand args)
    :create (bundle/create proj args)
    :install (bundle/install proj args)
    :uninstall (bundle/uninstall proj args)
    (util/help #'bundle)))

(defn uninstall
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
    :help (util/help #'uninstall)
    (let [dir (data/install-dir proj)]
      (println (format "Recursively removing the directory '%s' ..." dir))
      (util/sh (util/get-output-flag args) "rm" "-rfv" dir)
      (script proj (concat ["uninstall"] args)))))
