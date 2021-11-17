(ns ae.demesne.item)

(create-ns 'ae.demesne.event)
(alias 'event 'ae.demesne.event)

(defn append-event [item event]
  (update item ::event/changes #(concat % [event])))

(defmulti apply-event (fn [_ event] (::event/type event)))

(defmethod apply-event :ae.demesne.event.type/item-created
  [item {:keys [::id ::name]}]
  (assoc item ::id id ::name name ::active? true))

(defmethod apply-event :ae.demesne.event.type/item-deactivated
  [item _]
  (assoc item ::active? false))

(defmethod apply-event :ae.demesne.event.type/item-reactivated
  [item _]
  (assoc item ::active? true))

(defn increment [x y]
  (+ (or x 0) y))

(defmethod apply-event :ae.demesne.event.type/item-checked-in
  [item {:keys [::amount]}]
  (update item ::amount #(increment % amount)))

(defn decrement [x y]
  (- (or x 0) y))

(defmethod apply-event :ae.demesne.event.type/item-checked-out
  [item {:keys [::amount]}]
  (update item ::amount #(decrement % amount)))

(defmethod apply-event :ae.demesne.event.type/item-renamed
  [item {:keys [::name]}]
  (assoc item ::name name))

(defn raise [item event]
  (-> item
      (append-event event)
      (apply-event event)))

(defn create [item id name]
  (raise item {::event/type :ae.demesne.event.type/item-created
               ::id id ::name name}))

(defn deactivate [item id]
  (raise item {::event/type :ae.demesne.event.type/item-deactivated
               ::id id}))

(defn reactivate [item id]
  (raise item {::event/type :ae.demesne.event.type/item-reactivated
               ::id id}))

(defn check-in [item id amount]
  (raise item {::event/type :ae.demesne.event.type/item-checked-in
               ::id id ::amount amount}))

(defn check-out [item id amount]
  (raise item {::event/type :ae.demesne.event.type/item-checked-out
               ::id id ::amount amount}))

(defn rename [item id name]
  (raise item {::event/type :ae.demesne.event.type/item-renamed
               ::id id ::name name}))

(defn load-from-history [events]
  (reduce apply-event nil events))
