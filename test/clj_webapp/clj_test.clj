(ns clj-webapp.clj-test
  (:require [clojure.test :refer :all]))

(def my-atom (atom 42))

(deftest atom-def-test
  (testing "deref atom"
    (is (= 42 @my-atom))
    (is (= 42 (deref my-atom)))
    (is (= @my-atom (deref my-atom)))))

(deftest atom-update
  (testing "update atom1"
    (is (= 43 (swap! my-atom inc))))
  (testing "update atom2"
    (is (= 45 (swap! my-atom + 2)))))  ;; 注意这里是从 43 -> 45


(deftest atom-validator
  (testing "use validator"
    (let [non-negative (atom 0 :validator #(>= % 0))]
      ;; 成功情况
      (is (= 42 (reset! non-negative 42)))  ;; ✅ 正确断言写法
      ;; 失败情况
      (is (thrown? IllegalStateException    ;; thrown? 只能运行在clojure.test中
                   (reset! non-negative -1))) ;; ✅ 会抛出异常
      ;; 确认值未被修改
      (is (= 42 @non-negative))
      )
    )
  )