(ns leiningen.felix.commands
  (:require
    [clojure.java.shell :as shell]
    [leiningen.core.eval :as eval]))

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

(defn- -download
  [proj args]
  (println "Downloading Felix ...")
  (shell/sh "curl" "-sO" (download-url proj)))

(defn move
  [proj args]
  (shell/sh "mkdir" "-p" (install-dir proj))
  (shell/sh "mv" (dist-filename proj) (install-dir proj)))

(defn download
  [proj args]
  (-download proj args)
  (move proj args))

(defn unpack
  [proj args]
  (println "Unpacking Felix ...")
  (shell/sh "unzip" "-qq" (zip-filename proj) "-d" (install-dir proj)))

(defn setup
  [proj args]
  (download proj args)
  (unpack proj args)
  (println "Felix setup completed.")
  (println "You can now start the Felix shell with 'lein felix shell'."))

(defn- shell
  "This is actually broken for running felix's shell, as readline doens't
  behave ... nor, in fact, do simple operations like copy-and-paste into the
  shell. Keeping this here for now, just in case we come up with a lein-plugin
  way to addres this.

  The workaround for this is addressed in the following ticket:
  * https://github.com/starship-tools/lein-felix/issues/4"
  [proj args]
  (println "Starting Felix shell ...")
  (println "To stop the Felix shell, type ^D\n")
  (binding [eval/*dir* (working-dir proj)]
    (eval/sh "java" "-jar" (felix-jar proj))))

(defn clean
  [proj args]
  (let [dir (install-dir proj)]
    (println (format "Recursively removing the directory `%s' ..." dir))
    (shell/sh "rm" "-rf" (install-dir proj))))
