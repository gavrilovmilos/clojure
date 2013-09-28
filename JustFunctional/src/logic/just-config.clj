(ns logic.just-config
  (:use [propertea.core])
  (:require [clojure.string :refer [split]]
            [clojure.java.io :refer [reader]])
  (:import java.util.Properties)
  (:import java.io.FileInputStream))

;1. pokudsj ucitavanja fajla koristeci javine metode
(defn load-props [file-name]
  (let [props Properties]
    (.load props (new java.io.FileInputStream file-name))))

(defn read [] (load-props "config.properties"))

;2. koriscenjem propertea biblioteke
(def props (read-properties "config.properties"))

;3. ucitavanje fajla uz clojure core slurp funkciju
(slurp "file.properties")