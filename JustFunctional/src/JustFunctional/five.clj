(ns JustFunctional.five)

(def r2 (ref {:balance 400}))

(def r1 (ref {:balance 500}))

(defn debit [account amount] 
  (if (>= (:balance account) amount)
    (assoc account :balance (- (:balance account) amount))
    (throw (java.lang.RuntimeException. "Nema para"))))

(defn credit [account amount]
  (assoc account :balance (+ (:balance account) amount)))


(defn transfer-funds [sender receiver amount]
  (dosync
   (alter sender debit amount)
   (alter receiver credit amount)))

(defn hypot
  [a b]
  (let [a2 (* a a)
        b2 (* b b)]
    (Math/sqrt (+ a2 b2))))

(def v [42 "foo" 99.2 [5 12]])

