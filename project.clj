(defproject devstopfix.auid "0.3.0"
  :description "Application Unique Identifiers"
  :url "https://github.com/devstopfix/auid"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/test.check "0.5.9"]]
  :profiles {
              :test {
                      :dependencies [[org.clojure/test.check "0.5.9"]]}})
