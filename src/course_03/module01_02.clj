(ns course-03.module01
  (:use [clojure pprint])
  (:require [course-03.hospital.model :as h.model]
             [course-03.logic :as h.logic]))

; using global variables like this will cause problems when using different threads
(defn simula-um-dia []
    ; root binding
  (def hospital (h.model/novo-hospital))
  (def hospital (h.logic/chega-em hospital :espera "111"))
  (def hospital (h.logic/chega-em hospital :espera "222"))
  (def hospital (h.logic/chega-em hospital :espera "333"))
  (pprint hospital)

  (def hospital (h.logic/chega-em hospital :laboratorio1 "444"))
  (def hospital (h.logic/chega-em hospital :laboratorio3 "555"))
  (pprint hospital)

  (def hospital (h.logic/atende hospital :laboratorio1))
  (def hospital (h.logic/atende hospital :espera))
  (pprint hospital)

  (def hospital (h.logic/chega-em hospital :espera "666"))
  (def hospital (h.logic/chega-em hospital :espera "777"))
  (def hospital (h.logic/chega-em hospital :espera "888"))
  (pprint hospital)

  (def hospital (h.logic/chega-em hospital :espera "999"))
  (pprint hospital))

(simula-um-dia)

; JAVA THREADS
(defn thread-example
  []
  (.start (Thread. (fn [] (println "oi1"))))
  (.start (Thread. (fn [] (println "oi2"))))
  (.start (Thread. (fn [] (println "oi3"))))
  (.start (Thread. (fn [] (println "oi4")))))

(thread-example)