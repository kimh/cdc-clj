(ns animal-service.core
  (:gen-class)
  (:require [clojure.data.json :as json]
            [animal-service.db :as db]
            [animal-service.contract :refer [defcontract] ]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.spec.test.alpha :as stest])
  (:use [compojure.route :only [files not-found]]
        [compojure.handler :only [site]]
        [compojure.core :only [defroutes GET POST DELETE ANY context]]
        org.httpkit.server))

(s/def :animal/name string?)
(s/def :animal/alligator (s/keys :req [:animal/name]))

(defn get-alligator
  [name]
  (if-let [alligator (-> name
                         db/find-alligators
                         first)]
    {:status 200
     :body (json/write-str alligator)}
    {:status  404
     :body "NotFound"}))

(defcontract get-alligator
  {:context {:spec :animal/alligator :count 1 :overrides {:animal/name "Mike"}}
   :request {:method :get
             :path (format "/alligators/%s" "Mike")}
   :expects {:status 200
             :body "{\"name\":\"Mike\"}"}})

(defroutes all-routes
  (GET "/alligators/:name" [name] (get-alligator name)))

(defn -main [& args]
  (run-server (site #'all-routes) {:port 9999}))
