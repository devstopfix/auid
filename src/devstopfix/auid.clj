(ns devstopfix.auid
  (import [java.util Date Random]
          [java.nio ByteBuffer]))

(set! *warn-on-reflection* true)

; (.getTime (Date. (- 2009 1900) (dec 4) 5))
(def clojure-epoch 1238886000000)

(defn combine-counter-with-machine [c m]
  "Return the least signficant 24 bits of the result
   of shifting the counter left by a nibble and
   adding in the right-most nibble of the machine id."
    (+ (bit-shift-left c 4) (bit-and m 2r00001111)))

(defn zero-most-significant-bit [^bytes ba]
  "Avoid generating negative Longs.
   (this actually reduces us to a 63-but AUID)"
    (aset-byte ba 0 (bit-and (get ba 0) 2r01111111)))

(defn delta-in-ms [^long epoch]
  "Number of milliseconds between current system clock
   and given epoch."
  (- (System/currentTimeMillis) epoch))

(defn mask-and-shift [^long x ^long mask bits-to-shift]
  (bit-shift-left (bit-and x mask) bits-to-shift))

(defn- ^long next-id [^long machine-id counter ^long epoch]
  (let [^long c (swap! counter inc)
        ^long Δt (delta-in-ms epoch)]
    (bit-or
      (mask-and-shift Δt         0x7FFFFFFFFF 24)
      (mask-and-shift machine-id 0x7          20)
      (bit-and        c          0xFFFFF))))

(defn id-fn
  "Create a function that generates IDs from the
   given epoch date (which must be in the past)
   and the given seed. The returned function is
   invoked without any parameters."
  ([^long machine-id ^long seed]
    (id-fn machine-id seed (Date. clojure-epoch)))
  ([^long machine-id ^long seed ^Date epoch]
    (id-fn machine-id seed epoch (Random. seed)))
  ([^long machine-id ^long seed ^Date epoch ^Random rng]
      (partial next-id
        (bit-and machine-id 0x7)
        (atom (.nextLong rng))
        (.getTime epoch))))

