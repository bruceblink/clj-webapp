(ns clj-webapp.routes
  (:require [clj-webapp.handler.player :refer [create-player delete-player list-players update-player]]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :refer [response]]
            ))

(defroutes app-routes
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

           (route/not-found "Not Found"))
