(ns animal-service.core-test
  (:require [clojure.test :refer :all]
            [clojure.data.json :as json]
            [clj-http.client :as http]
            [clojure.java.jdbc :as jdbc]
            [clojure.spec.gen.alpha :as gen]
            [animal-service.core :as animal]
            [animal-service.db :as db]
            [clojure.spec.alpha :as s]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))

(defn prepare [prep]
  (dotimes [_ (:count prep)]
    (let [db-row (-> (s/gen (:spec prep))
                     (gen/generate)
                     (merge (:overrides prep)))]
      (prn "inserting db-row" db-row)
      (db/insert-alligator db-row))))

(defn replay [request]
  (let [method (:get request)
        path (:path request)]
    (http/get (format "http://localhost:9999/%s" path) {:accept :json})))

(defn verify [contract]
  (let [request (:request contract)
        expects (:expects contract)
        context (:context contract)]
    (prepare context)
    (let [res (replay (:request contract))]
      (doseq [e expects]
        (when-not (e res)
          (throw (Exception. "contract is broken")))))))
