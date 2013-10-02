(ns logic.just-core
    (:use [hiccup.page :only (html5)]
          [hiccup.form]
          [hiccup.page :only [include-css]]
          [compojure.core :only (defroutes GET POST)]
          [ring.middleware.params :only [wrap-params]]
          [ring.adapter.jetty]
          [clojure.string]
          [db.mongodb :as db]
          [common.config]
          [common.mylogger])
    (:require [compojure.route :as route]
              [compojure.handler :as handler]
              [clostache.parser :as clostache]
             ))

;user
(def ^:dynamic active-user {:user-name "" :password ""})

(defn read-component [component-name] 
  (slurp (clojure.java.io/resource 
           (str "components/" component-name ".mustache"))))

;this method reads template file from templates folder
(defn read-template [template-name] 
  (slurp (clojure.java.io/resource 
           (str "templates/" template-name ".mustache"))))

;this function renders template with params and components (header and footer)
;maybe this method should receive two hash maps(header and template)
(defn render-template [template-file params]
  (clostache/render (read-template template-file) params))

(defn just-error-handler [log-message page-message] 
  (do (just-log "ERROR" log-message) (render-template "error" {:error-message page-message})))

(defn index-page [] 
  (render-template "index" {:header (read-component "header") } ))

;cloustache login page with index template
(defn login-page [] 
  (render-template "authentication" {:header (read-component "header") :greeting (get-config-prop :log_in_message)}))

(defn register-page[] 
  (render-template "registration" {:greeting (get-config-prop :register_message)}))

(defn logging [email pass] 
  (do (just-log "INFO" (str "Trying to log in user with email: " email)) 
    (let [logged-user (db/log-in email pass)]
      (do (just-log "INFO" (str "Logged user with email" (:_id logged-user))) 
        (render-template "home" {:name (:surname logged-user)}) ))))

(defn registration [name surname email password] 
  (try (do (just-log "INFO" (str "Trying to register user with e-mail: " email)) 
           (db/save-user name surname email password) 
           (just-log "INFO" (str "Successfully registered user with e-mail: " email))
           (render-template "index" {:message "Registration success! Welcome" :name name}))
    (catch Exception e (just-error-handler (.getMessage e) "An error ocured while trying to registerer user!") )))


(defroutes routes
  (GET "/" [] "<h2>Hello World</h2>")
  (GET "/index" [] (index-page))
  (GET "/log-in" [] (login-page))
  (GET "/register" [] (register-page))
  (POST "/index" [name surname email password] (registration name surname email password))
  (POST "/user-home" [email password] (logging email password))
  (route/resources "/")
  (route/not-found "404 Page Not Found"))


(def app (wrap-params routes))

(defn server [] (run-jetty app {:port (get-config-prop :port "true")}))