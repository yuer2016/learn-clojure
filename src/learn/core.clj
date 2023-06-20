(ns learn.core
  (:gen-class))

(defn foo
  "I don't do a whole lot."
  [name]
  (println (format "%s hello world!" name)))

(defn cube
  "calc cube"
  [x]
  (* x x x))

(defn sum-integers
  "计算从 a 到 b 的各整数和"
  [a b]
  (if (> a b)
    0
    (+ a (sum-integers (+ a 1) b))))

(defn sum-cube
  "计算从 a 到 b 的立方之和"
  [a b]
  (if (> a b)
    0
    (+ (cube a) (sum-cube (+ a 1) b))))

;; sum-cube(1 5)
;; 1  2 * 2 * 2 (sum-cube 3 5)
;; 1  8  3 * 3 * 3 (sum-cube 4 5)
;; 1  8  27 4 * 4 * 4  (sum-cube 5 5)
;; 1  8  27 64 125 (sum-cube 6 5)
;; 9 27 64 125
;; 36 64 125
;; 100 125
;; 225

;; 模拟求和公式
(defn sum
  [term a next b]
  (if (> a b)
    0
    (+ (term a)
       (sum term (next a) next b))))

;; 自增函数
(defn increment
  [n]
  (+ n 1))

;; 计算 a 到 b 的立方和
(defn sum-cubes
  [a b]
  (sum cube a increment b))

;; 声明恒等式
(defn identity-x [x] x)

;;抽象版本
(defn sum-integers-a
  [a b]
  (sum identity-x a increment b))

;;计算模拟圆周率
(defn pi-sum
  [a b]
  (let [pi-term (fn [x] (/ 1.0 (* x (+ x 2))))
        pi-next (fn [y] (+ y 4))]
    (sum pi-term a pi-next b)))

;; 函数 f 在范围 a 和 b 之间的定积分的近似值
(defn integral
  [f a b dx]
  (let [add-dx (fn [x] (+ x dx))]
    (* (sum f (+ a (/ dx 2.0)) add-dx b) dx)))

;; clojure 闭包示例
;; 闭包通常用于保留函数的状态和记忆效应，而柯里化则用于对函数进行组合和封装。
(defn make-add-closure [x]
  (fn [y] (+ x y)))

