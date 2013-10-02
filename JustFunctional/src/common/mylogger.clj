(ns common.mylogger
  (:use [common.config]
        [clj-time.core]
        [clj-time.format]
        [clj-time.local])
  (:require clojure.java.io))

(def log-types #{"INFO" "ERROR"})

;message > Message that should be printed
;type > INFO, ERROR
(defn print-logger [writer] 
  #(binding [*out* writer] 
     (println (let [type %1
                    message %2] 
                (if (contains? log-types type) 
                  (str (format-local-time (local-now) :mysql) " [" type "] " message)
                  (str (format-local-time (local-now) :mysql) " [" (get-config-prop :def_log_type) "] " message))))))

(defn file-logger [file] 
  #(with-open [f (clojure.java.io/writer file :append true)] 
     ((print-logger f) %1 %2))) 

(def just-log (file-logger (get-config-prop :log_file)))