(defn get-prompt
  [ns]
  (str "\u001B[35m[\u001B[34m"
       ns
       "\u001B[35m]\u001B[33m λ\u001B[m=> "))

(defproject lein-felix "0.3.0-SNAPSHOT"
  :description "A lein plugin for the Apache Felix OSGi project"
  :url "https://github.com/starship-tools/lein-felix"
  :license {
    :name "Apache License, Version 2.0"
    :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :repl-options {
    :prompt ~get-prompt})
