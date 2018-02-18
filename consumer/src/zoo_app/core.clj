(ns zoo-app.core
  (:gen-class)
  (:require [clojure.data.json :as json]
            [clj-http.client :as http]
            [clojure.spec.alpha :as s]))

(def animal-service-url "http://localhost:9999")

(defn ->ok [json-str]
  (let [body (json/read-str json-str)]
    (into {}
          (for [[k v] body]
            [(keyword k) v]))))

(defn ->not-found []
  {:error "not found"})

(defn ->error []
  {:error "something bad happend"})

(s/def :zoo/id int?)
(s/def :zoo/name string?)
(s/def ::alligator (s/keys :req [:zoo/id :zoo/name]))

;; TODO: can we do something like this?
;; some concerns;
;; - how do we save the contract and replya on server?
;; - when verify the contract on server test, how do we populate data?
(defn contract-get-alligator-by-name []
  (let [name "Mary"]
    [{:request {:method :get
                :path (format "/alligators/" name)}
      :recieve {:status 200
                :body {:name name :family "alligatoridae"}}}]))

(defn get-alligator-by-name
  [url name]
  (let [res (http/get (format "%s/alligators/%s" url name) {:accept :json})
        status (:status res)]
    (case status
      200 (->ok (:body res))
      404 (->not-found)
      (->error))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
