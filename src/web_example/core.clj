(ns web-example.core
  "Contains handlers and routes"
  (:use compojure.core)
  (:require
   [web-example.db :refer :all]
   [web-example.web :refer :all]
   [ring.adapter.jetty :as jetty]
   [clojure.pprint :refer [pprint]]
   [compojure.route :as route]))

;; Handlers for static pages. note wrap-params is implicitly included
;; by defroutes.

(def new-post-form
  (fn [req]
    (let [auth_name (get-in req {:params "auth_name"})
          post_text (get-in req {:params "post_text"})]
      (if (and auth_name post_text)
        (new-post auth_name post_text)
        (throw (IllegalArgumentException. "No name or text provided."))))))

(defroutes main-routes
  (GET "/" [] (render-template front-page (get-posts db)))
  #_(GET "")
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
