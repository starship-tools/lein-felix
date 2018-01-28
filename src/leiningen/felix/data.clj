(ns leiningen.felix.data)

(defn install-dir
  [proj]
  (get-in proj [:felix :install-dir]))

(defn working-dir
  [proj]
  (format (get-in proj [:felix :working-dir-tmpl])
          (get-in proj [:felix :install-dir])
          (get-in proj [:felix :version])))

(defn dist-filename
  [proj]
  (format (get-in proj [:felix :download :filename-tmpl])
          (get-in proj [:felix :download :dist-name])
          (get-in proj [:felix :version])))

(defn download-url
  [proj]
  (format (get-in proj [:felix :download :url-tmpl])
          (get-in proj [:felix :download :host])
          (dist-filename proj)))

(defn zip-filename
  [proj]
  (format "%s/%s"
          (install-dir proj)
          (dist-filename proj)))

(defn felix-jar
  [proj]
  (get-in proj [:felix :jar]))

(defn felix-script-dir
  [proj]
  (get-in proj [:felix :script :install-dir]))

(defn felix-script
  [proj]
  (format "%s/%s"
          (get-in proj [:felix :script :install-dir])
          (get-in proj [:felix :script :name])))

(defn bundle-dir
  [proj]
  (format "%s/%s"
          (working-dir proj)
          (get-in proj [:felix :bundle-dir])))
