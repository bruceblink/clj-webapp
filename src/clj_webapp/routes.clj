(ns clj-webapp.routes
  (:require [clj-webapp.handler.player :refer [create-player delete-player list-players update-player]]
            [clj-webapp.handler.session :refer [create-session get-session]]
            [clj-webapp.handler.snippet :refer [accept-snippet]]
            [clj-webapp.handler.translate :refer [get-translation]]
            [clojure.edn :as edn]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :refer [response]]
            ))

(defroutes app-routes
           (GET "/" [] "Hello World")

           (GET "/hello/:name" [name]
             (str "<h1>Hello, " name "!</h1>"))

           (POST "/api/echo" {body :body}
             (str "You posted: " (slurp body)))
           ;; players相关的 API
           (GET "/players" [] (response (list-players)))

           (PUT "/players/:player-name" [player-name :as req]
             (let [{:keys [new-name]} (:body req)]  ;; 从 JSON body 里取新名字
               (response (update-player player-name new-name))
               )
             )

           (DELETE "/players/:player-name" [player-name]
             (response (delete-player player-name))
             )

           (POST "/players" {body :body}
             (let [{:keys [player-name]} body]
               (response (create-player player-name)))
             )

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
