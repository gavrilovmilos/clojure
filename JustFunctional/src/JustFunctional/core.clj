(ns JustFunctional.core
    (:use [hiccup.page :only (html5)]
          [compojure.core :only (defroutes GET POST)]
          [hiccup.form]
          [ring.adapter.jetty]
          [clojure.string]
          [hiccup.page :only [include-css]]))

;(defn page-def-head [title] ([:head
;									      [:title title] (include-css "/css/layout.css")]))

(defn index [par1] (html5
									    [:head
									      [:title "JustFunctional"] (include-css "/css/layout.css")]
									    [:body
									      [:div {:id "content"} (str "Index page" par1)]
               [:div {:id "content2"} 
                [:form {:action "/home" :method "post"}
	                (label "email  2" "E-mail: ")
	                (text-field "email" "email")
	                (label "password" "Password: ")
	                (password-field "password" "password")
	                (submit-button {:id "loginButton"} "Log in")]]]))

(defn first-page [email pass] (str "Welcome: " email pass))
(defn log-in [params] (str "Home page" params))

(defroutes routes
  (GET "/" [] "<h2>Hello World</h2>")
  (GET "/index" [] (index "parametar"))
  (GET "/first" [email pass] (first-page email pass))
  (POST "/home" params (log-in params)))


(defn server [] (run-jetty #'routes {:port 8080}))