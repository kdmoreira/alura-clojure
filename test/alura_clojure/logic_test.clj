(ns alura-clojure.logic-test
  (:require [clojure.test :refer :all]
            [course-06.module01-03 :refer :all]))

(deftest cabe-na-fila?-test
  
  ; boundary tests
  ; exact borders and one off: -1, +1, <=, >=, =
  ; make a mental checklist

  ; zero border
  (testing "Que cabe numa fila fila"
    (is (cabe-na-fila? {:espera []}, :espera)))
  
  ; limit border
  (testing "Que não cabe na fila quando a fila está cheia"
    (is (not (cabe-na-fila? {:espera [1 2 3 4 5]}, :espera))))
  
  ; one off limit border +1
  (testing "Que não cabe na fila quando tem mais do que uma fila cheia"
    (is (not (cabe-na-fila? {:espera [1 2 3 4 5 6]}, :espera))))
  
  ; inside borders
  (testing "Que cabe na fila quando tem gente, mas não está cheia"
    (is (cabe-na-fila? {:espera [1 2 3 4]}, :espera))
    (is (cabe-na-fila? {:espera [1 2]}, :espera)))
  
  (testing "Que não cabe quando o departamento não existe"
    (is (not (cabe-na-fila? {:espera [1 2 3 4]}, :raio-x)))))

(deftest chega-em-test
  
  (let [hospital-cheio {:espera [1, 35, 42, 64, 21]}]
    
    (testing "Aceita pessoas enquanto cabem pessoas na fila"

    ; previous implementations
    ;(is (= {:espera [1 2 3 4 5]}
    ;       (chega-em {:espera [1 2 3 4]}, :espera, 5)))

    ; tip: don't use sequential values only
    ;(is (= {:espera [1 2 5]}
    ;       (chega-em {:espera [1 2]}, :espera, 5)))

      (is (= {:hospital hospital-cheio, :resultado :sucesso}
             (chega-em {:espera [1, 35, 42, 64]}, :espera, 21)))

      (is (= {:hospital {:espera [1, 35, 42]}, :resultado :sucesso}
             (chega-em {:espera [1, 35]}, :espera, 42))))
    
    (testing "Não aceita pessoas quando a fila está cheia"
      
      (is (= {:hospital hospital-cheio, :resultado :impossivel-adicionar-na-fila}
             (chega-em hospital-cheio, :espera 76))))
    
    ; don't use generic exceptions or exception messages
    ;(is (thrown? clojure.lang.ExceptionInfo
    ;    (chega-em {:espera [1 2 3 4 5]}, :espera, 6)))

    ; also possible, but not ideal
    ;(is (try
    ;      (chega-em {:espera [1 35 42 64 21]}, :espera, 76)
    ;      false
    ;      (catch clojure.lang.ExceptionInfo exception
    ;        (= :impossivel-adicionar-na-fila (:tipo (ex-data exception))))))
    ))