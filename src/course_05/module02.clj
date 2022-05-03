(ns course-05.module02
  (:require [schema.core :as s]))

; calling this global variable to activate validations
(s/set-fn-validation! true)

;(s/defrecord Paciente
;             [id :- Long, nome :- s/Str])
;(Paciente. 15 "guilherme")
;(Paciente. "15" "guilherme")

; using capital letter to indicate it's a schema
(def Paciente
  "Schema de um paciente"
  {:id s/Num, :nome s/Str})

(s/explain Paciente)
(s/validate Paciente {:id 15, :nome "guilherme"})

; key typo (:name instead of :nome) throws error, but this could lead to
; non-forward compatibility, which can be good depending on the context
(s/validate Paciente {:id 15, :name "guilherme"})
(s/validate Paciente {:id 15}) ; also throws error since it lacks a key

(s/defn novo-paciente :- Paciente
  [id :- Long, nome :- s/Str]
  {:id id, :nome nome, :plano []})

(novo-paciente 15, "guilherme") ; => {:plano disallowed-key}

; pure, simple and testable function
(defn estritamente-positivo? [x]
  (> x 0))

(def EstritamentePositivo (s/pred estritamente-positivo?
                                  'estritamente-positivo)) 
; 'some-example is optional, it removes namespacefrom the error

(s/validate EstritamentePositivo 15)
(s/validate EstritamentePositivo -2) ; => error

; combining schemas with s/constrained
(def Paciente
  "Schema de um paciente"
  {:id (s/constrained s/Int estritamente-positivo?), :nome s/Str})
; instead of estritamente-positivo, clojure's pos? could be used
; this combination can also be achieved with clojure's pos-int?
; it's not recommended to use lambdas in this case since they aren't
; easily identified in error messages since they don't have a name

(s/validate Paciente {:id 15, :nome "guilherme"})
(s/validate Paciente {:id 0, :nome "guilherme"}) ; => error