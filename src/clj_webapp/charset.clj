(ns clj-webapp.charset
  (:require [ring.util.response :refer [charset]]))

(defn wrap-charset
  "转换字符"
  [handler]
  (fn [req] (charset (handler req) "UTF-8"))
  )
