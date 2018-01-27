(ns leiningen.felix.commands
  (:require
    [clojure.java.shell :as shell]))

(defn download
  [args]
  (println "Downloading Felix ..."))

(defn unpack
  [args]
  (println "Unpacking Felix ..."))

(defn setup
  [args]
  (download args)
  (unpack args))

(defn shell
  [args]
  (println "Starting Felix shell ..."))
