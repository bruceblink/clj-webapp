(ns clj-webapp.translate
  (:require [clj-http.client :as client]) )

(def translator "翻译的API URL" "http://localhost:3001/translate")

(defn translate
  "翻译文字 返回 翻译后的文字"
  [text]
  (future
    ((client/post translator {:body text} :body))
    )
  )

(defn get-translation
  "通过session获取translation"
  [session n]
  @(nth @(:translations session) n)
  )