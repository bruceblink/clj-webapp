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
      ;; 请求头需要添加 "Content-Type": "application/json" 参数
      (wrap-json-body {:keywords? true}) ;; 自动解析 JSON 请求体 ✅ 关键点：开启关键字转换
      wrap-keyword-params ;; 将参数名转换为关键字
      wrap-params  ;; 解析 URL 参数
      (wrap-defaults api-defaults) ;; 默认配置中间件
      wrap-json-response  ;; 自动返回 JSON 响应
      wrap-charset ;; 自定义字符编码中间件
      )
  )
