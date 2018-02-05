(ns zoo-app.core
  (:gen-class)
  (:require [clojure.data.json :as json]
            [org.httpkit.client :as http]))

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

(defn get-alligator-by-name
  [name]
  (let [res (http/get (format "%s/alligators/%s" animal-service-url name) {:as :text})
        status (:status @res)]
    (case status
      200 (->ok (:body @res))
      404 (->not-found)
      (->error))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
