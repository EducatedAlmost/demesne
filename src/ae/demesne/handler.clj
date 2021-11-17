(ns ae.demesne.handler
  (:require
   [ae.demesne.item :as item]
   [ae.demesne.repository :as repo]
   [ae.demesne.command :as command]
   [clojure.tools.logging :as log]
   [clojure.pprint :as pp]))

(defn id-exception [id]
  (throw (new Exception (format "Cannot create item with id: %s. Item already exists." id))))

(defn name-exception [name]
  (throw (new Exception (format "Name '%s' is nil or empty." name))))

(defmulti handle
  (fn [_ command] (::command/type command)))

(defmethod handle :default [params]
  (log/warnf "::command.type not recognised: %s" (pp/pprint params))
  ;; Return a failure response?
  nil)

(defmethod handle :ae.demesne.command.type/create
  [item {:keys [::item/id ::item/name]}]
  (if (not (nil? item)) (id-exception id)
      (if (empty? name) (name-exception name)
          (item/create item id name))))

(defmethod handle :ae.demesne.command.type/deactivate
  [item {:keys [::item/id]}]
  (item/deactivate item id))

(defmethod handle :ae.demesne.command.type/reactivate
  [item {:keys [::item/id]}]
  (item/reactivate item id))

(defmethod handle :ae.demesne.command.type/check-in
  [item {:keys [::item/id ::item/amount]}]
  (if (< amount 0)
    (throw (new Exception ":ae.demesne.item/amount must be positive."))
    (item/check-in item id amount)))

(defmethod handle :ae.demesne.command.type/check-out
  [item {:keys [::item/id ::item/amount]}]
  (if (< amount 0)
    (throw (new Exception ":ae.demesne.item/amount must be positive."))
    (item/check-out item id amount)))

(defmethod handle :ae.demesne.command.type/rename
  [item {:keys [::item/id ::item/name]}]
  (if (empty? name) (name-exception name)
      (item/rename item id name)))

(defn handle! [{:keys [::item/id] :as command}]
  (-> id
      repo/get-by-id
      (handle command)
      repo/save!))
