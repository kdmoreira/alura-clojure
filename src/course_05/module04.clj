(ns course-05.module04
  (:use clojure.pprint)
  (:require [schema.core :as s]))

; validating dynamic maps

(s/set-fn-validation! true)

; alias
(def PosInt (s/pred pos-int? 'inteiro-positivo))

; alias
(def Plano [s/Keyword])

(def Paciente
  {:id PosInt,
   :nome s/Str,
   :plano Plano,
   (s/optional-key :nascimento) s/Str}) ; optional

(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano [:raio-x, :ultrassom]}))
(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano [:raio-x]}))
(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano []}))
(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano nil}))
(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano [], :nascimento "18/09/1981"}))

; DYNAMIC MAP KEYS

; another kind of map that can be used, that doesn't have keywords
(def Pacientes
  {PosInt Paciente})

; in these cases, clojure doesn't throw errors 
; since it recognizes that it's a dynamic map
(pprint (s/validate Pacientes {}))
( let [guilherme {:id 15, :nome "guilherme", :plano [:raio-x]}
       daniela {:id 20, :nome "daniela", :plano []}]
 (pprint (s/validate Pacientes {15 guilherme}))
 (pprint (s/validate Pacientes {15 guilherme, 20 daniela})))

; alias
(def Visitas
  {PosInt [s/Str]})