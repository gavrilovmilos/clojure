(ns JustFunctional.core
    (:use [hiccup.page :only (html5)]
          [hiccup.form]
          [hiccup.page :only [include-css]]
          [compojure.core :only (defroutes GET POST)]
          
          [ring.adapter.jetty]
          [clojure.string]
          )
    (:require [compojure.route :as route]
              ; [clostache.route :as route]
              [clostache.parser :as clostache]
             ))

;user
(def ^:dynamic active-user {:user-name "" :password ""})


;(defn page-def-head [title] ([:head
;									      [:title title] (include-css "/css/layout.css")]))

;cloustache template
(defn read-template [template-name] 
  (slurp (clojure.java.io/resource 
           (str "templates/" template-name ".mustache"))))

(defn render-template [template-file params]
  (clostache/render (read-template template-file) params))

;cloustache page with index template
(defn clos-page [] (render-template "index" {:greeting "Ciao djaci"}))

;random index page
(defn index [par1] (html5
									    [:head
									      [:title "JustFunctional"] (include-css "public/css/layout.css")]
									    [:body
									      [:div {:id "content"} (str "Index page > " par1)]
               [:div {:id "content2"} 
                [:form {:action "/home" :method "post"}
	                (label "email  2" "E-mail: ")
	                (text-field "email" "email")
	                (label "password" "Password: ")
	                (password-field "password" "password")
	                (submit-button {:id "loginButton"} "Log in")]]]))

(defn first-page [email pass] 
  (do (println email) (str "Welcome: " email pass)))

(defn log-in [params] (str "Home page " params))


(defroutes routes
  (GET "/" [] "<h2>Hello World</h2>")
  (GET "/index" [] (index "parametar"))
  (GET "/first" [email pass] (first-page email pass))
  (GET "/clos" [] (clos-page))
  (POST "/home" params (first-page (get params :email) (get params :password)))
  (route/resources "/")
  (route/not-found "404 Page Not Found"))



(defn server [] (run-jetty #'routes {:port 8081}))