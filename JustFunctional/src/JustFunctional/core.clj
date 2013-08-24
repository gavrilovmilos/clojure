(ns JustFunctional.core
    (:use [hiccup.page :only (html5)]
          [compojure.core :only (defroutes GET)]
          [hiccup.form]
          [ring.adapter.jetty]
          [clojure.string]))

(defn extract-name [uri]
    (replace-first uri "/" ""))

(defn index [par1] (html5
									    [:head
									      [:title "Hallo world"]]
									    [:body
									      [:div {:id "content"} (str "Index page" par1)]
               [:div {:id "content2"} 
                [:form {:action "/first" :method "get"}
	                (label "email  2" "E-mail: ")
	                (text-field "email" "email")
	                (label "password" "Password: ")
	                (password-field "password" "password")
	                (submit-button {:id "loginButton"} "Log in")]]]))

(defn first-page [email pass] (str "Welcome: " email pass))

(defroutes routes
  (GET "/" [] "<h2>Hello World</h2>")
  (GET "/index" [] (index "parametar"))
  (GET "/first" [email pass] (first-page email pass)))

(defn redirect
	[id]
		(if-let [url (url-for id)]
			(ring.util.response/redirect url)
			{:status 404 :body (str "No such short URL: " id)}))


(defn server [] (run-jetty #'routes {:port 8080}))



