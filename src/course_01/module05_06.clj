(ns course-01.module05-06)

(def estoque {"Mochila" 10
              "Camiseta" 5})
estoque

(println "Temos" (count estoque) "elementos")
(println "Chaves são " (keys estoque))
(str "Valores são " (vals estoque))

(def estoque {:mochila 10
              :camiseta 5})
estoque

(assoc estoque :cadeira 3)
estoque

(update estoque :mochila inc)

(defn tira-um
  [valor]
  (println "Tirando um de" valor)
  (- valor 1))

(tira-um 5)
(update estoque :camiseta tira-um)
(update estoque :camiseta #(- % 1))
(dissoc estoque :mochila) ; => {:camiseta 5}

(def pedido {:mochila {:quantidade 2, :preco 80}
             :camiseta {:quantidade 2, :preco 40}})

(assoc pedido :chaveiro {:quantidade 1, :preco 10})

; careful with null pointer exception when pedido is nil
(pedido :mochila)
(:mochila pedido)
(get pedido :mochila)
(:quantidade (:mochila pedido))
(get-in pedido [:mochila :preco])
(update-in pedido [:mochila :quantidade] inc)

; THREADING FIRST
(-> pedido
    :mochila
    :quantidade) ; => 2

(-> pedido
    :mochila
    :quantidade
    inc) ; => 3

(defn imprime-e-etc [valor]
  (str valor "e etc"))

(imprime-e-etc pedido)

; destructuring a vector
(defn imprime-e-etc [[chave valor]]
  (str chave valor "e etc"))

(map imprime-e-etc pedido)

; _ could have been chave, but since it won't be used, _ is better
(defn preco-dos-produtos
  [[_ valor]]
  (* (:preco valor) (:quantidade valor)))

(map preco-dos-produtos pedido)
(reduce + (map preco-dos-produtos pedido))

(defn total-do-pedido
  [pedido]
  (reduce + (map preco-dos-produtos pedido)))

(total-do-pedido pedido)

; THREAD LAST ->>
(defn total-do-pedido-v2
  [pedido]
  (->> pedido
      (map preco-dos-produtos)
      (reduce +)))
; collections are passed last, since as parameters they would come
; last with these functions

(total-do-pedido-v2 pedido)

(defn preco-total-do-produto
  [produto]
  (* (:quantidade produto) (:preco produto)))

; using vals to iterate over values
(vals pedido)

(defn total-do-pedido-v3
  [pedido]
  (->> pedido
       vals
       (map preco-total-do-produto)
       (reduce +)))

(total-do-pedido-v3 pedido)


(def pedido {:mochila {:quantidade 2, :preco 80}
             :camiseta {:quantidade 2, :preco 40}
             :chaveiro {:quantidade 1}})

(defn gratuito?
  [item]
  (<= (get item :preco 0) 0))

(filter gratuito? (vals pedido))
(filter (fn [[chave item]] (gratuito? item)) pedido)
(filter #(gratuito? (second %)) pedido)

(defn pago?
  [item]
  (not (gratuito? item)))

(pago? {:preco 50})

; COMP to compose functions
(defn pago?-v2 [item] ((comp not gratuito?) item))
(pago?-v2 {:preco 0})
(pago?-v2 {:preco 50})

(def clientes [
               {:nome "Guilherme"
                :certificados ["Clojure", "Java", "Machine Learning"]}
               {:nome "Paulo"
                :certificados ["Java", "Ciência da Computação"]}
               {:nome "Daniela"
                :certificados ["Arquitetura", "Gastronomia"]}])

(->> clientes
     (map :certificados)
     (map count)
     (reduce +)) ; => 7