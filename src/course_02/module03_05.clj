(ns course-02.module03-05
  (:require [course-02.loja-db :as l.db]
            [course-02.logic :as l.logic]))

(l.db/todos-pedidos)

; grouping by user
(group-by :usuario (l.db/todos-pedidos))

; how many users?
(count (vals (group-by :usuario (l.db/todos-pedidos))))

; how many orders each user has?
(map count (vals (group-by :usuario (l.db/todos-pedidos))))

; alternative with thread last
(->> (l.db/todos-pedidos)
     (group-by :usuario)
     vals
     (map count)) ; => (3 1 1 1)

; make it so it shows to whom the count belongs
(defn conta-total-por-usuario
  [[usuario pedidos]]
  {:usuario usuario :total-de-pedidos (count pedidos)})

(->> (l.db/todos-pedidos)
     (group-by :usuario)
     (map conta-total-por-usuario))

; including total price
(defn total-do-item
  [[item-id detalhes]]
  (* (get detalhes :quantidade 0) (get detalhes :preco-unitario 0)))

(defn total-do-pedido
  [pedidos]
  (reduce + (map total-do-item pedidos)))

(defn total-dos-pedidos
  [pedidos]
  (->> pedidos
       (map :itens)
       (map total-do-pedido)
       (reduce +)))

(defn quantia-pedidos-e-gasto-total-usuario
  [[usuario pedidos]]
  {:usuario usuario
   :total-de-pedidos (count pedidos)
   :preco-total (total-dos-pedidos pedidos)})

(->> (l.db/todos-pedidos)
     (group-by :usuario)
     (map quantia-pedidos-e-gasto-total-usuario))

(let [pedidos (l.db/todos-pedidos)
      resumo (l.logic/resumo-por-usuario (l.db/todos-pedidos))]
  (println "Resumo" resumo)
  (println "Ordenado" (sort-by :preco-total resumo))
  (println "Ordenado ao contrario" (reverse (sort-by :preco-total resumo)))
  (println "Ordenado por Id" (sort-by :usuario-id resumo))

  (println (get-in pedidos [0 :itens :mochila :quantidade])))

(defn resumo-por-usuario-ordenado [pedidos]
  (->> pedidos
       l.logic/resumo-por-usuario
       (sort-by :preco-total)
       reverse))

(let [pedidos (l.db/todos-pedidos)
      resumo (resumo-por-usuario-ordenado pedidos)]
  (println "Resumo" resumo)
  (println "Primeiro" (first resumo))
  (println "Segundo" (second resumo))
  (println "Resto" (rest resumo))
  (println "Total" (count resumo))
  (println "Class" (class resumo))
  (println "nth 1" (nth resumo 1))
  (println "get 1" (get resumo 1)) ; it will return nil since it's not a vector
  (println "take" (take 2 resumo)))

(defn top-2 [resumo]
  (take 2 resumo))

(let [pedidos (l.db/todos-pedidos)
      resumo (resumo-por-usuario-ordenado pedidos)]
  (println (filter #(> (:preco-total %) 500) resumo))
  (println (not (empty? (filter #(> (:preco-total %) 500) resumo)))) ; has anyone spend more than 500?
  (println (some #(> (:preco-total %) 500) resumo))) ; same effect as above

; KEEP
(defn gastou-bastante? [info-usuario]
  (> (:preco-total info-usuario)))

(let [pedidos (l.db/todos-pedidos)
      resumo (l.logic/resumo-por-usuario pedidos)]
  (println "keep" (keep gastou-bastante? resumo))
  (println "filter" (filter gastou-bastante? resumo)))

; LAZY SEQUENCES
; it will not execute the entire sequence (range) before taking 2,
; it generates chunks from the sequence
(println (range 10))
(println (take 2 (range 10000000000)))

(defn filtro1
  [x]
  (println "filtro1" x)
  x)

(defn filtro2
  [x]
  (println "filtro2" x)
  x)

; maps use chunks, in this case, 32 elements
(->> (range 50)
     (map filtro1)
     (map filtro2)
     println)

; using mapv it will load the sequence no matter its size
(->> (range 50)
     (mapv filtro1)
     (mapv filtro2)
     println)

; linked lists are 100% lazy
(->> '(0 1 2 3 4 5 6 7 8 9 0 0 1 2 3 4 5 6 7 8 9 0)
     (map filtro1)
     (map filtro2)
     println)