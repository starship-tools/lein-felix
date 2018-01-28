(ns leiningen.felix.commands
  (:require
    [clojure.java.shell :as shell]
    [leiningen.core.eval :as eval]
    [leiningen.felix.data :as data]
    [leiningen.felix.util :as util]))

(defn- -download
  [proj args]
  (println "Downloading Felix ...")
  (shell/sh "curl" "-sO" (data/download-url proj)))

(defn -move
  [proj args]
  (shell/sh "mkdir" "-p" (data/install-dir proj))
  (shell/sh "mv" (data/dist-filename proj) (data/install-dir proj)))

(defn download
  "Usage: lein felix download

  Download the Apache Felix distribution of the configured version from the
  configured mirror.

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

  This command uses the following configuration options:

  * :felix :download :dist-name
  * :felix :version
  * :felix :install-dir"
  [proj args]
  (case (util/subcommand args)
    :help (util/help #'unpack)
    (do
      (println "Unpacking Felix ...")
      (shell/sh "unzip"
                "-qq" (data/zip-filename proj)
                "-d" (data/install-dir proj)))))

(defn script-install
  [proj args]
  )

(defn script-uninstall
  [proj args]
  )

(defn script
  [proj args]
  )

(defn install
  "Usage: lein felix install

  Perform the 'download', 'unpack, and optionally, 'script install' tasks
  by wrapping those commands.

  For configuration options, see the help for the wrapped commands."
  [proj args]
  (case (util/subcommand args)
    :help (util/help #'install)
    (do
      (download proj args)
      (unpack proj args)
      (println "Felix setup completed.")
      (println "You can now start the Felix shell with 'lein felix shell'."))))

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
  [proj args]
  (let [dir (data/install-dir proj)]
    (println (format "Recursively removing the directory `%s' ..." dir))
    (shell/sh "rm" "-rf" (data/install-dir proj))))
