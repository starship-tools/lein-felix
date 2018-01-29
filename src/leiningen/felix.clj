(ns leiningen.felix
  (:require
    [leiningen.core.project :as lein]
    [leiningen.felix.commands :as commands]
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
      :download (commands/download proj args)
      :unpack (commands/unpack proj args)
      :script (commands/script proj args)
      :install (commands/install proj args)
      :bundle (commands/bundle proj args)
      :clean (commands/clean proj args)
      :uninstall (commands/uninstall proj args)
      (util/help #'felix))))

