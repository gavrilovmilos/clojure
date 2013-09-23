(ns logic.just-core
    (:use [hiccup.page :only (html5)]
          [hiccup.form]
          [hiccup.page :only [include-css]]
          [compojure.core :only (defroutes GET POST)]
          [ring.middleware.params :only [wrap-params]]
          [ring.adapter.jetty]
          [clojure.string]
          [db.mongodb :as db])
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
  (do (println log-message) (render-template "error" {:error-message page-message})))

(defn index-page [] (render-template "index" {:header (read-component "header") } ))

;cloustache login page with index template
(defn login-page [] (render-template "authentication" {:header (read-component "header") :greeting "Ciao djaci"}))

(defn register-page[] (render-template "registration" {:greeting "Ciao djaci"}))

(defn logging [email pass] 
  (do (println email) 
    (let [logged-user (db/log-in email pass)]
      (do (println (str "Logged user: " (:_id logged-user))) 
        (render-template "home" {:name (:_id logged-user)}) ))))

(defn registration [name surname email password] 
  (try (do (println name) 
           (db/save-user name surname email password) (render-template "index" {:message "Registration success! Welcome" :name name}))
    (catch Exception e (just-error-handler (.getMessage e) "An error ocured while registering user!") )))


(defroutes routes
  (GET "/" [] "<h2>Hello World</h2>")
  (GET "/index" [] (index-page ))
  (GET "/log-in" [] (login-page))
  (GET "/register" [] (register-page))
  (POST "/index" [name surname email password] (registration name surname email password))
  (POST "/user-home" [email password] (logging email password))
  (route/resources "/")
  (route/not-found "404 Page Not Found"))


(def app (wrap-params routes))

(defn server [] (run-jetty app {:port 8081}))