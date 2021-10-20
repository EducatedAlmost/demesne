(ns ae.demesne.repository
  (:require [ae.demesne.event :as event]
            [ae.demesne.eventstore :as evst]
            [ae.demesne.item :as item]))

(defn get-by-id [id]
  (item/load-from-history (evst/get-events id)))

(defn save! [{:keys [::item/id ::event/changes] :as item}]
  (evst/save-events! id changes -1)
  ;; item
  ;; What should be returned?
  item)
