(ns diagnose.constants)

;Break the string up into smaller building blocks
;For example, the infinite-loop-diagnosis and 
;fails-to-produce-output-diagnosis both have a similar
;diagnosis regarding looping conditions.

(def ^:const more-information
  "Please open a file for more detailed information on your specific symptom.")

(def ^:const infinite-loop-diagnosis
  " Identify all 'for', 'while', 'doseq', 'do-while' 
\r (etc.) loops (NOTE: 'foreach' loops should never
\r enter into an infinite loop so they shouldn't be 
\r an issue) in your code. The the loop should be 
\r analyzed, depending on the type, for logical errors.

\r Errors include but are not limited to the following types:
\t (1) for(int i = x; i < y; i--)    ;where 'x' is less than 'y' such that 'i' is always less than 'y' 
\t (2) for(int i = x; i > y; i++)    ;where 'x' is greater than 'y' such that 'i' is always greater than 'y' 
\t (3) while(x)                      ;where 'x' always evaluates to 'true'")


(def ^:const fails-to-produce-output-diagnosis
  " There could be a variety of problems in this case. 
\r (1) Check to make sure you HAVE a 'print',
\r 'println' or 'cout' statement in your code.
                                         
\r (2) If your print statement in nested in a 
\r 'for', 'while', 'doseq', 'do-while' (etc.)
\r loop then check to make sure your loops are 
\r executing. The reason a loop may not execute 
\r is that the conditional never evaluates to true. 
\r Instances when this would happen include
\r but are not limited to the following types:
                                         
\r\t (a) for(int i = x; i < y; i++)     ;where 'x' is greater than 'y' such that 'i' is always greater than 'y' 
                                        
\r\t (b) for(int i = x; i > y; i--)     ;where 'x' is less than 'y' such that 'i' is always less than 'y'
                                        
\r\t (c) while(x)                       ;where 'x' is always evaluated to 'false'
                                         
\r (3) Check to make sure that the conditionals in
\r your 'if' or 'case' statements are satisified. These are
\r satisfied only if the conditional evaluates to 'true',
\r so check to make sure that your logic holds.")


(def ^:const executes-too-few-times-diagnosis
  " Identify all all 'for', 'while', 'foreach', 'doseq', 
\r 'do-while' (etc.) loops in the your program. Then, 
\r depending on the type of loop, you should attempt to 
\r identify various potential problems. 
\r Such problems include but are not limited to the 
\r following types:
                                      
\r\t (1) for(int i = x, i < y, i++)    ;where 'x' should be 'x - 1'
                                        
\r\t (2) for(int i = x, i < y, i++)    ;where 'i < y' should be 'i <= y'
                                         
\r\t (3) for(word : sentence)          ;where 'sentence.length()' should be 'sentence.length() + 1'
                                         
\r\t (4) while(x != y) or while(x < y) ;where 'y' should be 'y + 1' or 'x < y' should be 'x <= y' respectively")


(def ^:const executes-too-many-times-diagnosis 
  " Identify all all 'for', 'while', 'foreach', 'doseq', 
\r 'do-while' (etc.) loops in the your program. Then, 
\r depending on the type of loop, you should attempt to 
\r identify various potential problems. 
\r Such problems include but are not limited to the 
\r following types:
                                      
\r\t (1) for(int i = x, i < y, i++)    ;where 'x' should be 'x + 1'
                                        
\r\t (2) for(int i = x, i <= y, i++)    ;where 'i <= y' should be 'i < y'
                                         
\r\t (3) for(word : sentence)          ;where 'sentence.length()' should be 'sentence.length() - 1'
                                         
\r\t (4) while(x != y) or while(x <= y) ;where 'y' should be 'y - 1' or 'x <= y' should be 'x < y' respectively")

(def ^:const illegal-operation-attempted-diagnosis 
  " Check all declaration types (int, String, char, 
\r boolean, etc.) and write down the name of the 
\r declared variable name and its type so that you 
\r end up with a list of all variable names. Then, 
\r for every operation attempted (+, *, ++, += etc.) 
\r check the variables being operated upon and make
\r sure they are of the same type. All variables should
\r be of the same type if you are attempting to preform
\r operations on them.")