(ns course-05.module03
  (:require [schema.core :as s]))

; working with more complex schemas

(s/set-fn-validation! true)

(def PosInt (s/pred pos-int? 'inteiro-positivo))

(def Paciente
  {:id (s/pred pos-int?), :nome s/Str})

(s/defn novo-paciente :-  Paciente
  [id :- (s/pred pos-int?)
   nome :- s/Str]
  {:id id, :nome nome})

(novo-paciente 15, "guilherme")

(defn maior-ou-igual-a-zero? [x] (>= x 0))
(def ValorFinanceiro (s/constrained s/Num maior-ou-igual-a-zero?))

(def Pedido
  {:paciente Paciente
   :valor ValorFinanceiro
   :procedimento s/Keyword})

(s/defn novo-pedido :- Pedido
        [paciente :- Paciente, valor :- ValorFinanceiro, procedimento :- s/Keyword]
        {:paciente paciente, :valor valor, :procedimento procedimento})

(def Numeros [s/Num])
(s/validate Numeros [15])
(s/validate Numeros [15, 13, 1.0, 0])
(s/validate Numeros [])
(s/validate Numeros nil) ; it works since it's an empty sequence
(s/validate Numeros [nil]) ; => error

(def Plano [s/Keyword])
(s/validate Plano [:raio-x])

; redefinition using Plano
(def Paciente
  {:id (s/pred pos-int?), :nome s/Str, :plano Plano})

(s/validate Paciente {:id 15, :nome "guilherme", :plano [:raio-x]})
(s/validate Paciente {:id 15, :nome "guilherme", :plano [:raio-x, :ultra]})
(s/validate Paciente {:id 15, :nome "guilherme", :plano []})
(s/validate Paciente {:id 15, :nome "guilherme", :plano nil})
(s/validate Paciente {:id 15, :nome "guilherme"}) ; => error