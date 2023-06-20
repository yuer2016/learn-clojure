(ns learn.core-test
  (:require [clojure.test :refer [deftest testing is]]
            [learn.core :refer [foo cube make-add-closure sum-cube sum-cubes integral pi-sum sum-integers sum-integers-a]]))

(deftest foo-test
  "test foo hello world!"
  (foo "clojure"))

(deftest cube-test
  "test cube"
  (testing "FINE"
    (is
     (= 27 (cube 3)))))

;; 测试字符串转换大写
(deftest toStrUpperCase
  "str toUpperString"
  (println (.toUpperCase "abc")))

;; 测试数学库绝对值
(deftest testMathAbs
  "test math abs"
  (testing (is
            (= 3 (Math/abs (int -3))))))

;; 测试 a 到 b 之间的和
(deftest test-sum-integer
  (println (sum-integers 1 5)))

(deftest test-sum-integers-a
  (println (sum-integers-a 1 5)))

;; 测试 a 到 b 之间的立方和
(deftest test-sum-cube
  (println (sum-cube 1 5)))

;; 改进版本的测试 a 到 b 之间的立方和
(deftest test-sum-cubes
  (println (sum-cubes 1 10)))

;; 计算圆周率的近似值
(deftest test-pi-sum
  (println (* 8 (pi-sum 1 1000))))

;; 定积分的近似值
(deftest test-integral
  (println (integral cube 0 1 0.01)))

;; clojure closure 示例
(deftest test-clojure-closure
  (let [add-ten (make-add-closure 10)]
    (println (add-ten 2))))
