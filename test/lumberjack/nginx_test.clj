(ns lumberjack.nginx-test
  (:use midje.sweet
        lumberjack.nginx))

(def logline "74.125.225.243 - - [18/Mar/2013:15:18:49 -0500] \"GET /index?key=val\" 302 197 \"http://www.amazon.com/gp/prime?ie=UTF8&*Version*=1&*entries*=0\" \"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)\" \"-\" \"got: uid=C8D925AED17547510D26082802B0C116\" \"set: -\" 1363637929.657")
(def logline2 "173.252.110.27 - - [18/Mar/2013:15:20:10 -0500] \"PUT /logon\" 404 1178 \"http://shop.github.com/products/octopint-set-of-2\" \"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)\" \"-\" \"got: uid=C3D0C0AD617547517407B52502BD72AD\" \"set: -\" 1363638010.685")
(def logline-no-ipaddress " - - [18/Mar/2013:15:20:59 -0500] \"POST /search?id=1234567890\" 200 2 \"http://www.google.com\" \"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)\" \"-\" \"got: uid=C3D0C0AD617547517407B52502BD72AD\" \"set: -\" 1363638010.685")

(def logline-seq [logline logline2 logline-no-ipaddress])

(facts "We can parse Nginx log lines into its components"
  (fact "We can get the original log line from the parsed log line"
    (:original (parse-line logline)) => logline
    (:original (parse-line logline2)) => logline2
    (:original (parse-line logline-no-ipaddress)) => logline-no-ipaddress)
  (fact "We can get the IP Address from a log line"
    (:ip (parse-line logline)) => "74.125.225.243"
    (:ip (parse-line logline2)) => "173.252.110.27"
    (:ip (parse-line logline-no-ipaddress)) => nil)
  (fact "We can get the Timestamp from a log line"
    (:timestamp (parse-line logline)) => "18/Mar/2013:15:18:49 -0500"
    (:timestamp (parse-line logline2)) => "18/Mar/2013:15:20:10 -0500"
    (:timestamp (parse-line logline-no-ipaddress)) => "18/Mar/2013:15:20:59 -0500")
  (fact "We can get the request method from a log line"
    (:request-method (parse-line logline)) => "GET"
    (:request-method (parse-line logline2)) => "PUT"
    (:request-method (parse-line logline-no-ipaddress)) => "POST")
  (fact "We can get the request uri from a log line"
    (:request-uri (parse-line logline)) => "/index?key=val"
    (:request-uri (parse-line logline2)) => "/logon"
    (:request-uri (parse-line logline-no-ipaddress)) => "/search?id=1234567890")
  (fact "We can get the status code from a log line"
    (:status-code (parse-line logline)) => "302"
    (:status-code (parse-line logline2)) => "404"
    (:status-code (parse-line logline-no-ipaddress)) => "200")
  (fact "We can get the repsonse size from a logline"
    (:response-size (parse-line logline)) => "197"
    (:response-size (parse-line logline2)) => "1178"
    (:response-size (parse-line logline-no-ipaddress)) => "2")
  (fact "We can get the referred from a logline"
    (:referrer (parse-line logline)) => "http://www.amazon.com/gp/prime?ie=UTF8&*Version*=1&*entries*=0"
    (:referrer (parse-line logline2)) => "http://shop.github.com/products/octopint-set-of-2"
    (:referrer (parse-line logline-no-ipaddress)) => "http://www.google.com"))

(fact "We can parse multiple lines"
  (count (parse-lines logline-seq)) => (count logline-seq))

(fact "We can parse loglines from file(s)"
  (fact "We can parse loglines from a single file"
    (count (process-logfile "test/lumberjack/nginx_sample.log")) => 4)
  (fact "We can parse loglines from multiple files"
    (count (nginx-log-entries-from ["test/lumberjack/nginx_sample.log" "test/lumberjack/nginx_sample.log"])) => 8))
