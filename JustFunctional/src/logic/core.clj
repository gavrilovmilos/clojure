(ns logic.core
    (:use [hiccup.page :only (html5)]
          [hiccup.form]
          [hiccup.page :only [include-css]]
          [compojure.core :only (defroutes GET POST)]
          [ring.middleware.params :only [wrap-params]]
          [ring.adapter.jetty]
          [clojure.string]
          [db.mongodb as db]
          )
    (:require [compojure.route :as route]
              ; [clostache.route :as route]
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

(defn index-page [] (render-template "index" {} ))

;cloustache login page with index template
(defn login-page [] (render-template "authentication" {:header (read-component "header") :greeting "<h1>Ciao djaci</h1>"}))

(defn register-page[] (render-template "registration" {:greeting "Ciao djaci"}))

(defn user-home [email pass] 
  (do (println email) (str "Welcome: " email pass)))

(defn registration user-attributes 
  (do (println (get user-attributes :name)) 
    (db/save-user [(get user-attributes :name) (get user-attributes :surname) 
                   (get user-attributes :email) (get user-attributes :password)])))


(defroutes routes
  (GET "/" [] "<h2>Hello World</h2>")
  (GET "/index" [] (index-page ))
  (GET "/login" [] (login-page))
  (GET "/register" [] (register-page))
  (POST "/index" [name surname email password] (registration {:name name :surname surname :email email :password password}))
  (POST "/user-home" [email password] (user-home email password))
  (route/resources "/")
  (route/not-found "404 Page Not Found"))


(def app (wrap-params routes))

(defn server [] (run-jetty app {:port 8081}))
