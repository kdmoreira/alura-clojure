(ns course-04.module04)

; similar to module02 but now we consider the urgency (situacao) of the patient
(defrecord PacienteParticular [id, nome, nascimento, situacao])
(defrecord PacientePlanoDeSaude [id, nome, nascimento, plano, situacao])

(defprotocol Cobravel
  (deve-assinar-pre-autorizacao? [paciente procedimento valor]))

(defn nao-eh-urgente? [paciente]
  (not= :urgente (:situacao paciente :normal)))

(extend-type PacienteParticular
  Cobravel
  (deve-assinar-pre-autorizacao? [paciente, procedimento, valor]
    (and (>= valor 50) (nao-eh-urgente? paciente))))

(extend-type PacientePlanoDeSaude
  Cobravel
  (deve-assinar-pre-autorizacao? [paciente, procedimento, valor]
    (let [plano (:plano paciente)]
      (and (not (some #(= % procedimento) plano)) (nao-eh-urgente? paciente)))))

(let [particular (->PacienteParticular 15, "Guilherme", "18/09/1981", :urgente)
      plano (->PacientePlanoDeSaude 15, "Guilherme", "18/09/1981", 
                                    [:raio-x, :ultrassom], :moderado)]
  (println (deve-assinar-pre-autorizacao? particular, :raio-x, 500))
  (println (deve-assinar-pre-autorizacao? particular, :raio-x, 40))
  (println (deve-assinar-pre-autorizacao? plano, :raio-x, 9999))
  (println (deve-assinar-pre-autorizacao? plano, :coleta-de-sangue, 9999)))

; DEFMULTI
; using multi suffix just to keep the function inside the same file
; this suffix is not a convention
(defmulti deve-assinar-pre-autorizacao-multi? class)
(defmethod deve-assinar-pre-autorizacao-multi? PacienteParticular 
  [paciente] true)
(defmethod deve-assinar-pre-autorizacao-multi? PacientePlanoDeSaude 
  [paciente] false)

(let [particular (->PacienteParticular 15, "Guilherme", "18/09/1981", :urgente)
      plano (->PacientePlanoDeSaude 15, "Guilherme", "18/09/1981", 
                                    [:raio-x, :ultrassom], :urgente)]
  (println (deve-assinar-pre-autorizacao-multi? particular))
  (println (deve-assinar-pre-autorizacao-multi? plano)))

; exploring the function that defines the strategy of a defmulti
(defn minha-funcao [p]
  (println p)
  (class p))

(defmulti multi-teste minha-funcao)
(multi-teste "guilherme")
; => No method in multimethod 'multi-teste' for dispatch value: class java.lang.String

; not ideal since it mixes classes and keywords as keys,
; but only for testing purposes (see a better approach in module05.clj)
(defn tipo-de-autorizador
  [pedido]
  (let [paciente (:paciente pedido)
        situacao (:situacao paciente)
        urgencia? (= :urgente situacao)]
    (if urgencia?
      :sempre-autorizado
      (class paciente))))

(defmulti deve-assinar-pre-autorizacao-do-pedido? tipo-de-autorizador)
(defmethod deve-assinar-pre-autorizacao-do-pedido? :sempre-autorizado [pedido] false)
(defmethod deve-assinar-pre-autorizacao-do-pedido? PacienteParticular [pedido]
  (>= (:valor pedido 0) 50))
(defmethod deve-assinar-pre-autorizacao-do-pedido? PacientePlanoDeSaude [pedido]
  (not (some #(= % (:procedimento pedido)) (:plano (:paciente pedido)))))