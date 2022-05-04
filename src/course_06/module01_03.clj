(ns course-06.module01-03)

; SOME: if any intermediate result is nil, it returns nil
(defn cabe-na-fila?
  [hospital departamento]
  (some-> hospital
      departamento
      count
      (< 5)))

; alternative using WHEN-LET
(defn cabe-na-fila?-let
  [hospital departamento]
  (when-let [fila (get hospital departamento)]
  (-> fila
          count
          (< 5))))

; possible, but not ideal
(defn chega-em
  [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)
    (throw (ex-info 
            "Não cabe ninguém neste departamento."
            {:paciente pessoa,
             :tipo :impossivel-adicionar-na-fila}))))

; non-public def
(defn- tenta-colocar-na-fila
  [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)))

; a better way
;(defn chega-em
;  [hospital departamento pessoa]
;  (if-let [novo-hospital (tenta-colocar-na-fila hospital departamento pessoa)]
;    {:hospital novo-hospital, :resultado :sucesso}
;    {:hospital hospital, :resultado :impossivel-adicionar-na-fila}))

; using functions from previous courses

(defn chega-em
  [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)
    (throw (ex-info "Não cabe ninguém neste departamento" {:paciente pessoa}))))

(defn atende
  [hospital departamento]
  (update hospital departamento pop))

(defn proxima
  "Retorna a próxima pessoa"
  [hospital departamento]
  (peek (get hospital departamento)))

(defn transfere
  "Transfere para da fila de um departamento para a de outro"
  [hospital de para]
  (let [pessoa (proxima hospital de)]
    (-> hospital
        (atende ,,, de)
        (chega-em ,,, para pessoa))))