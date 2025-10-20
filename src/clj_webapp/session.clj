(ns clj-webapp.session)

(def sessions "会话 返回(atom {}) " (atom {}))

(def last-session-id
  "返回会话id (atom integer)"
  (atom 0)
  )

(defn next-session-id "更新会话id 返回下一个会话id (atom integer)"
  []
  (swap! last-session-id inc)
  )

(defn new-sessions "创建新的会话 返回一个 (atom {}) 对象" [initial]
  (let [session-id (next-session-id)]
    (swap! sessions assoc session-id initial)
    session-id
    )
  )

(defn get-session "获取会话 返回 (atom integer) 对象"
  [id]
  (@sessions id)
  )
