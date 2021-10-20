(ns ae.demesne.event-test
  (:require [ae.demesne.event :as sut]
            [clojure.test :as t]
            [ae.demesne.item :as item]))

(t/deftest create-test
  (let [id 3 name "foo"]
    (t/is (= (sut/create id name)
             {::sut/type :ae.demesne.event.type/create
              ::item/id id
              ::item/name name}))))

(t/deftest deactivate-test
  (let [id 3]
    (t/is (= (sut/deactivate id)
             {::sut/type :ae.demesne.event.type/deactivate
              ::item/id id}))))

(t/deftest check-out-test
  (let [id 3 amount 5]
    (t/is (= (sut/check-out id amount)
             {::sut/type :ae.demesne.event.type/check-out
              ::item/id id
              ::item/amount amount}))))

(t/deftest check-in-test
  (let [id 3 amount 5]
    (t/is (= (sut/check-in id amount)
             {::sut/type :ae.demesne.event.type/check-in
              ::item/id id
              ::item/amount amount}))))

(t/deftest rename-test
  (let [id 3 name "foo"]
    (t/is (= (sut/rename id name)
             {::sut/type :ae.demesne.event.type/rename
              ::item/id id
              ::item/name name}))))
