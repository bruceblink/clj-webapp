(ns clj-webapp.core
  (:require [ring.adapter.jetty :as jetty]
            [clj-webapp.routes :refer [app-routes]]
            [clj-webapp.middleware :refer [wrap-base]])
  )

;; 组装 app
(def app
  (wrap-base app-routes)
  )

;; 启动服务器
(defn -main [& _]
  (jetty/run-jetty app {:port 3001 :join? false})
  )
