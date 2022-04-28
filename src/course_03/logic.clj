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

