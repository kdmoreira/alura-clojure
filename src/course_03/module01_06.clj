(ns course-03.module01-06
  (:use [clojure pprint])
  (:require [course-03.hospital.model :as h.model]
            [course-03.logic :as h.logic]))

; using global variables like this will cause problems when using different threads
(defn simula-um-dia []
    ; root binding
  (def hospital (h.model/novo-hospital))
  (def hospital (h.logic/chega-em hospital :espera "111"))
  (def hospital (h.logic/chega-em hospital :espera "222"))
  (def hospital (h.logic/chega-em hospital :espera "333"))
  (pprint hospital)

  (def hospital (h.logic/chega-em hospital :laboratorio1 "444"))
  (def hospital (h.logic/chega-em hospital :laboratorio3 "555"))
  (pprint hospital)

  (def hospital (h.logic/atende hospital :laboratorio1))
  (def hospital (h.logic/atende hospital :espera))
  (pprint hospital)

  (def hospital (h.logic/chega-em hospital :espera "666"))
  (def hospital (h.logic/chega-em hospital :espera "777"))
  (def hospital (h.logic/chega-em hospital :espera "888"))
  (pprint hospital)

  (def hospital (h.logic/chega-em hospital :espera "999"))
  (pprint hospital))

(simula-um-dia)

; JAVA THREADS
(defn thread-example
  []
  (.start (Thread. (fn [] (println "oi1"))))
  (.start (Thread. (fn [] (println "oi2"))))
  (.start (Thread. (fn [] (println "oi3"))))
  (.start (Thread. (fn [] (println "oi4")))))

(thread-example)

; BINDINGS
; any thread that accesses this symbol will have access
; to the value "guilherme"
(def nome "guilherme")
(def nome 123) ; now nome is redefined and it could be dangerous

; local binding
(let [nome "guilherme"]
  (println nome)
  ; shadowing, not redoing the binding
  (let [nome "daniela"]
    (println nome))
  (println nome)) ; it will print guilherme, daniela, guilherme

; ATOM
; it's good practice to use ! when naming functions that cause side effects
(defn testa-atomo! []
  (let [hospital-silveira (atom {:espera h.model/fila_vazia})]
    (print hospital-silveira)
    (pprint hospital-silveira)
    ; it shows what's inside the atom, in this case, the map
    (pprint (deref hospital-silveira)) ; or (pprint @hospital-silveira), more common

    ; this won't change the map inside the atom
    (pprint (assoc @hospital-silveira :laboratorio1 h.model/fila_vazia))
    (pprint @hospital-silveira)

    ; this is one way of changing content inside an atom
    (swap! hospital-silveira assoc :laboratorio1 h.model/fila_vazia)
    (pprint @hospital-silveira)

    ; immutable update with dereference, doesn't change anything
    (update @hospital-silveira :laboratorio1 conj "111")))

(testa-atomo!)

; when different threads access the same atom, if thread A
; changes its value and thread B, which is still working
; on it, notices the change, a retry occurs for thread B,
; therefore it's like a transaction that avoids concurrency
; problems, it's also possible to limit the number of retries

; refactoring simula-um-dia
(defn chega-em! [hospital pessoa]
  (swap! hospital h.logic/chega-em :espera pessoa))

(defn transfere! [hospital de para]
  (swap! hospital h.logic/transfere de para))

(defn simula-um-dia-v2
  []
  (let [hospital (atom (h.model/novo-hospital))]
    (chega-em! hospital "joao")
    (chega-em! hospital "maria")
    (chega-em! hospital "daniela")
    (chega-em! hospital "guilherme")
    (transfere! hospital :espera :laboratorio1)
    (transfere! hospital :espera :laboratorio2)
    (transfere! hospital :espera :laboratorio2)
    (transfere! hospital :laboratorio2 :laboratorio3)
    (pprint hospital)))

(simula-um-dia-v2)

; --- SIDE NOTES ---
(def add10 (partial + 10))
(def numbers-to-add [1, 2, 3, 4, 5])

(map #(+ 10 %) numbers-to-add)
(map add10 numbers-to-add) ; same effect

(def children {:a 0 :b 0 :c 0})

(update children :a #(+ 10 %))
(assoc children :a 10) ; same effect

; ASSOC and UPDATE
; iterating over a map and updating its values
(defn give-allowance-update
  [children allowance]
  (into {} (map #(update % 1 allowance) children)))

(give-allowance-update children #(+ % 10))

(defn give-allowance-assoc
  [children allowance]
  (into {} (map #(assoc % 1 allowance) children)))

(give-allowance-assoc children 10)

; DOSEQ
; always return nil and it's mostly used for side effects
(defn print-keys-doseq
 [collection]
 (doseq [k collection]
   (println (key k))))

(print-keys-doseq children)
; also prints keys
(println (keys children))

; FOR
; builds a lazy seq and returns it
(defn print-keys-for
  [collection]
  (for [key (keys collection)]
    key))

(print-keys-for children)

; DOTIMES
; returns nil and it's used for side effects, similarly to doseq
(dotimes [n 5] 
  (println n)) ; prints from 1 to 5

(defn chega-em [fila pessoa]
  (conj fila pessoa))

; notice the syntax, chega-em receives a dereferenced fila
(defn chega-em!
  "Troca de referencia via ref-set"
  [hospital pessoa]
  (let [fila (get hospital :espera)]
    (ref-set fila (chega-em @fila pessoa))))

; different syntax compared to ref-set
(defn chega-em!
  "Troca de referencia via alter"
  [hospital pessoa]
  (let [fila (get hospital :espera)]
    (alter fila chega-em pessoa)))

; DOSYNC
; runs expressions in a transaction
(defn simula-um-dia []
  (let [hospital {:espera       (ref h.model/fila-vazia)
                  :laboratorio1 (ref h.model/fila-vazia)
                  :laboratorio2 (ref h.model/fila-vazia)
                  :laboratorio3 (ref h.model/fila-vazia)}]
    (dosync
     (chega-em! hospital "guilherme")
     (pprint hospital))))

(simula-um-dia)

; FUTURE
(defn async-chega-em! [hospital pessoa]
  (future
    (Thread/sleep (rand 5000))
    (dosync 
     (println "Tentando o codigo sincronizado" pessoa)
     (chega-em! [hospital pessoa]))))

(defn simula-um-dia-async
  []
  (let [hospital {:espera       (ref h.model/fila-vazia)
                  :laboratorio1 (ref h.model/fila-vazia)
                  :laboratorio2 (ref h.model/fila-vazia)
                  :laboratorio3 (ref h.model/fila-vazia)}]
    (def futures (mapv #(async-chega-em! hospital %) (range 10)))
    (future
      (dotimes [n 4]
        (Thread/sleep 2000)
        (pprint hospital)
        (pprint futures)))))

(simula-um-dia-async)