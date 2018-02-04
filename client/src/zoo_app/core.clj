(ns zoo-app.core
  (:gen-class)
  (:require [clojure.data.json :as json]
            [org.httpkit.client :as http]))

(def animal-service-url "http://localhost:9999")

(defn get-animal []
  (http/get animal-service-url
            (fn [{:keys [status headers body error]}] ;; asynchronous response handling
              (if error
                (println "Failed, exception is " error)
                (println "Async HTTP GET: " status)))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
