(ns clj-webapp.core-test
  (:require [clojure.test :refer :all]
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

  (testing "snippet route"
    (let [response (app (mock/request :put "/snippet/0" "Twas brilling, and the slithy toves"))]
      (is (= (:status response) 200))
      (is (= (:body response) "OK"))
      )
    )

  )


