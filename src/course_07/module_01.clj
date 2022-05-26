(ns course-07.module-01
  (:use clojure.pprint)
  (:require [course-06.module04-06 :refer :all]
            [clojure.test.check.generators :as gen]
            [schema.core :as s]))

; generate sample, object to generate, number of samples
(gen/sample gen/boolean 3)
(gen/sample gen/small-integer 100)
(gen/sample gen/string)
(gen/sample gen/string-alphanumeric 100)

; generates 100 vectors of dynamic size
(gen/sample (gen/vector gen/small-integer) 100)

; generates 100 vectors of sizes between 1 to 5
(gen/sample (gen/vector gen/small-integer 1 5) 100)

; generates 5 vectors of size 15
(gen/sample (gen/vector gen/small-integer 15) 5)