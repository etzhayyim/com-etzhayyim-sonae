(require '[clojure.test :as t])

(doseq [ns-sym '[sonae.methods.test-charter-gates
                  sonae.murakumo-test
                  sonae.repository-contract-test]]
  (require ns-sym))

(let [result (apply t/run-tests
                    '[sonae.methods.test-charter-gates
                      sonae.murakumo-test
                      sonae.repository-contract-test])]
  (System/exit (if (zero? (+ (:fail result) (:error result))) 0 1)))
