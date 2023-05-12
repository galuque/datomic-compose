;   Example adapted from: https://github.com/cognitect-labs/day-of-datomic-cloud
;   Copyright (c) Cognitect, Inc. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

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

(def db (d/db conn))

(def all-titles-q '[:find ?movie-title
                    :where [_ :movie/title ?movie-title]])

(comment
  
  (d/q all-titles-q db)
  
  )