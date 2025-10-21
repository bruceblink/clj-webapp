(ns clj-webapp.middleware
  (:require
    [ring.middleware.defaults :refer [api-defaults wrap-defaults]]
    [ring.middleware.params :refer [wrap-params]]
    [ring.middleware.keyword-params :refer [wrap-keyword-params]]
    [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
    [ring.util.response :refer [charset]]))

(defn wrap-charset
  "转换字符编码的中间件"
  [handler]
  (fn [req]
    (charset (handler req) "UTF-8")))

(defn wrap-base [handler]
  (-> handler
      wrap-charset ;; 自定义字符编码中间件
      (wrap-defaults api-defaults) ;; 默认配置中间件
      wrap-params  ;; 解析 URL 参数
      wrap-keyword-params ;; 将参数名转换为关键字
      wrap-json-body ;; 自动解析 JSON 请求体
      wrap-json-response  ;; 自动返回 JSON 响应
      )
  )
