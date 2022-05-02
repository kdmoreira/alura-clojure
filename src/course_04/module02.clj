(ns course-04.module02)

(defrecord Paciente [id, nome, nascimento])
; Paciente Plano de Saude ===> + plano de saude
; Paciente Particular ===> + 0

(defrecord PacienteParticular [id, nome, nascimento])
(defrecord PacientePlanoDeSaude [id, nome, nascimento, plano])

; protocols can be used similarly to a strategy pattern
(defprotocol Cobravel
  (deve-assinar-pre-autorizacao? [paciente procedimento valor]))

; in this case they extend a type created previously
(extend-type PacienteParticular
  Cobravel
  (deve-assinar-pre-autorizacao? [paciente, procedimento, valor]
    (>= valor 50)))

(extend-type PacientePlanoDeSaude
  Cobravel
  (deve-assinar-pre-autorizacao? [paciente, procedimento, valor]
    (let [plano (:plano paciente)]
      (not (some #(= % procedimento) plano)))))

(let [particular (->PacienteParticular 15, "Guilherme", "18/09/1981")
      plano (->PacientePlanoDeSaude 15, "Guilherme", "18/09/1981", [:raio-x, :ultrassom])]
  (println (deve-assinar-pre-autorizacao? particular, :raio-x, 500))
  (println (deve-assinar-pre-autorizacao? particular, :raio-x, 40))
  (println (deve-assinar-pre-autorizacao? plano, :raio-x, 9999))
  (println (deve-assinar-pre-autorizacao? plano, :coleta-de-sangue, 9999)))

; more examples of polymorphism
(defprotocol Dateable
  (to-ms [this]))

(extend-type java.lang.Number
  Dateable
  (to-ms [this] this))

(to-ms 56)

(extend-type java.util.Date
  Dateable
  (to-ms [this] (.getTime this)))

(to-ms (java.util.Date.))

(extend-type java.util.Calendar
  Dateable
  (to-ms [this] (to-ms (.getTime this))))

(to-ms (java.util.GregorianCalendar.))

(defn agora []
  (to-ms (java.util.Date.)))