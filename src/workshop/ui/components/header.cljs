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
            :style {:justify-content "space-between"
                    :background-color "white"}}
           (d/div {:class "flex flex-row"}
                  (d/h5 {:class "bold"} (:subject email)))
           (d/button {:style    {:color       "white" :background-color "rgb(255, 145, 145)"
                                 :border      "none"
                                 :width       "20px"
                                 :height      "20px"
                                 :align-items "center"}
                      :class    "px1 py1 flex justify-center rounded"
                      :on-click #(dispatch props :router :redirect! (dissoc route :subpage))} "-"))))

(def Header (with-keechma HeaderRenderer))
