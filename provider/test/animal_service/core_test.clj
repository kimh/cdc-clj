(ns animal-service.core-test
  (:require [clojure.test :refer :all]
            [clojure.data.json :as json]
            [clj-http.client :as http]
            [animal-service.core :as animal]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))

(defn verify-contract [contract]
  (let [request (:request contract)
        method (:get request)
        path (:path request)
        prepare-fn (:prepare request)
        expect (:expect contract)
        res (http/get (format "http://localhost:9999/%s" path) {:accept :json})]

    (is (= (:status res)
           (:status expect)))
    (is (= (-> (:body res) (json/read-str))
           (:body expect)))))
