;   Example adapted from: https://github.com/cognitect-labs/day-of-datomic-cloud
;   Copyright (c) Cognitect, Inc. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

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

(comment
   ;; Now transact the schema:
  (d/transact conn movie-schema)
  )

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

(comment
   ;; Now transact the movies:
  (d/transact conn first-movies)
  )
  

;; Get the current value of the database:
(def db (d/db conn))
  

;; Create a query for all movie titles:
(def all-titles-q '[:find ?movie-title
                    :where [_ :movie/title ?movie-title]])

(comment
  ;; Execute the query with the value of the database:
  (d/q all-titles-q db)
  )