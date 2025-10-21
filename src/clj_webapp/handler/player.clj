(ns clj-webapp.handler.player)

(def players "players 的原子类型" (atom #{"Alice" "Bob" "Charlie"}))

(defn list-players
  "查询 players"
  []
  {:players @players}
  )

(defn create-player
  "创建 player"
  [player-name]
  (swap! players conj player-name)
  {:msg "player created" :player-name player-name}
  )

(defn delete-player
  "删除 player"
  [player-name]
  (swap! players disj player-name)  ;; ✅ disj 可以直接从 set 中删除元素
  {:msg "player deleted" :player-name player-name}
  )

(defn update-player
  "更新 player old-name -> new-name"
  [old-name new-name]
  (swap! players #(-> % (disj old-name) (conj new-name)))
  {:msg "player updated" :old-name old-name :new-name new-name}
  )
