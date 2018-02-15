(defproject animal-service "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [http-kit "2.2.0"]
                 [compojure "1.1.5"]
                 [clj-http "3.7.0"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/java.jdbc "0.7.5"]
                 [org.xerial/sqlite-jdbc "3.8.6"]
                 [org.clojure/clojure "1.9.0"]
                 [org.clojure/test.check "0.10.0-alpha2"]]
  :plugins [[cider/cider-nrepl "0.16.0"]]
  :main ^:skip-aot animal-service.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
