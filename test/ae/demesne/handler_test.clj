(ns ae.demesne.handler-test
  (:require [ae.demesne.handler :as sut]
            [clojure.test :as t]
            [ae.demesne.command :as command]
            [ae.demesne.item :as item]))

(create-ns 'ae.demesne.event)
(alias 'event 'ae.demesne.event)

(t/deftest handle-test

  (t/testing "handle-create"

    (t/testing "If provided a non-nil aggregate it will throw an Exception."
      (t/is (thrown? Exception (sut/handle {} (command/create 3 "foo")))))

    (t/testing "Items are properly created with an id and name."
      (let [id 3 name "foo"]
        (t/is (= (sut/handle nil (command/create id name))
                 {::item/id id ::item/name name ::item/active? true
                  ::event/changes [{::event/type :ae.demesne.event.type/item-created
                                    ::item/id id ::item/name name}]})))))

  (t/testing "handle-deactivate"
    (t/testing "An item can be deactivated."
      (let [id "xyz" old-item {::item/id id ::item/active? true}
            command {::item/id id ::command/type :ae.demesne.command.type/deactivate}
            new-item {::item/id id ::item/active? false
                      ::event/changes [{::event/type :ae.demesne.event.type/item-deactivated
                                        ::item/id id}]}]
        (t/is (= new-item (sut/handle old-item command))))))

  (t/testing "handle-check-in"

    (t/testing "One can check-in items."
      (let [old-item {::item/amount 5}
            command {::command/type :ae.demesne.command.type/check-in ::item/amount 3}
            new-item {::item/amount 8
                      ::event/changes [{::event/type :ae.demesne.event.type/item-checked-in
                                        ::item/id nil ::item/amount 3}]}]
        (t/is (= new-item (sut/handle old-item command)))))

    (t/testing "One cannot check-in a negative quantity."
      (t/is (thrown? Exception (sut/handle {} (command/check-in 3 -5))))))

  (t/testing "handle-check-out"
    (t/testing "One cannot check-out a negative quantity."
      (t/is (thrown? Exception (sut/handle {} (command/check-out 3 -5))))))

  (t/testing "handle-rename"
    (t/testing "Items can be renamed."
      (let [old-item {::item/name "foo"}
            name "bar"
            command {::command/type :ae.demesne.command.type/rename ::item/name name}
            new-item {::item/name name
                      ::event/changes [{::event/type :ae.demesne.event.type/item-renamed
                                        ::item/id nil ::item/name name}]}]
        (t/is (= new-item (sut/handle old-item command)))))))
