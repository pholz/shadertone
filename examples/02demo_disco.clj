;; ======================================================================
;; this code is meant to be stepped through interactively, not
;; executed at once.  It demonstrates how we can interact with
;; Overtone.
(ns demo2
  (:use [overtone.live]
        [overtone.synth.stringed])
  (:require [shadertone.tone :as t]
            [leipzig.live    :as ll]
            [leipzig.melody  :as lm]
            )
  (:import [jsyphon JSyphonClient JSyphonServer]))

(t/start "examples/vdtest0.glsl"
         :width 800 :height 600
         :textures [:overtone-audio])

(System/getProperty "java.library.path")

(t/stop) ; at some point...

;; now go find some Overtone demos like examples/compositions/funk.clj

;; or try out your own drumkit via leipzig
(def snare (sample (freesound-path 26903)))
(def kick (sample (freesound-path 2086)))
(def close-hihat (sample (freesound-path 802)))
(def open-hihat (sample (freesound-path 26657)))
(def clap (sample (freesound-path 48310)))
(def gshake (sample (freesound-path 113625)))

(def gtr (guitar))

(guitar-strum gtr :E :down 1.25)

(defsynth bx0 [freq 440 amp 0.1]
  (out 0 (* amp (saw freq))))

(bx0)

(kill bx0)

(demo (g-verb (* 0.3 (sin-osc:kr 10 0) (saw 200)) 200 8))

(demo 60 (g-verb (sum
                  (map #(blip (* (midicps (duty:kr % 0 (dseq [24 27 31 36 41] INF)))
                                 %2)
                              (mul-add:kr (lf-noise1:kr 1/2) 3 4))
                       [1 1/2 1/4]
                       [1 4 8]))
                 200 8))



(defn trf [x] (* 3 x))

(mod 6 5)

(defn offs [] 44)

(definst wbl [oct 12] (let [
                             y (sum (map #(* 0.1
                                              (sin-osc
                                               (midicps
                                                (duty:kr % 0
                                                         (drand (map (partial + oct) [24 27 31]) INF)))
                                                     ))
                                    [1/2 1/4 1]))
                       x y]
                         (out 0 (pan2 (g-verb x 1 1/2)))))

(wbl 48)

(ctl wbl :oct 48)

(stop)

(snare)
(kick)

(defmethod ll/play-note :default [{p :pitch}]
  (case p
      0 nil
      1 (snare)
      2 (kick)
      3 (close-hihat)
      4 (open-hihat)
      5 (clap)
      6 (gshake)))

(def beats0     ;; 1   2   3   4   5   6   7   8
  (->> (lm/phrase [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]
                  [2 0 1 0 2 0 1 0 2 0 1 0 2 0 1 0]) ;; <- adjust
       (lm/with (lm/phrase
                  [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]
                  [0 0 5 0 0 0 5 0 0 0 5 0 0 0 5 0]))
       (lm/with (lm/phrase
                  [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]
                  [3 0 3 0 3 0 3 0 3 0 3 0 3 0 3 0]))
       (lm/with (lm/phrase
                  [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]
                  [6 6 0 6 6 6 0 6 6 6 0 6 6 6 0 6]))
       (lm/where :time (lm/bpm (* 2 124)))
       (lm/where :duration (lm/bpm (* 2 124)))))

(ll/jam (var beats0))

(def beats0     ;; 1   2   3   4   5   6   7   8
  (->> (lm/phrase [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]
                  [2 0 1 1 2 0 1 0 2 0 1 1 2 0 1 0])
       (lm/with (lm/phrase
                  [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]
                  [0 5 5 0 0 5 5 0 0 5 5 0 0 5 5 0]))
       (lm/with (lm/phrase
                  [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]
                  [3 0 0 3 3 0 3 0 3 0 3 0 3 0 3 0]))
       (lm/with (lm/phrase
                  [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]
                  [6 6 0 6 0 6 0 6 0 6 0 6 0 6 0 6]))
       (lm/where :time (lm/bpm (* 2 124)))
       (lm/where :duration (lm/bpm (* 2 124)))))

(def beats0 nil) ;; to turn off the melody

(stop)

(t/stop) ; at some point...
