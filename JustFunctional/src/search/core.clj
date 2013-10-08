(ns search.core
  (:use [clojure.string]))


(def search-file "resources/content/text.txt")

(def ^:dynamic lines (split-lines (slurp search-file)))

(defn get-words [line] (split line #"\s+"))

(defn get-index-list [map1 map2]
  (for [x map1
        y map2]
           (if (= (get x 0) (get y 0))
               (hash-map (get x 0) (vector (get x 1) (get y 1)))
               {:d3ef 9}
             )))

(defn convert-seq-to-map [seqv]
  (loop [i 0 map {}]
    (if (< i (count seqv))
      (recur (inc i) 
             (merge map (nth seqv i)))
      map)))

(defn get-same-keys-map [map1 map2] (dissoc (convert-seq-to-map (get-index-list map1 map2)) :d3ef))

;this function merges two hashmaps
;clojure/core merge function overrides properties with the same ID and the value from the last map is the final one.
;just-merge function creates a vector for map's properties with the same key. This map has values from both maps.
(defn just-merge [map1 map2] 
  (merge map1 map2
           (get-same-keys-map map1 map2)))

;this function is indexing given text file
;it uses just-merge function for merging hashmaps
(defn index [file-name] 
  (binding [lines (split-lines (slurp file-name))]
    (loop [i 0 index-map {}] 
      (if (< i (count lines))
        (recur (inc i) 
               (just-merge index-map 
                      (loop [j 0 inner-index-map {}] 
                        (if (< j (count (get-words (nth lines i))))
                          (recur (inc j) 
;                                 (do (println (str index-map "red: " i " -- rec : " j " " (nth (get-words (nth lines i)) j) " count: " (count (get-words (nth lines i))) "\n")) 
                                   (just-merge inner-index-map (hash-map (nth (get-words (nth lines i)) j) i)))
;                          )
                          inner-index-map))))        
        index-map))))
