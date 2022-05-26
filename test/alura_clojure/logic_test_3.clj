(ns alura-clojure.logic-test-3
  (:require [clojure.test :refer :all]
            [course-06.module04-06 :refer :all]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]))

(deftest cabe-na-fila?-test
  (testing "Que cabe pessoas em filas de tamanho até 4 inclusive"
    (doseq [fila (gen/sample (gen/vector gen/string-alphanumeric 0 4))]
      (is (cabe-na-fila? {:espera fila}, :espera)))))

; careful with doseq since it can generate a multiplication of cases,
; including repetitions, in the test below it generates 50 assertions
(deftest chega-em-test
  (testing "Que é colocada uma pessoa em filas menores que 5"
    (doseq [fila (gen/sample (gen/vector gen/string-alphanumeric 0 4) 10)
            pessoa (gen/sample gen/string-alphanumeric 5)]
      (println pessoa fila))))

(defspec explorando-a-api 10
  (prop/for-all [fila (gen/vector gen/string-alphanumeric 0 4)
                 pessoa gen/string-alphanumeric]
                (print pessoa fila)
                true))