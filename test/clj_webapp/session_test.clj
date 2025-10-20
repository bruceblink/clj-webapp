(ns clj-webapp.session-test
  (:require [clojure.test :refer [deftest is testing]]
            [clj-webapp.session :refer :all]))

(deftest test-new-and-get-session
  (testing "new session creation and get-session update"
    ;; 创建一个新的 session
    (let [session-id (new-session {})
          session (@sessions session-id)           ;; 从 sessions map 拿到 session atom
          old-time @(:last-referenced session)]   ;; 保存初始时间戳
      ;; 检查 session-id
      (is (= 1 session-id))
      (is (integer? session-id))

      ;; 检查 last-referenced 是时间戳
      (is (integer? old-time))
      (println "Initial last-referenced:" old-time)

      ;; 调用 get-session 更新 last-referenced
      (let [session2 (get-session session-id)
            new-time @(:last-referenced session2)]
        ;; last-referenced 应该比之前的时间更大或等于
        (is (>= new-time old-time))
        (println "Updated last-referenced:" new-time)
        )
      )
    )
  )
