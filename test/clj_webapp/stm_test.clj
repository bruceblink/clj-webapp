(ns clj-webapp.stm-test
  (:require [clojure.test :refer :all]))

(defn transfer [from to amount]
  (dosync
    (alter from - amount)
    (alter to + amount)
    )
  )

(deftest ref-test
  (testing "test def ref"
    (let [my-ref (ref 0)]
      (is (= 0 @my-ref))
      )
    )

  (testing "test alter single ref" ;; 测试更新单个ref变量
    (let [my-ref (ref 0)]
      (is (= 0 @my-ref))       ;; 确认ref的初始基准值
      (dosync
        (ref-set my-ref 42)    ;; 设置ref
        )
      (is (= 42 @my-ref))      ;; 断言设置后的ref
      (dosync
        (alter my-ref inc)     ;; 更新ref
        )
      (is (= 43 @my-ref))      ;; 更新后的ref
      )
    )

  (testing "test alter multi ref"  ;; 测试更新多个ref
    (let [checking (ref 1000)
          savings (ref 2000)
          ]
      (transfer savings checking 100)    ;; savings向checking 转账 100
      (is (= 1100 @checking))
      (is (= 1900 @savings))
      )
    )
  )

(def attempts (atom 0))
(def transfers (agent 0))

(defn transfer1 [from to amount]
  (dosync
    (swap! attempts inc)      ;; 会产生副作用
    (send transfers inc)
    (alter from - amount)
    (alter to + amount)
    )
  )

(def checking (ref 10000))
(def savings (ref 20000))

(defn stress-thread [from to iterations amount]
  (Thread. #(dotimes [_ iterations] (transfer1 from to amount)))
  )

(deftest test-transaction
  (testing "test transaction"
    (println "Before: Checking =" @checking " Savings =" @savings)
    (let [t1 (stress-thread checking savings 100 100)
          t2 (stress-thread savings checking 200 100)]
      (.start t1)
      (.start t2)
      (.join t1)
      (.join t2)
      (await transfers)
      )
    (is (= 300 @transfers))
    (is (> @attempts @transfers))
    (println "Attempts: " @attempts )
    (println "Transfers:" @transfers )
    (println "After:  Checking =" @checking " Savings =" @savings)
    )
  )
