(ns sonae.repository-contract-test
  (:require [clojure.edn :as edn]
            [clojure.test :refer [deftest is]]
            [clojure.java.io :as io]))

(deftest canonical-repository-shape
  (doseq [path ["manifest.edn" "identity.edn" "dependencies.edn" "repository-contracts.edn"
                "registry/warning-sources.edn"]]
    (is (map? (edn/read-string (slurp path))) path))
  (is (not (.exists (io/file "manifest.jsonld"))))
  (is (= 6 (count (filter #(and (.isFile %) (.endsWith (.getName %) ".edn"))
                          (file-seq (io/file "lex")))))))
