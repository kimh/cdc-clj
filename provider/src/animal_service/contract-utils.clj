(ns animal-service.contract-utils)

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

(defn have-one
  ([spec] (have-one spec {}))
  ([spec overrides]
   {:spec spec :count 1 :overrides overrides}))
