(ns clj-webapp.middleware
  (:require [ring.middleware.defaults :refer [api-defaults wrap-defaults]])
  )

;; 一个帮助函数，可以根据环境选择不同默认中间件
(defn wrap-base [handler]
  ;; 生产环境可以换成 site-defaults
  (wrap-defaults handler api-defaults)
  )
