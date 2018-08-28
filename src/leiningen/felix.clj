(ns leiningen.felix
  (:require
    [leiningen.core.project :as lein]
    [leiningen.felix.command.bundle :as bundle]
    [leiningen.felix.command.clean :as clean]
    [leiningen.felix.command.cljosgi :as cljosgi]
    [leiningen.felix.command.download :as download]
    [leiningen.felix.command.install :as install]
    [leiningen.felix.command.pom :as pom]
    [leiningen.felix.command.script :as script]
    [leiningen.felix.command.uninstall :as uninstall]
    [leiningen.felix.command.unpack :as unpack]
    [leiningen.felix.util :as util]))

(def defaults
  {:felix {
     :framework {
       :id 'org.apache.felix/org.apache.felix.framework}
     :download {
       :host "http://apache.claz.org"
       :dist-name "org.apache.felix.main.distribution"
       :filename-tmpl "%s-%s.zip" ; dist-name, version
       :url-tmpl "%s/felix/%s" ; host, dist-filename
       }
     :version "6.0.1"
     :install-dir "felix"
     :working-dir-tmpl "%s/felix-framework-%s" ; install-dir, version
     :bundle-dir "bundle"
     :jar "bin/felix.jar"
     :script {
       :install-dir "bin"
       :name "felix"}
     :clojure-osgi {
       :id 'com.theoryinpractise/clojure.osgi}}})

(defn felix
  "Usage: lein felix [COMMAND]

  Supported commands:

    bundle [SUBCOMMAND]       - Perform various operations related to OSGi
                                bundles.
    clean                     - Clean up emphemeral Felix files.
    clojure-osgi [SUBCOMMAND] - Pergform various operations related to Clojure
                                OSGi support.
    download                  - Download the supported version of the Apache
                                Felix distribution.
    help                      - Display this help message.
    install                   - Perform the 'download', 'unpack',
                                'script install', and 'clojure-osgi install'
                                tasks.
    pom                       - Generate a maven-bundle-plugin ready 'pom.xml'
                                file.
    script [SUBCOMMAND]       - Perform various operations related to the
                                wrapper script for the felix.jar file,
    uninstall                 - Recursively remove the local Felix install dir.
    unpack                    - Unzip the compressed distribution file to the
                                install directory.

  Additional help is available for each command via the 'help'
  subcommand, e.g.:

    $ lein felix download help"
  [proj-orig & [cmd & args]]
  (let [proj-with-profs (lein/project-with-profiles proj-orig)
        proj (util/deep-merge defaults
                              proj-with-profs
                              (or (get-in proj-with-profs [:profiles]) {}))]
    (case (keyword cmd)
      :bundle (bundle/run proj args)
      :clean (clean/run proj args)
      :clojure-osgi (cljosgi/run proj args)
      :download (download/run proj args)
      :install (install/run proj args)
      :pom (pom/run proj args)
      :script (script/run proj args)
      :uninstall (uninstall/run proj args)
      :unpack (unpack/run proj args)
      (util/help #'felix))))
