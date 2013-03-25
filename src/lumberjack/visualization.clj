(ns lumberjack.visualization
  (use incanter.core
       incanter.charts
       lumberjack.core))


(defn count-entries-dataset [records & {:keys [by grouping-name], :or {by identity}}]
  (col-names (to-dataset (map identity (count-entries records :by by))) [grouping-name "Hits"]))

(defn view-time-series [records & {:keys [by grouping-name]}]
  (let [x-label (keyword grouping-name)]
  (view (time-series-plot grouping-name
                          "Hits"
                          :x-label (str grouping-name)
                          :data (count-entries-dataset records :by by :grouping-name grouping-name)))))

