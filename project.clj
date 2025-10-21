(defproject clj-webapp "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "https://github.com/bruceblink/clj-webapp"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [compojure "1.7.0"]
                 [ring/ring-defaults "0.4.0"]
                 [ring/ring-jetty-adapter "1.10.0"]
                 [cheshire "5.0.1"]
                 [clj-http "0.7.0"]
                 [schejulure "0.1.4"]
                 [org.flatland/useful "0.10.0"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler clj-webapp.core/app}
  :main ^:skip-aot clj-webapp.core
  :target-path "target/%s"
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})
