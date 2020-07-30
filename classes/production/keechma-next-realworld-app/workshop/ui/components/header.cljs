(ns workshop.ui.components.header
  (:require [keechma.next.helix.core :refer [with-keechma use-sub dispatch]]
            [keechma.next.helix.lib :refer [defnc]]
            [helix.core :as hx :refer [$ <> suspense]]
            [helix.dom :as d]
            ["react" :as react]
            ["react-dom" :as rdom]
            [app.settings :as settings]
            [keechma.next.controllers.router :as router]
            [clojure.string :as str]))

(defnc HeaderRenderer [{:keys [email] :as props}]
       (let [route (use-sub props :router)]
         (d/div {:class "col-12 flex flex-row border-bottom px2 py2"
                 :style {:justify-content "space-between"}}
                (d/div {:class "flex flex-row"}
                       (when (:isImportant email)
                         (d/span {:class "px1 mr2 rounded center"
                                  :style {:color            "white"
                                          :width            "90px"
                                          :height           "20px"
                                          :font-size        ".7rem"
                                          :background-color "rgb(71,183,215)"}} "IMPORTANT"))
                       (d/h5 {:class "bold"} (:subject email)))
                (d/button {:style    {:color       "white" :background-color "red"
                                      :border      "none"
                                      :width       "20px"
                                      :height      "20px"
                                      :align-items "center"}
                           :class    "px1 py1 flex justify-center rounded"
                           :on-click #(dispatch props :router :redirect! (dissoc route :subpage))} "x"))))

(def Header (with-keechma HeaderRenderer))
