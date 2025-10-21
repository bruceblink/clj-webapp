(defproject clj-webapp "0.1.0"
  :description "一个使用compojure开发的 Clojure web app "
  :url "https://github.com/bruceblink/clj-webapp"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [compojure "1.7.0"]
                 [ring/ring-defaults "0.4.0"]
                 [ring/ring-jetty-adapter "1.10.0"]
                 [ring/ring-json "0.5.1"]
                 [cheshire "6.1.0"]
                 [clj-http "3.13.1"]
                 [schejulure "0.1.4"]
                 [org.flatland/useful "0.10.0"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler clj-webapp.core/app}
  :main ^:skip-aot clj-webapp.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all :jvm-opts ["-Dclojure.compiler.direct-linking=true"]} }
  :dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                       [ring/ring-mock "0.3.2"]]})
