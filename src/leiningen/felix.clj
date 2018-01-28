(ns leiningen.felix
  (:require
    [leiningen.core.project :as lein]
    [leiningen.felix.commands :as commands]))

(defn deep-merge
  "Recursively merges maps. If values are not maps, the last value wins."
  [& values]
  (if (every? map? values)
    (apply merge-with deep-merge values)
    (last values)))

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
     :jar "bin/felix.jar"}})

(defn felix
  ""
  [proj-orig & [cmd & args]]
  (let [proj-with-profs (lein/project-with-profiles proj-orig)
        proj (deep-merge defaults
                         proj-with-profs
                         (get-in proj-with-profs [:profiles]))]
    (case (keyword cmd)
      :download (commands/download proj args)
      :unpack (commands/unpack proj args)
      :setup (commands/setup proj args)
      :shell (commands/shell proj args)
      :clean (commands/clean proj args))))
