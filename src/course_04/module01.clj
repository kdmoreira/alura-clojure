(ns course-04.module01)

(def p-map {1 { :id 1 :nome "joao" :nascimento "01/01/1991" }, 
            2 { :id 2 :nome "maria" :nascimento "02/02/1992"}})
(keys p-map)

(defn adiciona-paciente
  [pacientes paciente]
  (let [id (:id paciente)]
    (assoc pacientes id paciente)))

(adiciona-paciente p-map {:id 0, :nome "karina", :nascimento "18/05/1992"})
; in case a patient doesn't have an id
(adiciona-paciente p-map {:nome "carlos"}) ; it associates with a nil key

; manual verification, notice IF-LET
; IF-LET: binds (let) and executes the 1st instruction if "id" exists, else, does smth
(defn adiciona-paciente-manual-verif
  [pacientes paciente]
  (if-let [id (:id paciente)]
    (assoc pacientes id paciente)
    (throw (ex-info "Paciente nao possui id" {:paciente paciente}))))

(adiciona-paciente-manual-verif p-map {:nome "carlos"})

; DEFRECORD allows Java interoperability
(defrecord Paciente [id ^String nome nascimento]) ; ˆis a tip for types, but doesn't enforce it
(->Paciente 15 "guilherme" "18/08/1981") ; ->Example can construct an object
(Paciente. 15 "guilherme" "18/08/1981") ; . after a record calls the constructor of the class
 ; construction treating the object as a map
(map->Paciente {:id 15, :nome "Guilherme", :nascimento "18/09/1981"})

(let [guilherme (->Paciente 15 "guilherme" "18/09/1981")]
  (println (:id guilherme))
  (println (vals guilherme))
  (println (class guilherme))
  (println (record? guilherme))
  (println (.nome guilherme)))

; however, creating Paciente as a map will allow more or less elements
(map->Paciente { :nome "Guilherme", :nascimento "18/09/1981", :rg "1234"})

; but using any of the other constructors (which are positional) will not allow it
; (->Paciente "Guilherme" "18/09/1981") ; ArityException
(->Paciente nil "Guilherme" "18/09/1981") ; explicity passing nil is ok

(assoc (Paciente. nil "Guilherme" "18/09/1981") :id 38)
(class (assoc (Paciente. nil "Guilherme" "18/09/1981") :id 38))