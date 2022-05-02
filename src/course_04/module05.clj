(ns course-04.module05)

(defn tipo-de-autorizador
  [pedido]
  (let [paciente (:paciente pedido)
        situacao (:situacao paciente)
        urgencia? (= :urgente situacao)]
    (cond (= :urgente situacao) :sempre-autorizado
          (contains? paciente :plano) :plano-de-saude
          :else :credito-minimo)))

(defmulti deve-assinar-pre-autorizacao? tipo-de-autorizador)
(defmethod deve-assinar-pre-autorizacao? :sempre-autorizado [pedido] false)
(defmethod deve-assinar-pre-autorizacao? :plano-de-saude [pedido]
  (not (some #(= % (:procedimento pedido)) (:plano (:paciente pedido)))))
(defmethod deve-assinar-pre-autorizacao? :credito-minimo [pedido]
  (>= (:valor pedido 0) 50))

(let [particular {:id 15, :nome "Guilherme", :nascimento "18/9/1981", :situacao :urgente}
      plano {:id 15, :nome "Guilherme", :nascimento "18/9/1981", :situacao :urgente, :plano [:raio-x, :ultrassom]}]
    (println (deve-assinar-pre-autorizacao? {:paciente particular, :valor 1000, :procedimento :coleta-de-sangue}))
    (println (deve-assinar-pre-autorizacao? {:paciente plano, :valor 1000, :procedimento :coleta-de-sangue})))