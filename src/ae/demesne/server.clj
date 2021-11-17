(ns ae.demesne.server
  (:require [ae.demesne.handler :as h]
            [ae.demesne.repository :as repo]
            [ae.demesne.command :as command]
            [ae.demesne.item :as item]
            [clj-uuid :as uuid]
            [clojure.tools.logging :as log]
            [clojure.pprint :as pp]
            [compojure.core :refer [GET defroutes]]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.json :as mid.json]
            [ring.util.response :refer [response]]))

(def ->resp identity)

(defroutes bare-handler
  (GET "/uuid" [] (uuid/v4))

  (GET "/search/:id" [id]
    (-> id repo/get-by-id ->resp response))

  (GET "/create/:id" [id name]
    (-> id (command/create name) h/handle! ->resp response))

  (GET "/deactivate/:id" [id]
    (-> id command/deactivate h/handle! ->resp response))

  (GET "/check-in/:id" [id amount]
    (-> id (command/check-in amount) h/handle! ->resp response))

  (GET "/check-out/:id" [id amount]
    (-> id (command/check-out amount) h/handle! ->resp response))

  (GET "/rename/:id" [id name]
    (-> id (command/rename name) h/handle! ->resp response))
  ;;
  )

(def handler
  (-> bare-handler
      ;; mid.json/wrap-keyword-params
      mid.json/wrap-json-body
      mid.json/wrap-json-params
      mid.json/wrap-json-response))

(defn start []
  (jetty/run-jetty handler {:port 8080}))
