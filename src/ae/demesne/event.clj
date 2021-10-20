(ns ae.demesne.event
  (:require [clojure.tools.logging :as log]))
(alias 'item 'ae.demesne.item)

(defn create [id name]
  {::type :ae.demesne.event.type/create
   ::item/id id
   ::item/name name})

(defn deactivate [id]
  {::type :ae.demesne.event.type/deactivate
   ::item/id id})

(defn check-in [id amount]
  {::type :ae.demesne.event.type/check-in
   ::item/id id
   ::item/amount amount})

(defn check-out [id amount]
  {::type :ae.demesne.event.type/check-out
   ::item/id id
   ::item/amount amount})

(defn rename [id name]
  {::type :ae.demesne.event.type/rename
   ::item/id id
   ::item/name name})
