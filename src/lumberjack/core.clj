(ns lumberjack.core
  (require [clj-time.core :as ttime]
           [clj-time.coerce :as coerce]
           [clj-time.format :as tformat]))


(def ^:dynamic *timestamp-format* "dd/MMM/yyy:HH:mm:ss Z")

(defn parse-datetime [timestamp]
  (tformat/parse (tformat/formatter *timestamp-format*) timestamp))

(defn timestamp-in-millis [record]
  (coerce/to-long (parse-datetime (:timestamp record))))

(defn with-timestamp-in-millis [record]
  (assoc record :timestamp-in-millis (timestamp-in-millis record)))

(def timestamp-resolutions
   {:millis 1
    :second 1000
    :minute (* 1000 60)
    :15-minutes (* 1000 60 15)
    :hour (* 1000 60 60)})

(defn timestamp-to-resolution [record resolution]
  (let [r (resolution timestamp-resolutions)]
    (* r (quot (timestamp-in-millis record) r))))

(defn timestamp-millis [record]
  (timestamp-to-resolution record :millis))

(defn timestamp-second [record]
  (timestamp-to-resolution record :second))

(defn timestamp-minute [record]
  (timestamp-to-resolution record :minute))

(defn timestamp-15-minutes [record]
  (timestamp-to-resolution record :15-minutes))

(defn timestamp-hour [record]
  (timestamp-to-resolution record :hour))

(defn count-entries [records & {:keys [by], :or {by identity}}]
  (frequencies (map by records)))

