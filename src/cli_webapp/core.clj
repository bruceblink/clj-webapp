(ns cli-webapp.core
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))
;; 定义路由
(defroutes app-routes
           (GET "/" [] "Hello World")
           (GET "/hello/:name" [name]
             (str "<h1>Hello, " name "!</h1>"))
           (POST "/api/echo" {body :body}
             (str "You posted: " (slurp body)))
           (route/not-found "Not Found"))

;; 将默认中间件包装上
(def app
  (wrap-defaults app-routes site-defaults))

;; 启动服务器
(defn -main [& args]
  (jetty/run-jetty app {:port 3001 :join? false}))
