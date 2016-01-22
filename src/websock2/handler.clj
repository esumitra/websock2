(ns websock2.handler
  (:require [compojure.core :refer [defroutes routes wrap-routes]]
            [websock2.layout :refer [error-page]]
            [websock2.routes.home :refer [home-routes]]
            [websock2.routes.websockets :refer [websocket-routes]]
            [websock2.middleware :as middleware]
            [clojure.tools.logging :as log]
            [compojure.route :as route]
            [environ.core :refer [env]]
            [websock2.config :refer [defaults]]
            [mount.core :as mount]))

(defn init
  "init will be called once when
   app is deployed as a servlet on
   an app server such as Tomcat
   put any initialization code here"
  []
  (when-let [config (:log-config env)]
    (org.apache.log4j.PropertyConfigurator/configure config))
  (doseq [component (:started (mount/start))]
    (log/info component "started"))
  ((:init defaults)))

(defn destroy
  "destroy will be called when your application
   shuts down, put any clean up code here"
  []
  (log/info "websock2 is shutting down...")
  (doseq [component (:stopped (mount/stop))]
    (log/info component "stopped"))
  (log/info "shutdown complete!"))

(def app-routes
  (routes
    (wrap-routes #'home-routes middleware/wrap-csrf)
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))

;; Need to redefine app to support websockets
;; (def app (middleware/wrap-base #'app-routes))

(def app
  (-> (routes
       websocket-routes
       (wrap-routes home-routes middleware/wrap-csrf))
      middleware/wrap-base))
