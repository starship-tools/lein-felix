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
     :jar "bin/felix.jar"
     :script {
        :install-dir "bin"
        :name "felix"}}})

(defn felix
  "Usage: lein felix [COMMAND]

  Supported commands:

    download            - Download the supported version of the Apache Felix
                          distribution.
    unpack              - Unzip the compressed distribution file to a location
                          specified in the configuration (:felix :install-dir).
    script [SUBCOMMAND] - Perform various operations related to the wrapper
                          script for the felix.jar file; installs to the
                          location specified by configiuration
                          (:felix :script :install-dir).
    install             - Perform the 'download', 'unpack, and optionally,
                          'script install' tasks.
    uninstall           - Recursively remove the local Felix install dir.
    bundle [SUBCOMMAND] - Perform various operations related to OSGi bundles.
    help                - Display this help message.

  Additional help is available for each command via the 'help'
  subcommand, e.g.:

    $ lein felix download help"
  [proj-orig & [cmd & args]]
  (let [proj-with-profs (lein/project-with-profiles proj-orig)
        proj (util/deep-merge defaults
                              proj-with-profs
                              (get-in proj-with-profs [:profiles]))]
    (case (keyword cmd)
      :download (commands/download proj args)
      :unpack (commands/unpack proj args)
      :script (commands/script proj args)
      :install (commands/install proj args)
      :uninstall (commands/uninstall proj args)
      :bundle (commands/bundle proj args)
      (util/help #'felix))))

