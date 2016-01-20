(ns websock2.config
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[websock2 started successfully]=-"))
   :middleware identity})
