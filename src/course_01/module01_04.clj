(ns course-01.module01-04)

; CLOJURE: INTRODUCTION TO FUNCTIONAL PROGRAMMING

(println "Bem-vindo ao sistema de estoque")

(def total-de-produtos 15)
total-de-produtos
(println total-de-produtos)
(println "Total" total-de-produtos)
(+ 13 3)
(- 13 3)
(def total-de-produtos (+ total-de-produtos 3))
total-de-produtos

(def estoque ["Mochila", "Camiseta"])
estoque
(estoque 0)
(estoque 1)
(count estoque)
(conj estoque "Cadeira")
estoque ; => "Mochila" "Camiseta"
(def estoque (conj estoque "Cadeira"))
estoque

(defn imprime-mensagem
  []
  (println "---------------------")
  (println "Bem-vindo ao estoque!"))
(imprime-mensagem)

(defn valor-descontado-v1
  "Retorna o valor com desconto de 10%."
  [valor-bruto]
  (let [desconto 0.10]
    (* valor-bruto (- 1 desconto))))

(valor-descontado-v1 100)

; N stands for BigInt and M stands for BigDecimal
(* (/ 10 100) 100) ; => 10N
(class 10M)
(class 10N)

(defn valor-descontado-v2
  "Retorna o valor com desconto de 10%."
  [valor-bruto]
  (let [taxa-de-desconto (/ 10 100)
        desconto         (* valor-bruto taxa-de-desconto)]
    (println "Calculando desconto de " desconto)
    (- valor-bruto desconto)))

(valor-descontado-v2 100)

(> 500 100)
(< 500 100)

(if (> 500 100)
  (println "maior")
  (println "menor ou igual"))

(defn valor-descontado-v3
  "Retorna o valor com desconto de 10% se o valor bruto for estritamente maior que 100."
  [valor-bruto]
  (if (> valor-bruto 100)
    (let [taxa-de-desconto (/ 10 100)
          desconto         (* valor-bruto taxa-de-desconto)]
      (println "Calculando desconto de " desconto)
      (- valor-bruto desconto))
    valor-bruto))

(valor-descontado-v3 101)

(defn aplica-desconto-when?
  [valor-bruto]
  (when (> valor-bruto 100)
    true))

(defn aplica-desconto?
  [valor-bruto]
  (> valor-bruto 100))

(aplica-desconto? 100)

(defn valor-descontado-v4
  "Retorna o valor com desconto de 10% se o valor bruto for estritamente maior que 100."
  [valor-bruto]
  (if (aplica-desconto? valor-bruto)
    (let [taxa-de-desconto (/ 10 100)
          desconto         (* valor-bruto taxa-de-desconto)]
      (println "Calculando desconto de " desconto)
      (- valor-bruto desconto))
    valor-bruto))

(valor-descontado-v4 100)
(valor-descontado-v4 500)

(defn aplica?
  [valor-bruto]
  (> valor-bruto 100))

; HIGH ORDER FUNCTIONS
; passing functions as parameters

(defn valor-descontado-v5
  "Retorna o valor com desconto de 10% se deve aplicar desconto."
  [aplica? valor-bruto]
  (if (aplica? valor-bruto)
    (let [taxa-de-desconto (/ 10 100)
          desconto         (* valor-bruto taxa-de-desconto)]
      (println "Calculando desconto de " desconto)
      (- valor-bruto desconto))
    valor-bruto))

(valor-descontado-v5 aplica? 100)
(valor-descontado-v5 aplica? 500)

((fn [valor-bruto] (> valor-bruto 100)) 100)
((fn [valor-bruto] (> valor-bruto 100)) 101)

(#(> % 100) 100)
(#(> % 100) 101)

(defn valor-descontado-v6
  "Retorna o valor com desconto de 10% se deve aplicar desconto."
  [valor-bruto]
  (if (#(> % 100) valor-bruto)
    (let [taxa-de-desconto (/ 10 100)
          desconto         (* valor-bruto taxa-de-desconto)]
      (- valor-bruto desconto))
    valor-bruto))

(valor-descontado-v6 500)

(def precos [30 700 1000])

(println (precos 0))
(get precos 0) ; => 30
(get precos 99) ; => nil

(get precos 2 0)

; returns 0 if the 99th index doesn't exist
(get precos 99 0) ; =>

(conj precos 5)
precos

(update precos 0 inc) ; => [31 70 1000]

(defn soma-1
  [valor]
  (+ valor 1))

(soma-1 20)

; same effect
(update precos 0 soma-1)

(map inc precos)
(map soma-1 precos)

(map valor-descontado-v6 precos)

(range 10)
(filter even? (range 10))
(filter aplica? precos)

(reduce + (range 5)) ; => 10
(reduce + 1 (range 5)) ; => 11
(reduce + [2]) ; => 2