# learn-clojure

形而上者谓之道 形而下者谓之器

任何足够复杂的 C 语言或者 Fortran 程序中,都包含一个临时特设的、不合规范的、充满程序错误的、运行速度很慢的、只有一半功能的 Common-Lisp 实现。

Clojure 语法相关的关键点:

1. 前缀表示法
2. 空格与注释
3. 大小写敏感性

Clojure 代码用 Clojure 数据结构表示。

## doc find-doc 和 apropos 查找文档

```clojure
;; 搜索与任何其他函数或者宏相关的文档。
;; 该宏接受你要了解的实体名称
(doc +)
(doc str)

;; find-doc 函数接受一个字符串参数，这个参数可以是正则表达式 regex 。
;; 该函数按照名称或者相关文档与所提供模式的匹配寻找符合条件的函数或宏文档
(find-doc "lazy")

;; apropos 是一个与文档相关的函数，其工作方式与 find-doc 非常类似，但是只打印匹配搜索模式的函数名称
(apropos 'doc)
```

## 基础类型

Clojure 的 nil 等价于 Java 中的 null 它的含义是 **什么也没有**, Clojure 核心函数在 nil 上操作时会尽量输出合理的结果。

Clojure 字符是 Java 字符(无符号 16 位 UTF-16 代码), 它们用双引号表示 (单引号是一个读取器宏, 正如前面你所看到的,它代表的是完全不同的意义)

```clojure
(.contains "clojure-in-action" "-")

(.endsWith "learn.clj" ".clj")
```

Clojure 中使用的数值都是 64 位整数 (Java 原始长整数) 或者 64 位浮点数 (Java 原始双精度数)。
当需要更大的范围时，可以使用大整数（任意精度整数）或者大十进制数 (任意精度十进制数)。

Clojure 还增加了另一种不太常见的数值类型：比例(ratio)。
比例在两个整数相除时创建，它们不能被进一步简化 。

```clojure
(/ 4 9)
;;-> 4/9

(/ 10.0 3.0)
;;-> 3.3333333333333335
```

## 符号和关键字

符号是 Clojure 程序中的标识符，代表值的名称。
例如，在形式（+ 1 2）中，**+** 是代表加法函数的符号。

因为 Clojure 的 **读取和求值** 是 **分离** 的，所以符号有两个不同的特性：
读取之后的 **程序数据结构形式** 以及它们 **解出的值**。

符号本身只是包含可选命名空间的名称，但当一个表达式求值时，它们被所代表的值代替。

符号是字母数字字符或者如下字符的任意组合：*！_？$%&=<>。

符号不能以数字开头；如果以 -、+ 或者.开始，则第二个字符不能是数字（不会和数值字面量混淆）；

它们的中间（不能放在其他地方）可以使用一个 **/** 分隔命名空间和名称的各个部分。

当你为一个符号加上 **引号** 时，就将这个符号当成 **数据** 而不是 **代码** 来处理。

在实践中，你几乎绝不会为符号加上引号而当成数据使用，因为 Clojure 有一个特殊类型专门用于这种用例：**关键字**。
**关键字** 就像是自动加上引号的符号：关键字从不引用其他值，求值的结果总是它们自身。

可以用 keyword 和 symbol 函数从字符串中构造 **关键字** 和 **符号**，这两个函数的参数是一个名称字符串和可选的命名空间字符串。
可以用 name 和 namespace 函数检查关键字和函数

```clojure
'abcd
;;-> abcd

;;关键字
:abc
;;-> :abc

(keyword "foo")
;;-> :foo

(symbol "foo" "bar")
;;-> foo/bar

(name :foo/bar)
;;-> "bar"

(namespace :foo/bar)
;;-> foo
```

## 列表

列表是 Clojure 中的基本集合数据结构。Clojure 列表是单链表。
列表的特殊性在于，每个 Clojure 代码表达式都是一个列表。
该列表可能包含其他数据结构(如向量)，但是列表是最基本的。

```clojure
;; 用 list 函数可以创建一个列表。
(list 1 2 3 4 5)
;;-> (1 2 3 4 5)

;; list？函数可以测试列表类型，*1 是一个 Clojure 中的特殊变量，它始终引用最近一次 REPL 会话中计算的结果
(list? * 1)
;;-> true

;;用 conj 函数创建一个新列表并在其中添加另一个值
(conj (list 1 2 3 4 5) 6)
;;-> (6 1 2 3 4 5)

(conj (list 1 2 3) 4 5 6)
;;-> (6 5 4 1 2 3)

;; 可以将列表当成一个栈来对待。

;; 用 peek 返回表头，空列表时返回 nil
(peek (list 1 2 3))
;;-> 1

;; 用 pop 返回表尾，空列表时报错
(pop (list 1 2 3))
;;-> (2 3)

;;count 函数得到列表中的元素数量
(count (list 1 2 3))
;;-> 3

;; 当时希望将符号作为数据而非代码。解决方案也相同——加上引号
(def three-numbers '(1 2 3))
```

## 向量

向量和列表相同：向量用方括号表示，以数字作为索引。

向量可以用 vector 函数创建，也可以用方括号表示法创建。

向量用数字方法索引，你可以快速随机访问向量中的元素。

```clojure
(vector 10 11 12 13 14 15)
;;-> [10 11 12 13 14 15]

[10 11 12 13 14 15]
;;-> [10 11 12 13 14 15]

;; 获取向量元素的函数有 get 和 nth
;; 修改向量,最常用的是 assoc，
(def v-list '[10 11 12 13 14 15])

;; 如果没有找到对应的值, get 返回 nil
(get v-list 0)
;;-> 10

;; 如果没有找到对应的值, nth 抛出异常
(nth v-list 0)
;;-> 10

;; 该函数接受的参数是与新值相关的索引以及新值本身
(assoc v-list 0 25)
;;-> [25 11 12 13 14 15]

;;conj 函数也适用于向量。不同的是这一次新元素出现在序列的最后，这是向量中最快速的插入位置
(conj v-list 16)
;;-> [10 11 12 13 14 15 16]

;; peek 和 pop 也适用于向量，它们查看向量的尾部，而不是列表中的表头
(peek v-list)
;;-> 15

(pop v-list)
;;-> [10 11 12 13 14]

;; 向量有一个有趣的属性：它们是取单一参数的函数。
;; 这个参数被假定为一个索引，当用一个数字调用向量时，将在向量中查找与该索引相关的值
(v-list 2)
;;-> 12
```

## 映射

映射类似于 Python、Ruby 和 Perl 等语言中的关联数组或者字典。

一个映射就是一个 键-值 对序列。

键可以是任何类型的对象，用对应的键可以在映射中查到一个值。映射用花括号表示。

```clojure
(def the-map {:a 1 :b 2 :c 3})
;;-> #'user/the-map

;; 映射还可以用 hash-map 函数构建
(hash-map :a 1 :b 2 :c 3)
;;-> {:c 3, :b 2, :a 1}

;; 值的查找方式如下
(:a the-map)
;;-> 1

;; 若未找到关键字,可以指定一个默认值
(:z the-map 26)
;;-> 26

;; 有多个函数可以修改映射，常用的是 assoc 和 dissoc
(assoc the-map :d 4)
;;-> {:a 1, :b 2, :c 3, :d 4}

(dissoc the-map :a)
;;-> {:b 2, :c 3}

(def users {:tom {
  :date-joined "2023-06-20"
  :summary {
    :average {
      :monthly 1000
      :yearly 12000
    }
  }
}})

;; Clojure 提供了三个简化嵌套集合更新的函数
(assoc-in users [:tom :summary :average :monthly] 3000)
;;-> {:tom {:date-joined "2023-06-20", :summary {:average {:monthly 3000, :yearly 12000}}}}

(get-in users [:tom :summary :average :monthly])
;;-> 1000

(update-in users [:tom :summary :average :monthly] + 500)
;;-> {:tom {:date-joined "2023-06-20", :summary {:average {:monthly 1500, :yearly 12000}}}}
```

 映射字面量和 hash-map 函数不完全等价，因为 Clojure 实际上有两种不同的映射实现：哈希映射（hash-map）和数组映射（array-map）。

 数组映射以有序方式保存键和值，以扫描方式执行查找，而不采用哈希方式。

 这对于小的映射更快，所以较小的映射字面量（10个键或者更少）实际上将成为一个数组映射而不是哈希映射。

 如果用 assoc 函数将太多键关联到一个数组映射，那么最终将会得到一个哈希映射（但是，反过来却不成立：哈希映射变得太小时不会返回一个数组映射）。

 透明地替换数据结构的实现是 Clojure 提高性能的常用技巧，这是通过使用不可变数据结构和纯函数实现的。

 不管调用 hash-map 和 array-map 函数时使用多少个参数，它们总是返回对应的结构

## 序列

**序列** 不是一种集合类型，而是一个接口（称为ISeq），这个接口输出 “在一件事之后发生更多的事” 的抽象。
Clojure 数据结构、函数和宏普遍实现这个接口。

ISeq接口提供三个函数：first、rest 和 cons

```clojure
(first (list 1 2 3))
;;-> 1

(rest (list 1 2 3))
;;->(2 3)

(first [1 2 3])
;;-> 1

(rest [1 2 3])
;;-> (2 3)

(first {:a 1 :b 2 :c 3})
;;-> {:a 1}

(rest {:a 1 :b 2 :c 3})
;;-> ([:b 2] [:c 3])

;; cons（construct的简写）用指定的元素和现有序列创建一个新序列
(cons 4 (list 1 2 3))
;;-> (4 1 2 3)

(cons 4 [1 2 3])
;;-> (4 1 2 3)

(cons {:a 1} {:b 2 :c 3})
;;-> ({:a 1} [:b 2] [:c 3])
```

序列抽象通常是 **惰性** 的，也就是说，尽管 first、rest 和 cons 的结果打印出来像一个列表（两边有圆括号），但并没有进行创建列表的额外工作。

```clojure
(list? (cons 1 (list 2 3)))
;;-> false
```

## 函数

Clojure 是一种函数式语言，这意味着函数是该语言的“头等公民”。

对第一类函数，语言允许它们

1. 动态创建
2. 作为参数传递给函数
3. 从其他函数中返回
4. 作为值保存在其他数据结构中

### 函数定义

Clojure 提供了方便的 defn 宏，可以实现传统形式的函数定义

```clojure
(defn addition [a b]
  (+ a b))

;; defn 宏可展开为 def 和 fn 调用的组合，其中 fn 本身是另一个宏，def 是一个特殊形式。
;; def 以指定的名称创建一个变量，并将其绑定到一个新的函数对象。
;; fn 宏接受方括号中的一系列参数，然后是函数主体; fn 形式可以直接用于定义匿名函数。

;; defn 宏等价形式
(def addition 
 (fn [a b] (+ a b)))

;; 函数可变参数列表
;; 定义包含两个及以上参数的函数 [x y & more]
(defn arity2+ [first second & more]
   (vector first second more))

;; let 形式可以在代码中将一个符号和某个值绑定，从而引入局部命名对象
;; let 形式接受一个向量作为其第一个参数，该向量包含偶数个形式，然后是在let求值时进行求值的0个或者多个形式。let 返回的是最后一个表达式的值
(defn let-s []
  (let [x 1 
        y 2 
        z (+ x y)]
    z))

;; 下划线标识符,下划线标识符可用于任何你不关心某个对象值的情况。
(defn let-s-ignore []
  (let [x 1
        y 2
        _ (println "total-size:" (+ x y))
        z (+ x y)]
    z))

;; do 形式是将多个 s- 表达式组合为一个表达式的方便手段。
;; 这是宏中常见的做法，许多 Clojure 核心形式都是以多个形式为参数并用隐含的 do 将它们合而为一的宏。
;; fn、let、doseq、loop、try、when、binding、dosync 和 locking 就是这方面的例子
(defn do-things [x]
  (do
    (println "do-first-things")
    (println "do-second-things")
    x))

```

### 读取器宏

Clojure **读取器** 将程序文本转换为 Clojure 数据结构。

这是通过识别圆括号、花括号等组成列表、哈希映射和向量开头（及结尾）的特殊字符完成的; 识别规则内建于读取器中。

其他字符也是特殊的，因为它们通知读取器之后的形式应该以特殊的方式处理。
从某种意义上讲，这些字符扩展了读取器的能力，它们被称为 **读取器宏**。

读取器宏最简单 (也是最传统) 的例子是注释符号 (;)。
当读取器遇到一个分号时，它将该行代码的其余部分视为注释并将其忽略。

### 程序流程

条件的最简单例子是 if 形式:

```clojure
;;  Clojure 中 if 的一般形式如下
(if (= 1 1) 
  "yes"
  "no")
;;-> yes

;; if-not 宏所做的和 if 特殊形式相反
(if-not (> 5 2) 
  "yes"
  "no")
;;-> no

;; cond 可以将嵌套的 if 条件树扁平化。一般形式如下
(defn condition-s [x]
  (cond
    (> x 0) "greater!"
    (= x 0) "zero!"
    :default "lesser!"))

;; when 宏的一般形式如下：
(when (> 5 2)
  (println "five")
  (println "is")
  (println "greater")
  "done")

;; when-not 是 when 的对立面，如果测试条件返回 false 或者 nil
(when-not (< 5 2)
  (println "two")
  (println "is")
  (println "smaller!")
  "done")  
```

### 逻辑函数

clojure 只有 nil 和 false 在逻辑上是假值，剩下的都是真值

and 和 or 也都是宏。
这意味着，它们不是内建于 Clojure 语言中的，但却是核心库的组成部分。
这还意味着，你可以编写与 and 或者 or 表现相同的宏，两者从语言上无法区别。

```clojure
;; and 接受 0 个或者多个形式，按顺序求值每个形式，如果任何一个返回 nil 或者 false，则返回该值。
;; 如果所有形式都不返回 false 或者 nil，则 and 返回最后一个形式的值
(and)
;;-> true

;; or 以相反的方式工作。
;; 它也接受 0 个或者多个形式并逐一求值。
;; 如果任何形式返回逻辑真值，则将其作为 or 的值返回。
;; 如果所有形式都不返回逻辑真值，则 or 返回最后一个值。
(or)
;; -> nil

;; not 函数
(not true)
;;-> false
(not false)
;;-> true

;; = 与 ==
;; 如果你知道所要对比的所有数据都是数值，且预期有不同类别的数字，则使用 ==，否则使用=
(== (/ 1 2) 0.5)
;;-> true

(= (/ 1 2) 0.5)
;;-> false
```

### 函数式循环

Clojure 的 while 宏的工作方式与 Ruby 和 Java 等命令式语言类似

```clojure
;; while 示例
(defn while-s []
   (def x (atom 1))
   (while (< @x 5)
      (do
         (println @x)
         (swap! x inc))))
```

Clojure 没有传统的 for 循环；
Clojure 版本的循环流程控制是 loop 和与之关联的 recur。

```clojure
;; recur 是 Clojure 中的一个特殊形式，虽然看上去像递归，但它不使用栈。
(defn fact-loop [n]
  "计算阶乘"
  (loop [current n fact 1] ;; current 与值 n 绑定，fact 与值 1 绑定
    (if (== current 1)
      fact
      (recur (dec current) (* fact current))))) ;; recur 有两个绑定值（dec current）和（*fact current），它们在计算之后重新与 current 和 fact 绑定
```

doseq 是一个有趣的形式。
最简单的形式接受一个包含两个项的向量，第一个项是一个新符号，以后将绑定到第二个项（必须是一个序列）中的每个元素。
形式的主体将对序列中的每个元素执行，然后整个形式将返回 nil

```clojure
(doseq [number [1 10 20 30 40]]
  (println "number is:" number))

;; dotimes 是一个方便的宏，接受一个向量（包含一个符号和一个数值n），然后是宏的主体。向量中的符号被设置为 0 到（n-1）的数值，并对每个数值求取主体的值
(dotimes [number 10]
  (println "number is:" number))  

;; map 的最简单用法是接受一个一元函数和一个数据元素序列。
;; 一元函数是只有一个参数的函数。
;; map 将这个函数应用到序列的每个元素，并返回一个新序列，该序列包含所有返回值
(map inc [0 1 2 3 4])
;;-> 1 2 3 4 5

;; map 接受一个函数，其可以取得任意数量的参数以及相同数量的序列。
;; 它将该函数应用到每个序列的对应元素，并收集其结果。
;; 如果序列的长度不等，则map应用到最短的一个
(map + [0 1 2 3 4] [1 2 3])
;;-> (1 3 5)

;; filter 函数
(defn non-zero-expenses [expenses]
  (let [non-zero? (fn [e] (not (zero? e)))]
  (filter non-zero? expenses)))

(non-zero-expenses [-10 -9 -8 0 2 3 4 5])
;;-> [0 2 3 4 5]

;; remove
(defn remove-expenses [expenses]
  (remove zero? expenses))

(remove-expenses [-10 -9 -8 0 2 3 4 5])
;;-> (0 2 3 4 5)

;; reduce 的最简形式是一个高阶函数，它接受一个函数（有两个参数）和一个数据元素序列。
;; 函数参数应用到序列的前两个元素，产生第一个结果。
;; 然后，用这个结果和序列的下一个元素再次调用同一个函数。
;; 对以后的元素重复上述过程，直到处理完最后一个元素。
(defn factorial [n]
  (let [numbers (range 1 (+ n 1))]
    (reduce * numbers)))

(factorial 10)
;;-> 3628800

(defn factorial-reductions [n]
  "计算阶乘"
  (let [numbers (range 1 (+ n 1))]
    (reductions * numbers)))


;; 判断给定的整数 x 是否为素数
(defn prime? [x]
  "判断给定的整数 x 是否为素数"
  (let [divisors (range 2 (inc (int (Math/sqrt x))))  ;; 创建一个整数列表，包含从 2 到 sqrt(x) 的所有除数
        remainders (map (fn [d] (rem x d)) divisors)] 
    (not (if (some zero? remainders) true false))))   ;; 如果 remainders 中不存在任何一个为 0 的元素，则说明 x 是素数，否则不是素数

(prime? 5)
;;-> true
(prime? 6)
;;-> false

;; 返回小于给定整数 n 的所有素数
(defn primes-less-than [n]
  "返回小于给定整数 n 的所有素数"
  (for [x (range 2 (inc n)) ;; 对从 2 到 n（包含 n）的所有整数进行迭代，选择其中的素数并返回结果
        :when (prime? x)]
    x))

(primes-less-than 10)
```

### 串行宏

```clojure

(defn final-amount [principle rate time-periods]
  (* (Math/pow (+ 1 (/ rate 100)) time-periods) principle))

(final-amount 100 20 1)

(final-amount 100 20 2)

;; thread-first 宏所做的是取得第一个参数，将其放在下一个表达式中的第二个位置上。
;; 它被称为 thread-first，是因为它将代码移到下一形式首个参数的位置。
;; 然后，它取得整个结果表达式，并将其移到再下一个表达式的第二个位置，重复上述过程，直到所有表达式都处理完毕。
;; (* (Math/pow (+ 1 (/ rate 100)) time-periods) principle))
(defn final-amount2 [principle rate time-periods]
  (-> rate
      (/ 100)
      (+ 1)
      (Math/pow time-periods)
      (* principle)))

(final-amount2 100 20 1)
(final-amount2 100 20 2)

;; thread-last 宏（名称为->>）是 thread-first 宏的近亲。
;; 它取得第一个表达式后并没有将其移入下一个表达式的第二个位置，而是将其移到最后的位置。然后，对向其提供的所有表达式重复该过程
;; (reduce * (range 1 (+1 n)))
(defn factorial2 [n]
  (->> n
       (+ 1)
       (range 1)
       (reduce *)))

;; thread-as 宏
;; as->更灵活：它允许提供一个名称，把各个连续形式的结果绑定到那个名称，以便在下一步中使用
(as-> {"a" [1 2 3 4]} <>
      (<> "a")
      (conj <> 10)
      (map inc <>))       
```
### 元数据

元数据是 "关于数据的数据"。
Clojure 支持用其他数据标记数据 (例如映射、列表和向量)，而不改变被标记数据的值。

```clojure
(def untrusted (with-meta {:command "delete-table" :subject "users"}
                          {:safe false :io true}))

(meta untrusted)
;;=> {:safe false, :io true}

(def untrusted ^{:safe false :io true} 
                {:command "delete-table" :subject "users"})
```
### clojure 多重方法多态

多态（polymorphism）是将多种类型当成相同类型使用的一种能力，可以编写相同代码来操作许多不同类型。
有多种方法能够实现多态，其中三种是许多语言共有的：参数化、随意和子类多态。

```clojure
(defn ad-hoc-type-number [thing]
             (condp = (type thing)
               java.lang.String "stirng"
               clojure.lang.PersistentVector "vector"))

(ad-hoc-type-number "hello clojure")
;;-> string
(ad-hoc-type-number [])
;;-> vector
```

子类多态

子类多态是面向对象语言中占据统治地位的多态类型，在这些语言里它表现为类或者接口层次结构。
Clojure 可以通过其 Java 互操作性使用 Java 类和接口，Clojure 的内建类型也参与 Java 接口和类层次结构。

