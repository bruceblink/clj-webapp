(ns clj-webapp.session
  (:require [clj-webapp.sentences :refer [strings->sentences]]
            [clj-webapp.translate :refer [translate]]))

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

(defn create-session
  "创建会话"
  []
  (let [snippets (repeatedly promise)
        translations (delay (map translate
                                 (strings->sentences (map deref snippets))))
        ]
    (new-session {:snippets snippets :transients translations})
    )
  )
