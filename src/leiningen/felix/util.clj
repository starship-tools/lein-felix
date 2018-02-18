(ns leiningen.felix.util
  (:require
    [clojure.java.io :as io]
    [clojure.java.shell :as shell]
    [clojure.string :as string]
    [leiningen.core.main :as lein]))

(defn deep-merge
  "Recursively merges maps. If values are not maps, the last value wins."
  [& values]
  (if (every? map? values)
    (apply merge-with deep-merge values)
    (last values)))

(defn help
  [func]
  (println (format "\n  %s\n" (:doc (meta func)))))

(defn subcommand
  [args]
  (keyword (first args)))

(defn- get-output
  [result out-type]
  (let [output (out-type result)]
    (if-not output
      output
      (let [output (string/trim output)]
        (if (= "" output)
          nil
          output)))))

(defn get-output-flag
  [args]
  (case (subcommand args)
    :-v :verbose
    :verbose :verbose
    :quiet))

(defn sh
  [mode & args]
  (let [result (apply shell/sh args)]
    (case mode
      :verbose (when-let [out (get-output result :out)] (println out))
      :quiet (when-let [out (get-output result :out)] (lein/debug out)))
    (when-let [err (get-output result :err)] (println err))))

(defn jarball
  [proj]
  (format "%s-%s.jar" (:name proj) (:version proj)))
