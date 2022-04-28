(ns course-03.hospital.core
  (:use [clojure pprint])
  (:require [course-03.hospital.model :as h.model]))

(let [meu-hospital (h.model/novo-hospital)]
  (pprint meu-hospital))

(pprint h.model/fila_vazia)