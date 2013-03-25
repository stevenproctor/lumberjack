(ns lumberjack.core-test
  (:require [clj-time.core :as time]
            [clj-time.coerce :as coerce])
  (:use midje.sweet
        lumberjack.core))

(def timestamp "21/Apr/2013:15:20:10 -0500")
(def timestamp-same-hour "21/Apr/2013:15:59:59 -0500")
(def timestamp-as-datetime (time/date-time 2013 04 21 20 20 10))

(fact "Can parse a date string into a DateTime"
  (parse-datetime timestamp) => timestamp-as-datetime)

(fact "Can get a log entry with time in miliseconds"
  (with-timestamp-in-millis {:timestamp timestamp}) => {:timestamp timestamp
                                                        :timestamp-in-millis (coerce/to-long timestamp-as-datetime)})

(facts "Can get the timestamp to a resolution"
  (fact "Can get the timestamp to the hour"
    (timestamp-hour {:timestamp timestamp}) => (coerce/to-long (time/date-time 2013 04 21 20))))

(fact "Can get a count of log entries to a time resolution"
  (fact "Can get the count of a single log entry to the hour"
    (count-entries [{:timestamp timestamp}] :by timestamp-hour) => {(coerce/to-long (time/date-time 2013 04 21 20)) 1})
  (fact "Can get the count of 2 log entries to the hour"
    (count-entries [{:timestamp timestamp}
                    {:timestamp timestamp-same-hour}] :by timestamp-hour) => {(coerce/to-long (time/date-time 2013 04 21 20)) 2}))
