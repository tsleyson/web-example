(defproject web-example "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [ring "1.1.8"]
                 [compojure "1.1.5"]
                 [enlive "1.1.4"]]
  :main web-example.core
  :min-lein-version "2.0.0"
  :plugins [[lein-ring "0.8.11"]]
  :ring {:handler web-example.core/main-routes})
