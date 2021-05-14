(ns split-numbers.core
  (:require [clojure.math.numeric-tower :as math]))

(defn convert-sign 
  "Takes a number and a list of numbers, converts all numbers in the list to the sign of n"
  [n nums]
  (if (neg? n) 
    (map #(- %) nums)
  nums))

(defn split-numbers-v
  "Takes a list of numbers and returns a vector of component base elements"
  [n coerce-int]
  (->> n
       math/abs
       (iterate #(quot % 10))
       (take-while pos?)
       (map #(mod % 10))
       (convert-sign n)
       (map-indexed list)
       (reverse)
       (mapv #(coerce-int (* (Math/pow 10 (first %)) (second %)))))) 

(defmulti split-numbers 
  "Takes a number or string and returns a vector of component base elements"
  class)

(defmethod split-numbers java.lang.Long [n]
  (split-numbers-v n int))

(defmethod split-numbers clojure.lang.BigInt [n]
  (split-numbers-v n bigint))

(defmethod split-numbers java.lang.Double [n]
  (split-numbers-v (int n) int))

(defmethod split-numbers java.lang.String [s]
  (if 
    (number? (read-string s)) 
    (split-numbers (read-string s))
    (throw (Exception. "Argument must be numeric"))))

(defn -main [& args]
  (println (split-numbers (first args))))

(comment
  (def n -467)
  n
  (def abs-n (math/abs n))
  abs-n 
  (def seperate (take-while pos? (iterate #(quot % 10) abs-n)))
  seperate
  (def remainder (map #(mod % 10) seperate))
  remainder
  (def sign (convert-sign n remainder))
  sign 
  (def index-list (reverse (map-indexed list sign)))
  index-list
  (def final-vec 
    (mapv 
      #(int 
         (* 
           (Math/pow 10 (first %)) 
           (second %))) 
      index-list))
  final-vec

  (split-numbers 467)
  (split-numbers 39)
  (split-numbers 100)
  (split-numbers -321)
  (split-numbers "467")
  (split-numbers "-321")
  (split-numbers 467.913993)
  (split-numbers 999999999) 
  (split-numbers -999999999)   
  (split-numbers 999999999999999999999999N)  
  (split-numbers -999999999999999999999999N)  
  (split-numbers 99999999999999999999999999N)  
  (split-numbers -99999999999999999999999999N)  
  (split-numbers "ABC")

  (dotimes [_ 5] 
    (time (split-numbers 470)))

  (dotimes [_ 5] 
    (time (split-numbers -335)))

  (dotimes [_ 5] 
    (time (split-numbers 201.53575)))

  (dotimes [_ 5] 
    (time (split-numbers "-109")))

  (dotimes [_ 5]
    (time (split-numbers 915799999)))

  (dotimes [_ 5]
    (time (split-numbers 989999999999999999999999N)))
)
