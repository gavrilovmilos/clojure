(ns db.mongodb
  (:require [monger.core :as mg]
            [monger.collection :as mc])
  (:import [com.mongodb MongoOptions ServerAddress]))

;; localhost, default port
(mg/connect!)
(mg/set-db! (mg/get-db "monger-test"))

;; given host, given port
;(mg/connect! { :host "db.megacorp.internal" :port 7878 })

;; using MongoOptions allows fine-tuning connection parameters,
;; like automatic reconnection (highly recommended for production environment)
(let [^MongoOptions opts (mg/mongo-options :threads-allowed-to-block-for-connection-multiplier 300)
      ^ServerAddress sa  (mg/server-address "127.0.0.1" 27017)]
  (mg/connect! sa opts))

;(mc/insert "documents" {:first_name "John"  :last_name "Lennon"})
(defn save-user [name surname email password] 
  (mc/insert "users" 
             {:name name :surname surname :email email :password password}))