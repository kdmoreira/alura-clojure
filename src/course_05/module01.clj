(ns course-05.module01
  (:require [schema.core :as s]))

(defn adiciona-paciente [pacientes paciente]
  (if-let [id (:id paciente)]
    (assoc pacientes id paciente)
    (throw (ex-info "Paciente não possui id" {:paciente paciente}))))

; {15 [], 20 {}, etc...}
(defn adiciona-visita
  [visitas, paciente, novas-visitas]
  (if (contains? visitas paciente)
    (update visitas paciente concat novas-visitas)
    (assoc visitas paciente novas-visitas)))

(defn imprime-relatorio-de-paciente [visitas, paciente]
  (println "Visitas do paciente" paciente "são" (get visitas paciente)))

; notice the problem with types, imprime-relatorio receives a patient
; as an id, while adiciona-visita receives a patient with all its attributes
; this could lead to mistakes
(defn testa-uso-de-pacientes []
  (let [guilherme {:id 15 :nome "guilherme"}
        daniela {:id 20 :nome "daniela"}
        paulo {:id 25 :nome "paulo"}
        pacientes (reduce adiciona-paciente {} [guilherme daniela paulo])
        visitas {}]
    (println pacientes)
    (println (adiciona-visita visitas 15 ["01/01/2019"]))
    (println (adiciona-visita visitas 15 ["01/02/2019", "01/01/2020"]))
    (println (adiciona-visita visitas 15 ["01/03/2019"]))
    (println (imprime-relatorio-de-paciente visitas daniela))))

(testa-uso-de-pacientes)

; SCHEMA
; when a value is valid, s/validate returns it
(s/validate Long 15) ; => 15
(s/validate Long "guilherme") ; => Execution error...

; s/defn defines a macro that defines a normal function, but we can
; indicate (no validation yet) that a parameter follows a specific type
(s/defn teste-simples [x :- Long] ; x follows Long schema
        (println x))
(teste-simples 30)
(teste-simples "guilherme")
; both cases work, even the last one

; to validate the schema
(s/set-fn-validation! true)

(teste-simples "guilherme")
; now it gives the error: [(named (not (instance? java.lang.Long "guilherme")) x)]

; redefining the previous function
(s/defn imprime-relatorio-de-paciente [visitas, paciente :- Long]
  (println "Visitas do paciente" paciente "são" (get visitas paciente)))

; now it will give an error since the function inside is called with the wrong param
(testa-uso-de-pacientes)

(s/defn novo-paciente
        [id :- Long, nome :- s/Str]
        {:id id, :nome nome})

(novo-paciente 15 "guilherme")
(novo-paciente "guilherme" 15)