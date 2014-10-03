(ns web-example.db
  (:require [clojure.java.jdbc :as jdbc]
            [clj-time [core :as tm] [coerce :as tc]])
  (:import [web_example PasswordHash]))

;; PasswordHash/createHash is the only important method.
;; Returns a string iters:salt:hash.
;; Do (clojure.string/split (PasswordHash/createHash "password") #":")
;; Stick it back together before passing to validatePassword.
;; Possibly modify ITERATION_INDEX in the class since I don't want to
;; store the number of iterations in the database.

(def db
  {:subprotocol "postgresql"
   :classname "org.postgresql.Driver"
   :subname "//127.0.0.1:5432/trisdan"})

(def db-con (jdbc/get-connection db))

(def prepared-queries
  {:get-all-posts (jdbc/prepare-statement
                   db-con
                   (str "SELECT auth_name, post_date, post_text "
                        "FROM blogs ORDER BY post_date DESC, post_time DESC;"))
   :get-n-posts (jdbc/prepare-statement
                 db-con
                 (str "SELECT auth_name, post_date, post_text " 
                      "FROM blogs ORDER BY post_date DESC, post_time DESC LIMIT ?"))})

(defn get-posts
  "Gets all posts without n, or n posts with it."
  ([db-spec]
     (jdbc/query db-spec [(:get-all-posts prepared-queries)]))
  ([db-spec n]
     (jdbc/query db-spec [(:get-n-posts prepared-queries) n])))

(def date-extractor (juxt tm/year tm/month tm/day))
(def time-extractor (juxt tm/hour tm/minute tm/sec))

(defn get-date
  "Gets a date string from clj-time."
  [sep extractor]
  (apply str (interpose sep (extractor (tm/now)))))

;; Note: this doesn't do dates. I'd rather have Postgres handle that,
;; but if I do need to do it from Clojure, something or other requires
;; clj-time (based on Joda Time) as a dependency, so we already have
;; that library in the classpath.
;; See https://github.com/seancorfield/clj-time.
(defn new-post
  "Add a new post to the database."
  [auth_name post_text]
  (jdbc/insert! db :blogs
                {:auth_name auth_name
                 :post_date (tc/to-sql-date (tm/now))
                 :post_time "now"
                 :post_text post_text}))

