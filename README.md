# lumberjack

A Clojure library designed to help with your log lines.  Currently creates a log entry structure for Nginx logs, and allows for some basic visualization of time series data.

## Usage

To view a simple time series graph of the number of hits by the minute:

(view-time-series (nginx-logs ["test/lumberjack/nginx_sample.log"]) :by timestamp-minute :grouping-name "minute")

## License

Copyright © 2013 Steven Proctor

Distributed under the Eclipse Public License, the same as Clojure.
