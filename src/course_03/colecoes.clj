(ns course-03.colecoes
  ;(:use [clojure pprint]) ; also possible
  (:require [clojure.pprint :as pprint]))

(defn testa-vetor []
  (let [espera [111, 222]]
    (println espera)
    (println (conj espera 333))
    (println (conj espera 444))
    (println (pop espera))))

(testa-vetor)

(defn testa-lista []
  (let [espera '(111, 222)]
    (println espera)
    (println (conj espera 333))
    (println (conj espera 444))
    (println (pop espera))))

(testa-lista)

(defn testa-conjunto []
  (let [espera #{111, 222}]
        (println espera)
    (println (conj espera 333))
    (println (conj espera 444))
    ; (println (pop espera)) ; POP doesn't work on sets
    ))

(testa-conjunto)

(defn testa-fila []
  (let [espera (conj clojure.lang.PersistentQueue/EMPTY "111", "222")]
    (println "fila")
    (println (seq espera))
    (println (seq (conj espera "333")))
    (println (seq (pop espera)))
    (println (peek espera))))

(testa-fila)

; sometimes it's useful to use pretty printing instead 
; of calling seq just to display queues

(defn pretty-fila []
  (let [fila (conj clojure.lang.PersistentQueue/EMPTY "111", "222")]
    (pprint/pprint fila)))

(pretty-fila)