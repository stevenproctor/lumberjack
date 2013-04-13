(ns lumberjack.nginx
  (require [clojure.java.io :as io]
            [clojure.string :as string]))

(def parts [:original :ip :timestamp :request-method :request-uri :status-code :response-size :referrer])

(def log-line-pattern #"(\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3})? - - \[(.*)\] \"(\w+) ([^\"]*)\" (\d{3}) (\d+) \"([^\"]*)\".*")

(defn parse-line [line]
  (let [match (re-find log-line-pattern line)]
    (apply hash-map (interleave parts match))))

(defn parse-lines [lines]
  (map parse-line lines))

(defn- logfile-lines [filename]
  (string/split-lines (slurp filename)))

(defn process-logfile [filename]
    (map parse-line (logfile-lines filename)))

(defn nginx-logs [filenames]
  (mapcat process-logfile filenames))
