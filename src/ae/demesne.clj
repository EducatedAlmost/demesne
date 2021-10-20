(ns ae.demesne
  (:gen-class)
  (:require [ae.demesne.server :as server]))

(defn greet
  "Callable entry point to the application."
  [data]
  (println (str "Hello, " (or (:name data) "World") "!")))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (greet {:name (first args)})
  (server/start))
