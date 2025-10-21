(ns clj-webapp.routes
  (:require [cheshire.core :as json]
            [clj-webapp.session :refer [create-session get-session]]
            [clj-webapp.translate :refer [get-translation]]
            [clojure.edn :as edn]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :refer [response status]]
            ))

;; 定义 snippets
(def snippets (repeatedly promise))

(defn accept-snippet [session n text]        ;; 新增支持session
  (deliver (nth (:snippets session) n) text)
  )

;; 后台异步打印提交的 snippet
(future
  (doseq [snippet (map deref snippets)]
    (println snippet))
  )
;; 定义 players 的原子类型
(def players (atom ()))

;; 查询players
(defn list-players []
  (response (json/encode @players))
  )

;; 创建 player
(defn creat-player [player-name]
  (swap! players conj player-name)
  (status (response "") 201)
  )

(defroutes app-routes
           (GET "/" [] "Hello World")

           (GET "/hello/:name" [name]
             (str "<h1>Hello, " name "!</h1>"))

           (POST "/api/echo" {body :body}
             (str "You posted: " (slurp body)))

           (GET "/players" [] (list-players))
           (PUT "/players/:player-name" [player-name] (creat-player player-name))

           (POST "/session/create" []
             (response (str (create-session)))
             )
           (context "/session/:session-id" [session-id]
             (let [session (get-session (edn/read-string session-id))]
               (routes
                 (PUT "/snippet/:n" [n :as {:keys [body]}]
                   (accept-snippet session (edn/read-string n) (slurp body))
                   (response "OK")
                   )

                 (GET "/translation/:n" [n]
                   (response (get-translation session (edn/read-string n)))
                   )
                 )
               )
             )

           (route/not-found "Not Found"))
