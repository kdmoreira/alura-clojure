(ns course-06.module04-06
  (:require [schema.core :as s]))

(def fila-vazia clojure.lang.PersistentQueue/EMPTY)

(defn novo-hospital []
    {:espera fila-vazia
    :laboratorio1 fila-vazia
    :laboratorio2 fila-vazia
    :laboratorio3 fila-vazia})

(s/def PacienteID s/Str)
(s/def Departamento [PacienteID])
(s/def Hospital {s/Keyword Departamento})

(s/validate PacienteID "Guilherme")
(s/validate Departamento ["Guilherme" "Daniela"])
(s/validate Hospital {:espera ["Guilherme" "Daniela"]})
     
(s/defn atende :- Hospital
  [hospital :- Hospital, departamento :- s/Keyword]
  (update hospital departamento pop))

(s/defn proxima :- PacienteID
  "Retorna o próximo paciente da fila"
  [hospital :- Hospital, departamento :- s/Keyword]
  (-> hospital
      departamento
      peek))

(defn cabe-na-fila?
  [hospital departamento]
  (some-> hospital
          departamento
          count
          (< 5)))

(defn chega-em
  [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)
    (throw (ex-info "Não cabe ninguém neste departamento" {:paciente pessoa}))))

(defn mesmo-tamanho? [hospital, outro-hospital, de, para]
  (= (+ (count (get outro-hospital de)) (count (get outro-hospital para)))
     (+ (count (get hospital de)) (count (get hospital para)))))

(s/defn transfere :- Hospital
  "Transfere o próximo paciente da fila de para a fila para"
  [hospital :- Hospital, de :- s/Keyword, para :- s/Keyword]
        {
         :pre [(contains? hospital de), (contains? hospital para)]
         :post [mesmo-tamanho?]
        }
  (let [pessoa (proxima hospital de)]
    (-> hospital
        (atende de)
        (chega-em para pessoa))))