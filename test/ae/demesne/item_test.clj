(ns ae.demesne.item-test
  (:require [ae.demesne.item :as item]
            [clojure.test :as t]))

(create-ns 'ae.demesne.event)
(alias 'event 'ae.demesne.event)

(t/deftest append-event-test
  (t/testing "Events can be appended to ::event/changes."
    (let [item {} e1 :foo e2 :bar
          actual (item/append-event item e1)]
      (t/is (= {::event/changes [e1]} actual))
      (t/is (= {::event/changes [e1 e2]} (item/append-event actual e2))))))

(t/deftest apply-event-test

  (t/testing "apply-event-item-created"
    (t/testing "Items can be created."
      (let [id 3 name "foo"
            event {::event/type :ae.demesne.event.type/item-created
                   ::item/id id ::item/name name}
            item {::item/id id ::item/name name ::item/active? true}]
        (t/is (= item (item/apply-event nil event))))))

  (t/testing "apply-event-item-deactivated"
    (t/testing "Items can be deactivated."
      (let [old-item {::item/active? true}
            new-item {::item/active? false}
            event {::event/type :ae.demesne.event.type/item-deactivated}]
        (t/is (= new-item (item/apply-event old-item event))))))

  (t/testing "apply-event-item-checked-in"
    (t/testing "Items can be checked in."
      (let [amount 4
            event {::event/type :ae.demesne.event.type/item-checked-in
                   ::item/amount amount}
            old-item {::item/amount 5}
            new-item {::item/amount 9}]
        (t/is (= new-item (item/apply-event old-item event))))))

  (t/testing "apply-event-item-checked-out"
    (t/testing "Items can be checked out."
      (let [amount 4
            event {::event/type :ae.demesne.event.type/item-checked-out
                   ::item/amount amount}
            old-item {::item/amount 5}
            new-item {::item/amount 1}]
        (t/is (= new-item (item/apply-event old-item event))))))

  (t/testing "apply-event-item-renamed"
    (t/testing "Items can be renamed."
      (let [name "foo"
            event {::event/type :ae.demesne.event.type/item-renamed
                   ::item/name name}
            old-item {::item/name "bar"}
            new-item {::item/name name}]
        (t/is (= new-item (item/apply-event old-item event)))))))

(t/deftest increment-test
  (t/is (= 3 (item/increment nil 3)))
  (t/is (= 5 (item/increment 2 3))))

(t/deftest decrement-test
  (t/is (= -3 (item/decrement nil 3)))
  (t/is (= -1 (item/decrement 2 3))))

(t/deftest load-from-history-test
  (t/testing "Items can be loaded from their event history."
    (let [id 3 name "foo" amount 10
          e1 {::event/type :ae.demesne.event.type/item-created
              ::item/id id ::item/name name}
          e2 {::event/type :ae.demesne.event.type/item-checked-in
              ::item/amount amount}
          expected {::item/id id ::item/name name ::item/active? true
                    ::item/amount amount}]
      (t/is (= expected (item/load-from-history [e1 e2]))))))
