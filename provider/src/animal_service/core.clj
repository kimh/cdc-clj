(ns animal-service.core
  (:gen-class)
  (:require [clojure.data.json :as json]
            [animal-service.db :as db]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.spec.test.alpha :as stest])
  (:use [compojure.route :only [files not-found]]
        [compojure.handler :only [site]]
        [compojure.core :only [defroutes GET POST DELETE ANY context]]
        org.httpkit.server))

(defn have-one
  ([spec] (have-one spec {}))
  ([spec overrides]
   {:spec spec :count 1 :overrides overrides}))

(s/def :animal/name string?)
(s/def :animal/alligator (s/keys :req [:animal/name]))


(defn to-file
  "Save a clojure form to a file"
  [file form]
  (with-open [w (java.io.FileWriter. file)]
    (print-dup form w)))

(defn from-file
  "Load a clojure form from file."
  [file]
  (with-open [r (java.io.PushbackReader. (java.io.FileReader. file))]
    (read r)))

;; TODO: this breaks when name var doesn't exist. How can I fix it?
(defmacro defcontract [name contract]
  `(if (resolve '~name)
     (alter-meta! (var ~name) #(assoc % :contracts ~contract))))

(defn save-contracts []
  (let [ns-vars (vals (ns-publics 'animal-service.core))
        contracts (remove nil? (map (fn [v]
                                      (if-let [contexts (-> v meta :contracts)]
                                        {(-> v meta :name str) contexts}))
                                    ns-vars))]
    (doseq [c contracts]
      (let [name (first (keys c))
            contracts (first (vals c))]
        (prn "writing contracts" (format "/tmp/contracts/%s.clj" name))
        (to-file (format "/tmp/contracts/%s.clj" name) contracts)))))

(defn contract-get-alligator-by-name []
  (let [name "Mike"]
    {:context (have-one :animal/alligator {:animal/name name})
     :request {:method :get
               :path (format "/alligators/%s" name)}
     :expects {:status 200
               :body "{\"name\":\"Mike\"}"}}))

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
