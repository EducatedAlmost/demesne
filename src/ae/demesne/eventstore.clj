(ns ae.demesne.eventstore)

(def db (atom {}))

(defn get-events [id] nil)

(defn save-events!
  ([id events] (save-events! id events -1))
  ([id events version] nil))
