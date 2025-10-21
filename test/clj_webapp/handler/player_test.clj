(ns clj-webapp.handler.player-test
  (:require [cheshire.core :as json]
            [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [clj-webapp.core :refer :all]))


(deftest player-api
  (testing "GET /players"
    (let [response (app (mock/request :get "/players"))]
      (is (= (:status response) 200))
      (is (= (:body response) "{\"players\":[\"Alice\",\"Charlie\",\"Bob\"]}")))
    )

  (testing "POST /players"
    ;; 发送 POST 请求，body 是 JSON
    (let [post-response (app (-> (mock/request :post "/players")
                                 (mock/json-body {:player-name "John"}))) ;; mock请求体
          get-response  (app (mock/request :get "/players"))
          players-data  (json/parse-string (:body get-response) true)] ;; true => keyword keys
      (is (= 200 (:status post-response))) ;; POST 返回状态
      (is (= 200 (:status get-response)))  ;; GET 返回状态
      (is (contains? (set (:players players-data)) "John")))
    )

  (testing "PUT /players"
    (let [put-response (app (-> (mock/request :put "/players/Alice")
                     (mock/json-body {:new-name "Alicia"})))
          get-response (app (mock/request :get "/players"))
          players (json/parse-string (:body get-response) true)]
      (is (= 200 (:status put-response)))
      (is (= 200 (:status get-response)))
      (is (contains? (set (:players players)) "Alicia")))
    )

  (testing "DELETE /players"
    (let [_ (app (mock/request :delete "/players/Alice"))
          get-response (app (mock/request :get "/players"))
          players (json/parse-string (:body get-response) true)]
      (is (= 200 (:status get-response)))
      (not (contains? (set (:players players)) "Alice")))
    )
  )