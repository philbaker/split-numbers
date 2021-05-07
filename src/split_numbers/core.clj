(ns split-numbers.core
  (:require [clojure.math.numeric-tower :as math]))

(defn seperate-numbers 
  "Takes a number and breaks it into a list of seperate of numbers"
  [n]
  (->> n
       math/abs
       (iterate #(quot % 10))
       (take-while pos?)
       (map #(mod % 10))))

(comment
  (def seperate-numbers-quot (take-while pos?  (iterate #(quot % 10) 467)))
  seperate-numbers-quot
  (map #(mod % 10) seperate-numbers-quot) 
  (seperate-numbers 467)
  (seperate-numbers -467)
)

(defn convert-sign 
  "Takes a number (n), seperates it into a list of seperate numbers and converts the sign of each number to the sign of n"
  [n]
  (let [nums (seperate-numbers n)]
    (if (neg? n) 
      (->> nums
           (map #(- %)))
      nums)))

(comment
  (convert-sign 467) 
  (convert-sign -467)
)

(defn split-numbers-v 
  "Takes a number and int coercion function (int, bigint) and returns a vector of component base elements"
  [n coerce-int-f]
  (->> (convert-sign n)
       (map-indexed vector)
       reverse
       (mapv #(coerce-int-f 
               (* 
                (Math/pow 10 (first %)) 
                (second %))))))

(comment
  (def mapped-vec (map-indexed vector '(7 6 4)))
  mapped-vec
  (reverse 
   (mapv #(* 
           (Math/pow 10 (first %)) 
           (second %))
         mapped-vec))
  )

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

(comment
  (split-numbers 467)
  (split-numbers 39)
  (split-numbers 100)
  (split-numbers -321)
  (split-numbers "467")
  (split-numbers "-321")
  (split-numbers 467.913993)
  (split-numbers "ABC")
  (split-numbers 999999999) 
  (split-numbers -999999999)   
  (split-numbers 999999999999999999999999N)  
  (split-numbers -999999999999999999999999N)  
  (split-numbers 99999999999999999999999999N)  
  (split-numbers -99999999999999999999999999N)  

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

(defn -main [& args]
  (println (split-numbers (first args))))
