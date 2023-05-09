(ns datomic-compose.peer
  (:require [datomic.api :as d]))

(def uri "datomic:sql://datomic?jdbc:postgresql://localhost:5432/datomic?user=datomic&password=datomic")

(def conn (d/connect uri))

(def movie-schema [{:db/ident :movie/title
                    :db/valueType :db.type/string
                    :db/cardinality :db.cardinality/one
                    :db/doc "The title of the movie"}

                   {:db/ident :movie/genre
                    :db/valueType :db.type/string
                    :db/cardinality :db.cardinality/one
                    :db/doc "The genre of the movie"}

                   {:db/ident :movie/release-year
                    :db/valueType :db.type/long
                    :db/cardinality :db.cardinality/one
                    :db/doc "The year the movie was released in theaters"}])

;; Now transact the schema:
(d/transact conn movie-schema)

;; Define some movies to add to the database:
(def first-movies [{:movie/title "The Goonies"
                    :movie/genre "action/adventure"
                    :movie/release-year 1985}
                   {:movie/title "Commando"
                    :movie/genre "action/adventure"
                    :movie/release-year 1985}
                   {:movie/title "Repo Man"
                    :movie/genre "punk dystopia"
                    :movie/release-year 1984}])

;; Now transact the movies:
(d/transact conn first-movies)

;; Get the current value of the database:
(def db (d/db conn))

;; Create a query for all movie titles:
(def all-titles-q '[:find ?movie-title
                    :where [_ :movie/title ?movie-title]])

;; Execute the query with the value of the database:
(d/q all-titles-q db)