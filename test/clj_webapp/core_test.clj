(ns clj-webapp.core-test
  (:require [cheshire.core :as json]
            [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [clj-webapp.core :refer :all]))

(deftest test-app
  (testing "main route"
    (let [response (app (mock/request :get "/"))]
      (is (= (:status response) 200))
      (is (= (:body response) "Hello World"))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404))))

  )

(deftest test-api-snippet-0
  (testing "snippet route"
    (let [req (-> (mock/request :put "/snippet/0")
                  (mock/body "Twas brilling, and the slithy toves"))
          response (app req)]
      (is (= 200 (:status response)))
      (is (= "OK" (:body response)))))
  ;;  Twas brilling, and the slithy toves
  )

(deftest test-api-snippet-2

  (testing "snippet route"
    (let [req (-> (mock/request :put "/snippet/2")
                  (mock/body "Twas brilling, and the slithy toves"))
          response (app req)]
      (is (= 200 (:status response)))
      (is (= "OK" (:body response)))))
  ;;  没有返回值
  )

(deftest player-api
  (testing "players route"
    (let [response (app (mock/request :get "/players"))]
      (is (= (:status response) 200))
      (is (= (:body response) "[]")))
    )

  (testing "PUT and GET /players"
    (let [_ (app (mock/request :put "/players/john"))
          response (app (mock/request :get "/players"))
          players (json/parse-string (:body response))]
      (is (= 200 (:status response)))
      (is (some #{"john"} players)))

    (let [_ (app (mock/request :put "/players/john"))
          _ (app (mock/request :put "/players/paul"))
          _ (app (mock/request :put "/players/george"))
          _ (app (mock/request :put "/players/ringo"))
          response (app (mock/request :get "/players"))
          players (json/parse-string (:body response))]
      (is (= 200 (:status response)))
      (is (some #{"john"} players))
      (is (some #{"paul"} players))
      (is (some #{"george"} players))
      (is (some #{"ringo"} players))

      )
    )
  )


