(ns ae.demesne.eventstore-test
  (:require [ae.demesne.eventstore :as sut]
            [ae.demesne.item :as item]
            [clojure.test :as t]))

(create-ns 'ae.demesne.event)
(alias 'event 'ae.demesne.event)

(t/deftest versions-match?-test
  (t/testing "Expected version matches present version."
    (t/is (sut/versions-match? 3 3)))
  (t/testing "Expected version doesn't match present version."
    (t/is (not (sut/versions-match? 3 4))))
  (t/testing "-1 allows us to ignore strict versioning."
    (t/is (sut/versions-match? -1 3)))
  (t/testing "The first event expects 0, and there is no present version."
    (t/is (sut/versions-match? 0 nil))))

(t/deftest update-db-test

  (t/testing "Can add the first events for an id."
    (let [db {} id "def" version 0
          e1 {::event/type :foo}
          e2 {::event/type :bar}
          events [e1 e2]]
      (t/is (= {id [{::item/id id ::event/version 1 ::event/data e1}
                    {::item/id id ::event/version 2 ::event/data e2}]}
               (sut/update-db db id events version)))))

  (t/testing "Can add the subsequent events for an id."
    (let [id "def" version 1
          e1 {::event/type :foo}
          e2 {::event/type :bar}
          e3 {::event/type :qux}
          db {id [{::item/id id ::event/version 1 ::event/data e1}]}
          events [e2 e3]]
      (t/is (= {id [{::item/id id ::event/version 1 ::event/data e1}
                    {::item/id id ::event/version 2 ::event/data e2}
                    {::item/id id ::event/version 3 ::event/data e3}]}
               (sut/update-db db id events version)))))

  (t/testing "Update will be rejected if expected version doesn't match present version."
    (let [db {} id "def" version 0
          e1 {::event/type :foo}
          e2 {::event/type :bar}
          events [e1 e2]
          db {id [{::item/id id ::event/version 5 ::event/data e1}]}]
      (t/is (thrown? Exception (sut/update-db db id events version))))))

(t/deftest get-events-test
  (t/testing "Events can be retrieved from the DB."
    (let [id "abc"
          e1 {::event/type :foo}
          e2 {::event/type :bar}
          e1-data {::event/data e1 ::item/id id ::event/version nil}
          e2-data {::event/data e2 ::item/id id ::event/version nil}]
      (swap! sut/db #(assoc % id [e1-data e2-data])) ; Build up
      (t/is (= [e1 e2] (sut/get-events id)))
      (swap! sut/db #(dissoc % id)) ;; Tear down
      )))
