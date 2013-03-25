(defproject lumberjack "0.1.0"
  :description "Log file analysis for Clojure"
  :url "https://www.github.com/stevenproctor/lumberjack"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [incanter "1.4.1"]
                 [clj-time "0.4.5"]]
  :profiles {:dev {:dependencies [[midje "1.5.0"]]}})
