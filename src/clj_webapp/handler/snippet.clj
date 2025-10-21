(ns clj-webapp.handler.snippet)

;; 定义 snippets
(def snippets (repeatedly promise))

(defn accept-snippet [session n text]        ;; 新增支持session
  (deliver (nth (:snippets session) n) text)
  )

;; 后台异步打印提交的 snippet
(future
  (doseq [snippet (map deref snippets)]
    (println snippet))
  )
