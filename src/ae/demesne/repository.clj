(ns ae.demesne.repository
  (:require [ae.demesne.eventstore :as evst]
            [ae.demesne.item :as item]))

(create-ns 'ae.demense.event)
(alias 'event 'ae.demense.event)

(defn get-by-id [id]
  (item/load-from-history (evst/get-events id)))

(defn save! [{:keys [::item/id ::event/changes] :as item}]
  (evst/save-events! id changes -1)
  ;; What should be returned?
  item)
