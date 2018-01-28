(ns leiningen.felix.util)

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
