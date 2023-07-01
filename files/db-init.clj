(require '[datomic.api :as d])

(def uri "datomic:sql://datomic?jdbc:postgresql://postgres:5432/datomic?user=datomic&password=datomic")

(defn create-datomic-db [uri]
  (let [halt? (atom false)]
    (loop []
      (try
        (let [created? (d/create-database uri)]
          (if created?
            (println "Database created successfully")
            (println "Database already exists")))
        (reset! halt? true)
        (catch Exception _e
          (println "Failed to create database, retrying in 5 seconds")
          (Thread/sleep 5000)))
      (if @halt? 
        (System/exit 0)
        (recur)))))

(create-datomic-db uri)
