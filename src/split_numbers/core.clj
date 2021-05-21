(ns split-numbers.core
  (:require [clojure.math.numeric-tower :as math]
            [clojure.string :as str]))

(defn convert-sign 
  "Takes a number and a list of numbers, converts all numbers in the list to the sign of n"
  [n nums]
  (if (neg? n) 
    (map #(- %) nums)
  nums))

(defn split-number-v
  "Takes a number and returns a vector of the number's component base elements"
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

(defmulti split-number 
  "Takes a number or string and returns a vector of component base elements"
  class)

(defmethod split-number java.lang.Long [n]
  (split-number-v n int))

(defmethod split-number clojure.lang.BigInt [n]
  (split-number-v n bigint))

(defmethod split-number java.lang.Double [n]
  (let [split (str/split (str (math/abs n)) #"\.")
        start (first split)
        end (last split)
        decimals (map #(* 
                         (Math/pow 10 (- (inc (first %))))
                         (read-string (second %))) 
                      (map-indexed vector (str/split end #"")))
        result (concat (split-number start) decimals)]
    (vec (convert-sign n result))))

(defmethod split-number java.lang.String [s]
  (if 
    (number? (read-string s)) 
    (split-number (read-string s))
    (throw (Exception. "Argument must be numeric"))))

(defn -main [& args]
  (println (split-number (first args))))

(comment
  ;; split-number-v
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
)

(comment
  ;; split-number (double)
  (def n2 350.21)
  n2
  (def d-split (str/split (str (math/abs n2)) #"\."))
  d-split
  (def d-start (first d-split))
  d-start
  (def d-end (last d-split))
  d-end
  (def d-index (map-indexed vector (str/split d-end #"")))
  d-index
  (def d-decimals 
    (map #(* 
           (Math/pow 10 (- (inc (first %))))
           (read-string (second %))) 
         d-index))
  (Math/pow 10 -1)
  (Math/pow 10 -2)
  d-decimals
  (def d-result (vec (concat (split-number d-start) d-decimals)))
  d-result
)

(comment
  (split-number 467)
  (split-number 39)
  (split-number 100)
  (split-number -321)
  (split-number "467")
  (split-number "-321")
  (split-number 467.913)
  (split-number "150.57")
  (split-number 999999999)
  (split-number -999999999)
  (split-number 999999999999999999999999N)
  (split-number -999999999999999999999999N)
  (split-number 99999999999999999999999999N)
  (split-number -99999999999999999999999999N)
  (split-number "ABC")
)

(comment
  (dotimes [_ 5] 
    (time (split-number 470)))

  (dotimes [_ 5] 
    (time (split-number -335)))

  (dotimes [_ 5] 
    (time (split-number 201.53575)))

  (dotimes [_ 5] 
    (time (split-number "-109")))

  (dotimes [_ 5] 
    (time (split-number "-109")))

  (dotimes [_ 5]
    (time (split-number 915799999)))

  (dotimes [_ 5]
    (time (split-number 989999999999999999999999N)))
)
