(ns JustFunctional.First)


(def a1 (atom 2))
(def a2 (atom 3))
(swap! a1 inc)
(deref a1) 
(println a2)

