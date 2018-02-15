(defproject zoo-app "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [clj-http "3.7.0"]
                 [compojure "1.1.5"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/test.check "0.10.0-alpha2"]]
  :main ^:skip-aot zoo-app.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
