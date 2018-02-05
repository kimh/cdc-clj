(ns animal-service.db
  (:require [clojure.java.jdbc :as jdbc]))

(def db-spec {:subprotocol "sqlite"
              :subname "test_db.sqlite"})

(def animal-table-ddl
  (jdbc/create-table-ddl :animals
                         [[:name "varchar(32)"]]))

;; Create table
;; (jdbc/db-do-commands db-spec [animal-table-ddl])

;; Insert initial data
;; (jdbc/insert! db-spec :animals {:name "Mary"})

;; Select raw
;; (jdbc/query db-spec ["SELECT * FROM animals WHERE name = ?" "Mary"])
