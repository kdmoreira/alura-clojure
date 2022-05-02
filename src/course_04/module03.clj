(ns course-04.module03
  (:require [course-04.module02 :as module3]))

(defn carrega-paciente [id]
  (println "Carregando" id)
  (Thread/sleep 1000)
  {:id id, :carregado-em (module3/agora)})

; pure function
(defn carrega-se-nao-existe
  [cache id carregadora]
  (if (contains? cache id)
    cache
    (let [paciente (carregadora id)]
      (assoc cache id paciente))))

(carrega-se-nao-existe {}, 15, carrega-paciente)
(carrega-se-nao-existe {15 {:id 15}}, 15, carrega-paciente)

; using Java interop
(defprotocol Carregavel
  (carrega! [this id]))

(defrecord Cache
  [cache carregadora]
  Carregavel
  (carrega! [this id]
            (swap! cache carrega-se-nao-existe id carregadora)
            (get @cache id)))

(def pacientes (->Cache (atom {}), carrega-paciente))
pacientes
(carrega! pacientes 15)
(carrega! pacientes 30)
(#break carrega! pacientes 15)
pacientes