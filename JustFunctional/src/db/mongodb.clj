(ns db.mongodb
  (:use [monger.conversion :only [from-db-object]]
        [common.config]
        [common.mylogger])
  (:require [monger.core :as mg]
            [monger.collection :as mc])
  (:import [com.mongodb MongoOptions ServerAddress]))

;; localhost, default port
(mg/connect!)
(mg/set-db! (mg/get-db (get-config-prop :db_name)))

;; using MongoOptions allows fine-tuning connection parameters,
;; like automatic reconnection (highly recommended for production environment)
(let [^MongoOptions opts (mg/mongo-options :threads-allowed-to-block-for-connection-multiplier 300)
      ^ServerAddress sa  (mg/server-address 
                           (get-config-prop :db_address) (get-config-prop :db_port true))]
  (mg/connect! sa opts))

(defn save-user [name surname email password] 
  (mc/insert "users"                       
             {:_id email :name name 
              :surname surname :password password}))

(defn log-in [email password] 
  (do (just-log "INFO" (str "ns: mongodb, Login function, user email: " email))
    (mc/find-one-as-map "users" {:_id email :password password})))

