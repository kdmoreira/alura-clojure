(ns course-03.logic)

(defn cabe-na-fila?
  [hospital departamento]
  (let [fila (get hospital departamento)
        tamanho-atual (count fila)]
    (< tamanho-atual 5)))

(defn cabe-na-fila?-v2
  [hospital departamento]
  (-> hospital
      (get ,,, departamento)
      count ,,,
      (< ,,, 5)))

(defn chega-em
  [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)
    (throw (ex-info "Fila já está cheia" {:tentando-adicionar pessoa}))))

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

(defn atende-completo
  [hospital departamento]
  {:paciente (update hospital departamento peek)
   :hospital (update hospital departamento pop)})

; JUXT
(defn atende-completo-que-chama-ambos
  [hospital departamento]
  (let [fila (get hospital departamento)
        peek-pop (juxt peek pop) ; using both functions
        ; each result go inside the vector below
        [pessoa fila-atualizada] (peek-pop fila) 
        hospital-atualizado (update hospital assoc departamento fila-atualizada)]
    {:paciente pessoa
     :hospital hospital-atualizado}))