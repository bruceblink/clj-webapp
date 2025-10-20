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

(deftest atom-watcher
  (testing "use watcher"
    (let [a (atom 0)
          out (with-out-str                      ;; 捕获 println 输出
                (add-watch a :print
                           #(println "Changed from" %3 "to" %4))
                (swap! a + 2))]                   ;; 触发 watcher

      (is (re-find #"Changed from 0 to 2" out))   ;; 验证输出字符串
      (is (= 2 @a))
      )
    )
  )

(deftest atom-watcher-with-redefs
  (testing "use watcher with redefs"
    (let [a (atom 0)
          called (atom nil)
          ]
      (with-redefs [println (fn [& args] (reset! called args))] ;; 使用 with-redefs 替换 println 输出
        (add-watch a :print
                   #(println "Changed from" %3 "to" %4))
        (swap! a + 2)
        )
      (is (= ["Changed from" 0 "to" 2] @called))   ;; 验证输出字符串
      (is (= 2 @a))
      )
    )
  )
