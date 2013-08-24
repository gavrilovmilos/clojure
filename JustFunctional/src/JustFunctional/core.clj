(ns JustFunctional.core
    (:use ring.adapter.jetty)
    (:use clojure.string)
    (:use [hiccup.page :only (html5)]
          [compojure.core :only (defroutes GET)])
    (:use hiccup.form))

(defn extract-name [uri]
    (replace-first uri "/" ""))

(defn index [par1] (html5
									    [:head
									      [:title "Hello World"]]
									    [:body
									      [:div {:id "content"} (str "Index page" par1)]
               [:div {:id "content2"} 
                [:form {:action "/first" :method "get"}
	                (label "email" "E-mail: ")
	                (text-field "email" "email")
	                (label "password" "Password: ")
	                (password-field "password" "password")
	                (submit-button {:id "loginButton"} "Log in")]]]))

(defn first-page [email pass] (str "Welcome: " email pass))

(defroutes routes
  (GET "/" [] "<h2>Hello World</h2>")
  (GET "/index" [] (index "parametar"))
  (GET "/first" [email pass] (first-page email pass)))

;I used dispetcher function instead of compojure's function defroutes
;(defn dispetcher [req]
;    {:status 200
;     :headers {"Content-Type" "text/html"}
;     :body    (if (= (:uri req) "/") 
;                  (index "parametar prvi ")
;                  (str "Hello, 2222 " (extract-name (:uri req)) "!"))})

(defn -main [] (run-jetty #'routes {:port 8080}))




;A simple handler to show send some response to the client
;(defn index 
;  [req]
;  (ring.util.response "Welcome page"))

;Routes definition
;(def routes
;  (app
;    [""] index))

;;start function for starting jetty
;(defn start
;  ([] (start 8080))
;  ([port] (run-jetty #'routes {:port port :join? false})))