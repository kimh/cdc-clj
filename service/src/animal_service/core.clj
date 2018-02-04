(ns animal-service.core
  (:gen-class)
  (:require [clojure.data.json :as json])
  (:use [compojure.route :only [files not-found]]
        [compojure.handler :only [site]]
        [compojure.core :only [defroutes GET POST DELETE ANY context]]
        org.httpkit.server))

(defn get-animal [req]
  {:status  200
   :body (json/write-str {"key" "value"})})

(defroutes all-routes
  (GET "/" [] get-animal))

(defn -main [& args]
  (run-server (site #'all-routes) {:port 9999}))
