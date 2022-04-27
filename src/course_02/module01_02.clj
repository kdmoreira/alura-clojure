(ns course-02.module01-02)

(def nomes ["ana", "marcos", "vinicius"])
(map println nomes)
(first nomes)
; returns nil when the collection is empty
(next nomes)
; returns an empty collection when empty
(rest nomes)

; TAIL RECURSION
; recur must be called last
(defn meu-mapa
  [funcao sequencia]
  (let [primeiro (first sequencia)]
    (if (not (nil? primeiro))
      (do
        (funcao primeiro)
        (recur funcao (rest sequencia))))))

(meu-mapa println nomes)
(meu-mapa println [])

(defn minha-funcao
  ([parametro1] (println parametro1))
  ([parametro1 parametro2] (println parametro1 parametro2)))
(minha-funcao 1)
(minha-funcao 1 2)

(defn contar
  ([elementos]
   (contar 0 elementos))
  ([total-ate-agora elementos]
   (if (seq elementos)
     (recur (inc total-ate-agora) (next elementos))
     total-ate-agora)))

(println (contar nomes))
(println (contar []))

; LOOPS
(defn contar-loop
  [elementos]
  (loop [total-ate-agora 0
         elementos-restantes elementos]
    (if (seq elementos-restantes)
      (recur (inc total-ate-agora) (next elementos-restantes))
      total-ate-agora)))

(contar-loop nomes)
(contar-loop [])