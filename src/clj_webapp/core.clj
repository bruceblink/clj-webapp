(ns clj-webapp.core
  (:gen-class)
  (:require [ring.adapter.jetty :as jetty]
            [clj-webapp.routes :refer [app-routes]]
            [clj-webapp.middleware :refer [wrap-base]])
  )

;; 组装 app
(def app
  (wrap-base app-routes)
  )

;; 启动服务器
(defn -main [& args]
  (let [port (Integer/parseInt (or (first args) "3001"))]
    (jetty/run-jetty app {:port port
                          :join? false      ;; 禁用join线程，防止无法优雅关机重启
                          :max-threads 100
                          :min-threads 8
                          :idle-timeout 60000} )))