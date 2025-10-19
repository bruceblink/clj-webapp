(ns clj-webapp.core
  (:require [clojure.edn :as edn]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]))

;; 定义一个 snippets的promise
(def snippets (repeatedly promise))

;; 更新 snippet
(defn accept-snippet [n text]
  (deliver (nth snippets n) text)
  )

(future
  (doseq [snippet (map deref snippets)]
    (println snippet)
    )
  )

;; 定义路由
(defroutes app-routes
           (GET "/" [] "Hello World")
           (GET "/hello/:name" [name]
             (str "<h1>Hello, " name "!</h1>"))
           ;; 定义分片路由
           (PUT "/snippet/:n" [n :as {:keys [body]}]
             (accept-snippet (edn/read-string n) (slurp body))
             (str "OK")
             )

           (POST "/api/echo" {body :body}
             (str "You posted: " (slurp body)))
           (route/not-found "Not Found"))

;; 将默认中间件包装上
(def app
  ;; 使用 api-defaults 禁用 anti-forgery
  (wrap-defaults app-routes api-defaults))

;; 启动服务器
(defn -main [& args]
  (jetty/run-jetty app {:port 3001 :join? false}))
