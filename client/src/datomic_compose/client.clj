(ns datomic-compose.client
  (:require [datomic.client.api :as d]))

(def client
  (d/client
   {:server-type :peer-server
    :endpoint "localhost:8998"
    :secret "secret"
    :access-key "accesskey"
    :validate-hostnames false}))

(def conn (d/connect client {:db-name "datomic"}))

;; Get the current value of the database:
(def db (d/db conn))

;; Create a query for all movie titles:
(def all-titles-q '[:find ?movie-title
                    :where [_ :movie/title ?movie-title]])

;; Execute the query with the value of the database:
(d/q all-titles-q db)