(ns common.config
  (:use [propertea.core])
;  (:require [clojure.string :refer [split]]
;            [clojure.java.io :refer [reader]])
;  (:import java.util.Properties)
;  (:import java.io.FileInputStream)
  )

;Reading properties file using Java methods
;(defn load-props [file-name]
;  (let [props Properties]
;    (.load props (new java.io.FileInputStream file-name))))
;
;(defn read [] (load-props "resources/config.properties"))

;Reading properties file using propertea project
(def config-props (read-properties "resources/config.properties"))

(defn get-config-prop [prop-name & [is-number]] 
  (if is-number (read-string (get config-props prop-name))
    (get config-props prop-name)))