(ns clj-webapp.session
  (:require [schejulure.core     :refer [schedule]]
            [flatland.useful.map :refer [remove-vals]])
  )

(def sessions "会话 返回(atom {}) " (atom {}))

(def last-session-id "返回最新的会话id (atom integer)"
  (atom 0)
  )

(defn next-session-id "更新会话id 返回下一个会话id (atom integer)"
  []
  (swap! last-session-id inc)
  )

(defn now "返回当前时间戳毫秒数" []
  (System/currentTimeMillis)
  )

(defn new-session "创建新的会话 返回一个 (atom {}) 对象" [initial]
  (let [session-id (next-session-id)
        session (assoc initial :last-referenced (atom (now)))]  ;;初始化 session
    (swap! sessions assoc session-id session)
    session-id
    )
  )

(defn get-session "获取会话 返回 (atom integer) 对象" [id]
  (let [session (@sessions id)]
    (reset! (:last-referenced session) (now))
    session
    )
  )

(defn session-expiry-time
  "会话过期时间"
  []
  (- (now) (* 10 60 1000))                                  ;; 10分钟
  )

(defn expired?
  "是否过期 返回bool值"
  [session]
  (< @(:last-referenced session) (session-expiry-time))
  )

(defn sweep-sessions
  "删除过期的session"
  []
  (swap! sessions #(remove-vals % expired?)))

(def session-sweeper "定时清除过期的session"
  (schedule {:min (range 0 60 5)} sweep-sessions )
  )
