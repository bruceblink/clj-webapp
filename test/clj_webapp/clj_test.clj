(ns clj-webapp.clj-test
  (:require [clojure.test :refer :all]))

(def my-atom (atom 42))

(deftest atom-def-test
  (testing "deref atom"
    (is 42 @my-atom)
    (is 42 (deref my-atom))
    (is @my-atom (deref my-atom))
    )
  )

(deftest atom-update
  (testing "update atom1"
    (is 43 (swap! my-atom inc))
    )
  (testing "update atom2"
    (is 44 (swap! my-atom + 2))
    )
  )