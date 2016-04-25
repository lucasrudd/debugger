(ns debugger.core
  (:gen-class)
  
  (:require [debugger-gui.gui-core  :refer :all]
            [clojure.string         :refer :all]
            [clojure.core.async     :as a 
                                    :refer [>! <! >!! <!! go chan buffer close!]]
            [clojure.core.match :refer :all]
            [diagnose.constants :refer :all])
  (:import  (java.io BufferedReader FileReader)))

; :author Lucas Rudd
;
; ---------------------------------------------------------
;|                                                         |
;|  A Diagnostic Tool for C++, Java, and Clojure           |
;|  For more information see the UML diagrams in the /doc  |
;|  folder and README.md                                   |
;|                                                         |
; ---------------------------------------------------------


; ------------
;|            |
;|  Keywords  |
;|            |
; ------------


;These terms are keywords that the program
;looks for to identify the potential problem.
;The terms are different depending upon the
;symptom and the language.
(def language-dependent-terms {:C++    {:infinite-loop               ["for" "while" "do"]
                                        :fails-to-produce-output     ["cout"]
                                        :executes-too-few-times      ["for" "while" "do"]
                                        :executes-too-many-times     ["for" "while" "do"]
                                        :illegal-operation-attempted ["bool" "char" "int" "float" "double" "void" "wchar_t" "string"]}
                             
                              :Java    {:infinite-loop               ["for" "while" "do"]
                                        :fails-to-produce-output     ["print" "println"]
                                        :executes-too-few-times      ["for" "while" "do"]
                                        :executes-too-many-times     ["for" "while" "do"]
                                        :illegal-operation-attempted ["byte" "short" "int" "long" "float" "double" "boolean" "char" "String"]}
 
                             :Clojure  {:infinite-loop               ["for" "while" "do"]
                                        :fails-to-produce-output     ["print" "println"]
                                        :executes-too-few-times      ["for" "while" "do"]
                                        :executes-too-many-times     ["for" "while" "do"]
                                        :illegal-operation-attempted ["byte" "short" "int" "long" "float" "double" "boolean" "char" "String"]}})


; -----------------------------
;|                             |
;|  All function declarations  |
;|                             |
; -----------------------------

;Function declarations are ordered such
;that they tell the story of what happens
;in the program.
(declare programming-language?
         extension
         extension-language?
         set-metadata
         opened-file?
         diagnose
         process-file)



; -----------------
;|                 |
;|  Start Program  |
;|                 |
; -----------------
(defn programming-language?
  "This function is called if the
   extension of the file is generic
   or not known (i.e. if it is a .txt
   file). Each language has some very
   specific termonology and syntax standards
   that makes it easy for a programmer
   experienced in the language to identify. 
   This termonology includes #include <foo> for
   C++, package or import bar for Java, and (ns class.core)
   for Clojure."
  [program]
  
  (let [first-line (nth program 0)]
    (cond
      (.contains first-line "#include")      "C++"
      (.contains first-line "package")       "Java"
      (.contains first-line "import")        "Java"
      (.contains first-line "public class")  "Java"
      (.contains first-line "private class") "Java"
      (.contains first-line "ns")            "Clojure"
      :else nil)))


(defn extension-language?
  "Identifies the programming language
   associated with the given extensions"
  [ext]
  
  (cond
    (= ".cpp"  ext)  "C++"
    (= ".java" ext)  "Java"
    (= ".clj"  ext)  "Clojure"
    :else nil))


(defn extension
  "The extension of the file
   is found by looking for the
   '.' character in the file name
   and then taking all the characters
   that follow after this." 
  [file]

  (subs file (.indexOf file ".")))


(defn set-metadata
  "This method goes through the
   various functions used to
   deduce the programming-language
   the code was written in and then
   sets that as the metadata for the
   symptom, filename, and file contents"
  [[symptom & file-info]]
  
  (if (empty? file-info)
    (vector symptom)
    (if-let [ext-language (extension-language? (extension (first file-info)))]
      (with-meta (conj (vector symptom) (first file-info) (second file-info)) {:language ext-language
                                                                               :symptom  symptom})
    
      (let [language (programming-language? (second file-info))]
        (with-meta (conj (vector symptom) (first file-info) (second file-info)) {:language language
                                                                                 :symptom  symptom})))))


;CONSIDER, this function is likely not needed any
;longer. The GUI has a function which slurps the file
;which is opened. Perhaps it would make more sense
;to move that simply function to this file instead.
;The GUI core should not have any functional material
;within it. The problem is that updating the file name
;between threads has proven to be difficult.
(defn process-file
   [file]
  
  (with-open [reader (BufferedReader. (FileReader. file))]
    (loop [remaining-lines (line-seq reader)
          file-contents (vector)]
      (if (empty? remaining-lines)
        file-contents
        (let [[head & remain] remaining-lines]
          (recur remain (conj file-contents head)))))))


(defn opened-file?
  "Checks to see if a file has
   been opened by the user in the
   GUI input section. Since the code
   uses lists to pass data around
   if the list has more than one 
   element that indicates that a file
   has been opened."
  [input-list]
  
  (let [size (count input-list)]
    (if
      (> size 1)  true
      false)))


(defn analyze-keywords
  "This method ought to take the
   line number, find the keyword,
   and then push it into a stack
   anlyzing each member element.
   Then it will check to make sure the
   syntax and logic of those pieces
   is correct"
  [[keyword & line-number]]
  
  )

(defn solution
  "Attempts to search for a
   solution to the symptom based
   on the type of symptom chosen"
  [keywords]
  
  (loop [remaining-keywords keywords
         all-issues         (str)]
    (if (empty? remaining-keywords)
      all-issues
      (let [[head & remain] remaining-keywords
            [keyword-name & line-num] head]
        (recur remain (join all-issues (str "Issue found on line " line-num " and the keyword is " keyword-name".")))))))        ;(recur remain (conj all-issues (analyze-keywords head)))))))


(defn search-for-keywords
  "Searches the code for the keywords
   and then saves them and the line
   number they were found at in a list"
  [code loops line-number]
  
  (loop [remaining-loops loops
         problem         (vector)]
   
    (if (empty? remaining-loops)
      problem
      (let [[head-loop & remain-loop] remaining-loops]
        (if (.contains code head-loop)
          ;(recur remain-loop (conj problem [head-loop line-number]))
          (recur remain-loop (conj problem [code line-number]))
          (recur remain-loop problem))))))


(defn search 
  [code language symptom-key]
   
  (let [symptom-keywords (symptom-key ((keyword language) language-dependent-terms))]
    
    (loop [remaining-code     code
           potential-problems (vector)
           line-number        1]
      
      (if (empty? remaining-code)
        potential-problems
        (let [[head-code & remain-code] remaining-code
              problem                  (search-for-keywords head-code symptom-keywords line-number)]
          (if (empty? problem)
            (recur remain-code potential-problems (inc line-number))
            (recur remain-code (concat potential-problems problem) (inc line-number))))))))


(defn convert-to-key
  "Converts the symptom chosen into
   a key-value by first making the entire
   string lower case, then replacing
   all spaces with a '-' character,
   and finally converting it to a keyword."
  [symptom]
  
  (keyword (replace (lower-case (str symptom)) #" " "-")))
         

(defmulti diagnose
  "The parameter 'symptom' is a list of both
   channel elements. The elements include
   both the symptom chosen by the user and, 
   if the user has opened a file, the file 
   contents. 
   If there is only one element
   in the parameter 'symptom' that means
   the user has NOT opened a file and thus
   the program should simply give a generic 
   description of the probable cause based 
   on what the user has chosen from the 
   drop-down menu.
   However, if there are two elements
   that implies that the user has
   opened a file. Based on the symptom
   chosen from the drop down menu the
   method will attempt to search for
   terms based on the language's syntax.
   Once the terms are found it will attempt
   to diagnose the issue automatically by
   checkiing the logic of the statement in
   question. If the problem is found it will
   notify the user of the line number and
   ask if they would like it to be fixed 
   automatically or manually take care of it.
   If the problem is not found it will notify
   the user of the line numbers of all potential 
   problems (potential problems are all instances
   of the given keywords which are found) and
   give them likely logical issues based on the 
   symptom."
   (fn [symptom] (count symptom)))


(defmethod diagnose 1
  [symptom]
  
  (let [diagnosis more-information]
  (match [(apply str symptom)]
    ["Infinite Loop"]                infinite-loop-diagnosis
    
    ["Fails to Produce Output"]      fails-to-produce-output-diagnosis
    
    ["Executes Too Few Times"]       executes-too-few-times-diagnosis
         
    ["Executes Too Many Times"]      executes-too-many-times-diagnosis
    
    ["Illegal Operation Attempted"]  illegal-operation-attempted-diagnosis)))


(defmethod diagnose 3
  [symptom]
  
  (let [sympt     (convert-to-key (:symptom (meta symptom)))
        file-name (nth symptom 1)
        code      (process-file file-name)
        language  (:language (meta symptom))]
    
    (if (not (nil? language))
       (solution (search code language sympt)))))


(defn -main
  [& args]
  
  (let [window @window-setup]
    (draw-window window)
    (go
      (loop [input-list (<! user-input)]
        (let [input-with-metadata (set-metadata input-list)]
          (if (not (opened-file? input-with-metadata))
            (alert-window more-information))
          (draw-temp-window (diagnose input-with-metadata)))
        (recur (<! user-input))))))
      
      
      
