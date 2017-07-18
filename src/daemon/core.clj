(ns daemon.core
  (:require [clojure.java.io :as io]))

(def driver-fifo (io/writer "../daemon.v3.driver/p"))
(def d driver-fifo)


(defn spin [amt]
  (.write d "0\n")
  (.write d (str amt  "\n"))
  (.flush d))

(defn flap [n-per-second times]
  (let [t (/ 1000 n-per-second)]
    (dotimes [x times]
     (.write d  "2\n")
     (.write d "300\n")
     (.write d  "3\n")
     (.write d "-300\n")
     (.flush d)
     (Thread/sleep t)
     (.write d  "2\n")
     (.write d "-300\n")
     (.write d  "3\n")
     (.write d "300\n")
     (.flush d)
     (if (not (= x (- times 1)))
      (Thread/sleep t)))))

(defn wag-tail [times]
  (dotimes [x 10]
   (.write d "1\n")
   (.write d "100\n")
   (.flush d)
   (Thread/sleep 75)
   (.write d "1\n")
   (.write d "-100\n")
   (.flush d)
   (Thread/sleep 75)))))

(defn wake-up []
  (dotimes [x 400]
    (spin 1)
    (Thread/sleep 0.5))
  (flap 9 6)
  (dotimes [x 400]
    (spin -1)
    (Thread/sleep 0.5))
  (wag-tail 3))
