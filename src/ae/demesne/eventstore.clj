(ns ae.demesne.eventstore
  (:require [ae.demesne.item :as item]))

(create-ns 'ae.demense.event)
(alias 'event 'ae.demense.event)

(def db (atom {}))

(defn ->descriptors [id events version]
  (map-indexed
   (fn [index event] {::item/id id
                      ::event/data event
                      ::event/version (+ 1 version index)})
   events))

(defn versions-match? [exp-version latest-version]
  (or (= -1 exp-version)              ; We don't care about version
      (= exp-version latest-version)  ; The version is correct
      (and (= exp-version 0)          ; This is the first event
           (nil? latest-version))))

(defn version-exception [version latest-version]
  (as-> "Expected version (%d) does not match DB version (%d)." e
    (format e latest-version version)
    (new Exception e)
    (throw e)))

(defn update-db [db id events version]
  (let [prev-event-datas (get db id)
        latest-version (::event/version (last prev-event-datas))]
    (if (versions-match? version latest-version)
      (update db id concat (->descriptors id events version))
      (version-exception version latest-version))))

(defn save-events!
  ([id events] (save-events! id events -1))
  ([id events version]
   (swap! db #(update-db % id events version))))

(defn get-events [id]
  (map ::event/data (get @db id)))
