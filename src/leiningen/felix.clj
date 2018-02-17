(ns leiningen.felix
  (:require
    [leiningen.core.project :as lein]
    [leiningen.felix.command.bundle :as bundle]
    [leiningen.felix.command.clean :as clean]
    [leiningen.felix.command.download :as download]
    [leiningen.felix.command.install :as install]
    [leiningen.felix.command.script :as script]
    [leiningen.felix.command.uninstall :as uninstall]
    [leiningen.felix.command.unpack :as unpack]
    [leiningen.felix.util :as util]))

(def defaults
  {:felix {
     :download {
       :host "http://apache.claz.org"
       :dist-name "org.apache.felix.main.distribution"
       :filename-tmpl "%s-%s.zip" ; dist-name, version
       :url-tmpl "%s/felix/%s" ; host, dist-filename
       }
     :version "5.6.10"
     :install-dir "felix"
     :working-dir-tmpl "%s/felix-framework-%s" ; install-dir, version
     :bundle-dir "bundle"
     :jar "bin/felix.jar"
     :script {
        :install-dir "bin"
        :name "felix"}}})

(defn felix
  "Usage: lein felix [COMMAND]

  Supported commands:

    download            - Download the supported version of the Apache Felix
                          distribution.
    unpack              - Unzip the compressed distribution file to the install
                          directory.
    script [SUBCOMMAND] - Perform various operations related to the wrapper
                          script for the felix.jar file,
    install             - Perform the 'download', 'unpack', and
                          'script install' tasks.
    clean               - Clean up emphemeral Felix files.
    bundle [SUBCOMMAND] - Perform various operations related to OSGi bundles.
    uninstall           - Recursively remove the local Felix install dir.
    help                - Display this help message.

  Additional help is available for each command via the 'help'
  subcommand, e.g.:

    $ lein felix download help"
  [proj-orig & [cmd & args]]
  (let [proj-with-profs (lein/project-with-profiles proj-orig)
        proj (util/deep-merge defaults
                              proj-with-profs
                              (or (get-in proj-with-profs [:profiles]) {}))]
    (case (keyword cmd)
      :download (download/run proj args)
      :unpack (unpack/run proj args)
      :script (script/run proj args)
      :install (install/run proj args)
      :bundle (bundle/run proj args)
      :clean (clean/run proj args)
      :uninstall (uninstall/run proj args)
      (util/help #'felix))))

