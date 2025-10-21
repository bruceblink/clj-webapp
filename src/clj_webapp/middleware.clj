(ns clj-webapp.middleware
  (:require [ring.middleware.defaults :refer [api-defaults wrap-defaults]]
            [ring.util.response :refer [charset]] ))

(defn wrap-charset
  "转换字符编码的中间件"
  [handler]
  (fn [req] (charset (handler req) "UTF-8"))
  )

;; 一个帮助函数，可以根据环境选择不同默认中间件
(defn wrap-base [handler]
  ;; 生产环境可以换成 site-defaults
  (-> handler
      wrap-charset
      (wrap-defaults api-defaults)
    )
  )
