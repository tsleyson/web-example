(ns web-example.dbtest
  (:require [clojure.java.jdbc :as jdbc]))

;; ''@localhost is the user from mysql's perspective.
;; '' is the password.
;; Go to mysql and use create database <name> to make a database
;; create table <name>(attrname type [, attrname type]) to make a table.
(def mysql-db {:subprotocol "mysql"
               :subname "//127.0.0.1:3306/clojure_test"
               ;:user ""  Don't need for local use.
               ;:password ""
               })

(def postgres-db {:subprotocol "postgresql"
                  :classname "org.postgresql.Driver"
                  :subname "//localhost:5432/trisdan"})

(defn -main []
  (do
    (jdbc/insert! postgres-db :fruits
                  {:name "Apple" :appearance "rosy" :cost 24}
                  {:name "Orange" :appearance "round" :cost 29})

    (jdbc/query postgres-db
                ["select * from fruits where appearance = ?" "rosy"]
                :row-fn :cost)))

