(ns animal-service.core
  (:gen-class)
  (:require [clojure.data.json :as json]
            [animal-service.db :as db]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest])
  (:use [compojure.route :only [files not-found]]
        [compojure.handler :only [site]]
        [compojure.core :only [defroutes GET POST DELETE ANY context]]
        org.httpkit.server))

;; (s/fdef ranged-rand
;;         :args (s/and (s/cat :start int? :end int?)
;;                      #(< (:start %) (:end %)))
;;         :ret int?
;;         :fn (s/and #(>= (:ret %) (-> % :args :start))
;;                    #(< (:ret %) (-> % :args :end))))
;; (s/def ::status int?)
;; (s/def ::body string?)
;; (s/def ::response (s/keys :req [::status ::body]))

;; (s/fdef get-alligator
;;         :args (s/cat :name string?)
;;         :ret ::response)
(s/def :zoo/id int?)
(s/def :zoo/name string?)
(s/def ::alligator (s/keys :req [:zoo/id :zoo/name]))

(defn contract-get-alligator-by-name []
  (let [name "Mary"]
    {:request {:method :get
               :path (format "/alligators/%s" name)
               :prepare #(assoc (s/gen ::alligator) :name name)}
     :expect {:status 200
               :body {"name" name}}}))

(defn get-alligator
  [name]
  (if-let [alligator (-> name
                         db/find-alligators
                         first)]
    {:status 200
     :body (json/write-str alligator)}
    {:status  404
     :body "NotFound"}))

(defroutes all-routes
  (GET "/alligators/:name" [name] (get-alligator name)))

(defn -main [& args]
  (run-server (site #'all-routes) {:port 9999}))
