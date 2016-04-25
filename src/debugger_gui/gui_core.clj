(ns debugger-gui.gui-core
  (:gen-class)
  (:require [clojure.core.async :as a 
                                :refer [>! <! >!! <!! go chan buffer close!]]
            [seesaw.core    :refer :all]
            [seesaw.font    :refer :all]
            [seesaw.chooser :refer :all])
  (:import  (java.io BufferedReader FileReader)))

;A channel to communicate between threads
(def user-input (chan))
(def file-name)

(def skin-setup
 
  (future
    (javax.swing.UIManager/setLookAndFeel "javax.swing.plaf.nimbus.NimbusLookAndFeel")))

(def my-font (font :name "Arial"
                   :size 22))

;TODO: Fix the create-restaurant-label function
;(defn create-label
;  [top-ten-restaurant-vector]
;  
;  (let [my-font (font :name "Arial"
;                      :size 22)]
;    (loop [remaining-restaurants top-ten-restaurant-vector
;           restaurant-labels (label :text ""
;                                    :font my-font)]
;      (if (empty? remaining-restaurants)
;        restaurant-labels
;        (let [[head & remaining] remaining-restaurants]
;          (recur remaining (config! restaurant-labels :text (str (config restaurant-labels :text) head))))))))
  

(defn draw-window
  [frame]
  
  (if (= nil frame)
    (alert "Error in draw-window.\n Frame cannot be nil")
    [(pack! frame)
     (show! frame)]))
  

(defn draw-temp-window
  [temp-content]
  
  (let [contents    (styled-text :text        temp-content
                                 :wrap-lines? true
                                 :styles (:font  my-font)) 
        temp-window (frame
                     :title "Diagnostic Tool by Lucas Rudd"
                     :content contents
                     :width 500
                     :height 500)]
  (draw-window temp-window)))


(defn alert-window
  [alert-string]
  
  (alert alert-string))

(defn submit-data
  [string]
  
  (draw-temp-window string))


(defn update-file-name
  [new-file-name]
   
  (def file-name new-file-name))


(defn format-input
  [input]
    
  (loop [remaining-input input
         temp-input      (vector)]
    (if (empty? remaining-input)
      temp-input
      (let [[head & remain] remaining-input]
        (println head)
        (if (bound? head)
          (conj temp-input head)
          (recur remain temp-input))))))


(defn update-user-input
  [new-input]
   
  (>!! user-input new-input))


(defn restaurant-selection-window
  [content content-menu]
  
  (frame
    :title "Software Diagnosic Tool"
    :menubar content-menu
    :content content
    :width 500
    :height 500
    :on-close :exit))

(defn get-file-contents
  [file]
  

  (slurp file))


(def window-setup  
 
  (future
    @skin-setup
    (let  [programming-text-field (text     :text   "Start Programming!"
                                            :multi-line? true
                                            :font   my-font)
        
          symptom-label           (label    :text   "Select a Symptom: "
                                            :font   my-font
                                            :border [20 10])
        
          symptom                 (combobox :model["Infinite Loop"
                                                   "Fails to Produce Output"
                                                   "Executes Too Few Times"
                                                   "Executes Too Many Times"
                                                   "Illegal Operation Attempted"])
          
          file-menu               (menubar  :items [(menu :text "File"
                                                          :items [(action :name "Open..."
                                                                          :key "ctrl O"
                                                                          :handler (fn [e] (choose-file :type :open
                                                                                                        :remember-directory? true
                                                                                                        :filters [["Software files" ["cpp" "java" "clj"]]
                                                                                                                  ["Text Files" ["txt"]]]
                                                                                                        :success-fn (fn [fc file] (update-file-name (.getAbsolutePath file))
                                                                                                                                  (text! programming-text-field (get-file-contents  (.getAbsolutePath file)))))))])])


                                                                    
          
          diagnose-button  (button
                             :text "Diagnose"
                             :listen [:action (fn [event] event (if (bound? #'file-name)
                                                                  (update-user-input [(selection symptom)
                                                                                       file-name
                                                                                      (text programming-text-field)])
                                                                  
                                                                  (update-user-input [(selection symptom)])))])
                         
          window-contents (vertical-panel :items [programming-text-field symptom-label 
                                                  symptom                diagnose-button])]
      
      (restaurant-selection-window window-contents file-menu))))

