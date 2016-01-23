(ns websock2.websockets
  (:require [cognitect.transit :as t]))

(defonce ws-chan (atom nil))
(def json-reader (t/reader :json))
(def json-writer (t/writer :json))

(defn receive-transit-msg!
  "returns a function that takes the incoming transit data, tranforms it 
  and calls the input function with the transformed message"
  [update-fn]
  (fn [msg]
    (update-fn
     (->> msg .-data (t/read json-reader)))))

(defn send-transit-msg!
  "sends input message to websocket; throws error if websocket is not available"
  [msg]
  (if @ws-chan
    (.send @ws-chan (t/write json-writer msg))
    (throw (js/Error. "Websockets not available!"))))

(defn make-websocket!
  [url receive-handler]
  (println "attempting to connect to websocket")
  (if-let [chan (js/WebSocket. url)]
    (do
      (set! (.-onmessage chan) (receive-transit-msg! receive-handler))
      (reset! ws-chan chan)
      (println "Websocket connection established at:" url))
    (throw (js/Error. "Websocket connection failed!"))))
