(ns animal-service.core
  (:gen-class)
  (:require [clojure.data.json :as json]
            [animal-service.db :as db])
  (:use [compojure.route :only [files not-found]]
        [compojure.handler :only [site]]
        [compojure.core :only [defroutes GET POST DELETE ANY context]]
        org.httpkit.server))

(defn get-alligator
  [name]
  (if-let [alligator (-> name
                         db/find-alligators
                         first)]
    {:status  200
     :body (json/write-str alligator)}
    {:status  404
     :body "NotFound"}))

(defroutes all-routes
  (GET "/alligators/:name" [name]
       (get-alligator name)))

(defn -main [& args]
  (run-server (site #'all-routes) {:port 9999}))
