(ns alura-clojure.logic-test-2
  (:use clojure.pprint)
  (:require [clojure.test :refer :all]
            [course-06.module04-06 :as hospital]
            [schema.core :as s]))

(s/set-fn-validation! true)

(deftest transfere-test
  (testing "Aceita pessoas se couberem na fila"
    (let [hospital-original {:espera (conj hospital/fila-vazia "5"),
                             :raio-x hospital/fila-vazia}]
      (is (= {:espera [], :raio-x ["5"]}
             (hospital/transfere hospital-original :espera :raio-x))))
    
    (let [hospital-original {:espera (conj hospital/fila-vazia "51" "5"), 
                             :raio-x (conj hospital/fila-vazia "13")}]
        (is (= {:espera ["5"]
                :raio-x ["13" "51"]}
               (hospital/transfere hospital-original :espera :raio-x)))
      
      (testing "recusa pessoas se não cabe"
        
        (let [hospital-cheio {:espera (conj hospital/fila-vazia "5"), 
                          :raio-x (conj hospital/fila-vazia "1" "54" "43" "12" "51")}]
      (is (thrown? clojure.lang.ExceptionInfo
                   (hospital/transfere hospital-cheio :espera :raio-x)))))
      
      (testing "Não pode invocar transferência sem hospital"
        (is (thrown? clojure.lang.ExceptionInfo
                     (hospital/transfere nil :espera :raio-x))))
      
      (testing "Condições obrigatórias"
        (let [hospital {:espera (conj hospital/fila-vazia "5"), 
                        :raio-x (conj hospital/fila-vazia "1" "54" "43" "12" "51")}]
          (is (thrown? AssertionError
                       (hospital/transfere hospital :nao-existe :raio-x)))
          (is (thrown? AssertionError
                       (hospital/transfere hospital :raio-x :nao-existe)))
          )))))
