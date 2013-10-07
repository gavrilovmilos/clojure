(ns search.core
  (:use [clojure.string]))


(def search-file "resources/content/text.txt")

(def ^:dynamic lines (split-lines (slurp search-file)))

(defn get-words [line] (split line #"\s+"))

;(def ^:dynamic row 0)

;isparsiraj tekst na linije teksta
;prolazis kroz svaku liniju uzimas rec i upisujes je u hash mapu
;npr {:street [1 5 6 2]} gde vektor predstavlja linije teksta na kojima se rec nalazi
(defn index [file-name] 
  (binding [lines (split-lines (slurp file-name))]
    (loop [i 0 index-map []] 
      (if (< i (count lines))
        (recur (inc i) 
               (merge index-map 
                      (loop [j 0 inner-index-map []] 
                 (if (< j (count (get-words (nth lines i))))
                   (recur (inc j) 
                          (do (println (str index-map "red: " i " -- rec : " j " " (nth (get-words (nth lines i)) j) " count: " (count (get-words (nth lines i))) "\n")) 
                            (merge inner-index-map {(nth (get-words (nth lines i)) j) i})))
                   inner-index-map))))        
        index-map))))

;(index "resources/content/text.txt")
;definisi metodu za merge-ovanje index mapa, koja ce prvo proveravati ima li indeksa i ukoliko ima vrsiti update a ukoliko nema
;vrsiti merge