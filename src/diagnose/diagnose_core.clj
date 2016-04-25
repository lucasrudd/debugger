(ns diagnose.diagnose-core
  (require [clojure.core.match :refer :all])
  (require [diagnose.constants :refer :all]))


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
  
  (println "Open a file for more detailed information on your symptom.\n")
  (match [(apply str symptom)]
    ["Infinite Loop"]           infinite-loop-diagnosis

    ["Fails to Produce Output"] fails-to-produce-output-diagnosis
    
    ["Executes Too Few Times"]  executes-too-few-times-diagnosis))

(defmethod diagnose 2
  
  [symptom]
  
  (match [(apply str symptom)]
    ["Infinite Loop"] (println "hi")))


