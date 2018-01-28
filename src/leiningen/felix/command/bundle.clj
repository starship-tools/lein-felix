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
  "Usage: lein felix bundle install JAR

  Install the given OSGi bundle into Felix.

  This command uses the following configuration options:

  * :felix :install-dir"
  [proj args]
  (case (util/subcommand args)
    :help (util/help #'install)
    (do
      )))

(defn uninstall
  "Usage: lein felix bundle uninstall JAR

  Uninstall the given OSGi bundle from the Felix bundle directory.

  This command uses the following configuration options:

  * :felix :install-dir"
  [proj args]
  (case (util/subcommand args)
    :help (util/help #'uninstall)
    (do
      )))
