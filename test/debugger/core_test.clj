(ns debugger.core-test
  (:require [clojure.test :refer :all]
            [debugger.core :refer :all]))

(def ^:constant c++-file          "test/resources/test-c++.cpp")
(def ^:constant c++-txt-file      "test/resources/test-c++-txt.txt")
(def ^:constant java-file         "test/resources/test-java.java")
(def ^:constant java-txt-file     "test/resources/test-java-txt.txt")
(def ^:constant c++-loop-file     "test/resources/test-for-loop-c++.cpp")
(def ^:constant java-loop-file    "test/resources/test-while-loop-java.java")
(def ^:constant c++-complex-file  "test/resources/test-complex-loops-c++.cpp")

(def ^:constant test-program-c++         (process-file c++-file))
(def ^:constant test-program-c++-txt     (process-file c++-txt-file))
(def ^:constant test-program-java        (process-file java-file))
(def ^:constant test-program-java-txt    (process-file java-txt-file))
(def ^:constant test-loop-program-c++    (process-file c++-loop-file))
(def ^:constant test-loop-program-java   (process-file java-loop-file))
(def ^:constant test-complex-program-c++ (process-file c++-complex-file))

(def ^:constant test-meta-c++      ["symptom" c++-file      test-program-c++])
(def ^:constant test-meta-c++-txt  ["symptom" c++-txt-file  test-program-c++-txt])
(def ^:constant test-meta-java     ["symptom" java-file     test-program-java])
(def ^:constant test-meta-java-txt ["symptom" java-txt-file test-program-java-txt])

(deftest search-infinite-loop-test
  (testing "Testing search-infinite-loop function..."
    (is (= (lazy-seq [["for"   7]])                                   (search test-loop-program-c++    "C++"   :infinite-loop)))
    (is (= (lazy-seq [["while" 4]])                                   (search test-loop-program-java   "Java"  :infinite-loop)))
    (is (= (lazy-seq [["while" 7] ["for" 8] ["while" 10] ["for" 12]]) (search test-complex-program-c++ "C++"   :infinite-loop)))))


(deftest set-metadata-test
  (testing "Testing set-metadata function..."
    (is (= {:language "C++"}   (meta (set-metadata test-meta-c++))))
    (is (= {:language "C++"}   (meta (set-metadata test-meta-c++-txt))))
    (is (= {:language "Java"}  (meta (set-metadata test-meta-java))))
    (is (= {:language "Java"}  (meta (set-metadata test-meta-java-txt))))))

(deftest extension-test
  (testing "Testing extension function..."
    (is (= ".cpp"  (extension "testC++.cpp")))
    (is (= ".java" (extension "testJava.java")))
    (is (= ".txt"  (extension "testTXT.txt")))
    (is (= ".pdf"  (extension "testPDF.pdf")))))

(deftest programming-language?-test
  (testing "Testing programming-language? function..."
    (is (= "C++"   (programming-language? test-program-c++)))
    (is (= "C++"   (programming-language? test-program-c++-txt)))
    (is (= "Java"  (programming-language? test-program-java)))
    (is (= "Java"  (programming-language? test-program-java-txt)))))