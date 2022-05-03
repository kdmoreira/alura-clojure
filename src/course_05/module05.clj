(ns course-05.module05
  (:use clojure.pprint)
  (:require [schema.core :as s]))

(s/set-fn-validation! true)

(def PosInt (s/pred pos-int? 'inteiro-positivo))
(def Plano [s/Keyword])
(def Paciente
  {:id PosInt,
   :nome s/Str,
   :plano Plano,
   (s/optional-key :nascimento) s/Str})

(def Pacientes
  {PosInt Paciente})

(def Visitas
  {PosInt [s/Str]})

; using schemas to modify the code from module01.clj

; IF and THROW were removed from IF-LET since the 
; schema guarantees the validity when validation is active
(s/defn adiciona-paciente :- Pacientes
  [pacientes :- Pacientes paciente :- Paciente]
  (let [id (:id paciente)]
    (assoc pacientes id paciente)))

(s/defn adiciona-visita :- Visitas
  [visitas :- Visitas, paciente :- PosInt, novas-visitas :- [s/Str]]
  (if (contains? visitas paciente)
    (update visitas paciente concat novas-visitas)
    (assoc visitas paciente novas-visitas)))

(s/defn imprime-relatorio-de-paciente [visitas :- Visitas, paciente :- PosInt]
  (println "Visitas do paciente" paciente "são" (get visitas paciente)))

; using the functions

(defn testa-uso-de-pacientes []
  (let [guilherme {:id 15 :nome "guilherme", :plano []}
        daniela {:id 20 :nome "daniela", :plano []}
        paulo {:id 25 :nome "paulo", :plano []}
        pacientes (reduce adiciona-paciente {} [guilherme daniela paulo])
        visitas {}]
    (println pacientes)
    (println (adiciona-visita visitas 15 ["01/01/2019"]))
    (println (adiciona-visita visitas 15 ["01/02/2019", "01/01/2020"]))
    (println (adiciona-visita visitas 15 ["01/03/2019"]))
    (println (imprime-relatorio-de-paciente visitas daniela))))
; => now the last println will throw an error since daniela is not a PosInt

(testa-uso-de-pacientes)