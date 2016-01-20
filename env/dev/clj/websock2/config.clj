(ns websock2.config
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [websock2.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[websock2 started successfully using the development profile]=-"))
   :middleware wrap-dev})
