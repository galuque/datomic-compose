(require '[datomic.api :as d])

(def uri "datomic:sql://datomic?jdbc:postgresql://postgres:5432/datomic?user=datomic&password=datomic")

(defn create-datomic-db [uri]
  (let [halt? (atom false)]
    (loop []
      (try
        (d/create-database uri)
        (println "Database created successfully")
        (swap! halt? not)
        (catch Exception _e
          (println "Failed to create database, retrying in 5 seconds")
          (Thread/sleep 5000)))
      (when-not @halt? (recur)))))

(create-datomic-db uri)