(ns clj-webapp.routes
  (:require [clojure.edn :as edn]
            [compojure.core :refer :all]
            [compojure.route :as route])
  )

;; 定义 snippets
(def snippets (repeatedly promise))

(defn accept-snippet [n text]
  (deliver (nth snippets n) text)
  )

;; 后台异步打印提交的 snippet
(future
  (doseq [snippet (map deref snippets)]
    (println snippet))
  )

(defroutes app-routes
           (GET "/" [] "Hello World")

           (GET "/hello/:name" [name]
             (str "<h1>Hello, " name "!</h1>"))

           (PUT "/snippet/:n" [n :as {:keys [body]}]
             (accept-snippet (edn/read-string n) (slurp body))
             "OK")

           (POST "/api/echo" {body :body}
             (str "You posted: " (slurp body)))

           (route/not-found "Not Found"))
