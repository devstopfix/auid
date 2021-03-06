(ns devstopfix.auid-test
  (:import [java.util Date])
  (:require [clojure.test :refer :all]
            [devstopfix.auid :refer :all]
            [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :as ct :refer (defspec)]))


(set! *warn-on-reflection* true)

(defn epoch-date [^Long ms-ago]
  (Date. (+ clojure-epoch ms-ago)))

; https://github.com/clojure/test.check/blob/master/doc/intro.md
(defn ascending?
  "clojure.core/sorted? doesn't do what we might expect"
  [coll]
  (every? (fn [[a b]] (<= a b))
    (partition 2 1 coll)))

(defn take-ids [params]
  "Append a list of ids generated by the given parameters."
  (let [[seed machine-id ms-ago size] params
         epoch   (epoch-date ms-ago)
         next-id (next-id-fn machine-id epoch seed)]
    (conj
      params
      (take size (repeatedly #(next-id))))))

(defn generate-sample []
  "A sample is a Tuple of [seed machine-id epoch-offset size ids]"
  (gen/fmap
    take-ids
    (gen/tuple
      gen/int
      gen/pos-int
      gen/neg-int
      (gen/such-that #(>= % 2) gen/s-pos-int))))

(defspec test-ids-never-duplicate 1e5
  (prop/for-all [s (generate-sample)]
    (let [ids (last s)]
      (= (count ids) (count (set ids))))))

(defspec test-ids-always-increase-with-time 1e4
  (prop/for-all [s (generate-sample)]
    (let [ids (vec (last s))]
      (and
        (ascending? ids)
        (< (first ids) (last ids))))))

(defspec test-ids-are-always-positive 1e4
  (prop/for-all [s (generate-sample)]
    (let [ids (last s)]
      (nil?
        (some neg? ids)))))
