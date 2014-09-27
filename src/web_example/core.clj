(ns web-example.core
  (:use compojure.core)
  (:require
   [ring.adapter.jetty :as jetty]
   [net.cgrand.enlive-html :as enlive]
   [clojure.java.io :refer [file]]
   [clojure.pprint :refer [pprint]]
   [compojure.route :as route]))

(enlive/deftemplate page-template (file "resources/public/page.html")
  [greeting]
  [:h1] (enlive/content greeting))

(defroutes main-routes
  (GET "/" [] (apply str (page-template "Hell world trapped there")))
  (route/resources "/")
  (route/not-found "404 not found"))

; Main for local running with lein run without lein ring server.
;; (defn -main []
;;   (do
;;     (jetty/run-jetty 
;;      #_ (fn [req]
;;        (do
;;          (println "Request received")
;;          {:status 200, :body "hell world"}))
;;      main-routes
;;      {:port 5001})))

;; Main for running on Heroku
(defn -main []
  (let [port (Integer/parseInt (get (System/getenv) "PORT" "5000"))]
    (jetty/run-jetty main-routes {:port port})))
