(ns ae.demesne.command
  (:require [clojure.tools.logging :as log]))

(create-ns 'ae.demesne.item)
(alias 'item 'ae.demesne.item)

(defn create [id name]
  {::type :ae.demesne.command.type/create
   ::item/id id
   ::item/name name})

(defn deactivate [id]
  {::type :ae.demesne.command.type/deactivate
   ::item/id id})

(defn reactivate [id]
  {::type :ae.demesne.command.type/reactivate
   ::item/id id})

(defn check-in [id amount]
  {::type :ae.demesne.command.type/check-in
   ::item/id id
   ::item/amount amount})

(defn check-out [id amount]
  {::type :ae.demesne.command.type/check-out
   ::item/id id
   ::item/amount amount})

(defn rename [id name]
  {::type :ae.demesne.command.type/rename
   ::item/id id
   ::item/name name})
