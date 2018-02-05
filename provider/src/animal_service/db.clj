(ns animal-service.db
  (:require [clojure.java.jdbc :as jdbc]))

(def db-spec {:subprotocol "sqlite"
              :subname "test_db.sqlite"})

(def table-ddl
  (jdbc/create-table-ddl :alligators
                         [[:name "varchar(32)"]]))

(defn find-alligators [name]
  (jdbc/query db-spec ["SELECT * FROM alligators WHERE name = ?" name]))

;; Create table
;; (jdbc/db-do-commands db-spec [table-ddl])

;; Insert initial data
;; (jdbc/insert! db-spec :alligators {:name "Mary"})
