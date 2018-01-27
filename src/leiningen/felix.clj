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
       }
     :version "5.6.10"
     :install-dir "felix"
     :working-dir-tmpl "%s/felix-framework-%s" ; install-dir, version
     :jar "bin/felix.jar"}})

(defn working-dir
  [proj]
  (format (get-in proj [:felix :working-dir-tmpl])
          (get-in proj [:felix :install-dir])
          (get-in proj [:felix :version])))

(defn felix
  ""
  [proj-orig & [cmd & args]]
  (let [proj-with-profs (lein/project-with-profiles proj-orig)
        proj (deep-merge defaults
                         proj-with-profs
                         (get-in proj-with-profs [:profiles]))]
    (case (keyword cmd)
      :download (commands/download args)
      :unpack (commands/unpack args)
      :setup (commands/setup args)
      :shell (commands/shell args))))
