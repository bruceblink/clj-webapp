(ns clj-webapp.sentences
  (:require [clojure.string :refer [trim]]))

(defn sentence-split
  "拆分段落为句子"
  [text]
  (map trim (re-seq #"[^\.!\?:;]+[\.!\?:;]*" text))
  )

(defn is-sentence?
  "判断文字是否是句子"
  [text]
  (re-matches #"^.*[\.!\?:;]$" text)
  )

(defn sentence-join
  "拼接句子"
  [x y]
  (if (is-sentence? x)
    y (str x " " y)
    )
  )

(defn strings->sentences
  "将文字转换为句子的list"
  [strings]
  (filter is-sentence?
          (reductions sentence-join
                      (mapcat sentence-split strings))))