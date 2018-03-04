(ns animal-service.contract)

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

(defn save-contracts [classpath]
  ;; TODO: remove this hardcode and make it pass with lein run -m
  (load classpath)
  (let [ns-vars (vals (ns-publics (-> classpath
                                      (clojure.string/replace #"^/" "")
                                      (clojure.string/replace #"_" "-")
                                      (clojure.string/replace #"/" ".")
                                      symbol)))
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

;; TODO: this breaks when name var doesn't exist but maybe it's ok?
(defmacro defcontract [name contract]
  `(alter-meta! (var ~name) #(assoc % :contracts ~contract)))
