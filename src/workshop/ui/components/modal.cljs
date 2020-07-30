(ns workshop.ui.components.modal
  (:require [keechma.next.helix.core :refer [with-keechma use-sub dispatch]]
            [keechma.next.helix.lib :refer [defnc]]
            [helix.core :as hx :refer [$ <> suspense]]
            [helix.dom :as d]
            [keechma.next.controllers.router :as router]
            ["react" :as react]
            [app.ui.components.inputs :refer [wrapped-input]]
            ["react-dom" :as rdom]
            [app.settings :as settings]
            [keechma.next.controllers.router :as router]
            [clojure.string :as str]))

(defnc ModalForm [props]
  (d/form {:class     "px2 py2"
           :on-submit (fn [e]
                        (.preventDefault e)
                        (dispatch props :message-form :keechma.form/submit)
                        )}
          (wrapped-input {:keechma.form/controller :message-form
                          :input/type              :text
                          :input/attr              :email
                          :placeholder             "username@mail.com"})
          (wrapped-input {:keechma.form/controller :message-form
                          :input/type              :text
                          :input/attr              :subject
                          :placeholder             "Enter Subject"})
          (wrapped-input {:keechma.form/controller :message-form
                          :input/type              :textarea
                          :input/attr              :body
                          :placeholder             "your message"})
          (wrapped-input {:keechma.form/controller :message-form
                          :input/type              :checkbox
                          :class                   "ml2"
                          :input/label             "Is Important"
                          :input/attr              :important})
          (d/button
           {:class "btn btn-lg pull-xs-right"} "Send")))

(defnc ModalRenderer [props]
  (let [route (use-sub props :router)]
    (d/div
     {:class "fixed col-12 flex justify-center"
      :style {:height           "100vh"
              :width            "100vw"
              :align-items      "center"
              :background-color "rgba(0,0,0,0.4)"
              :z-index "7"}}
     (d/div
      {:class "relative col-6"
       :style {:background-color "white"
               :min-height       "50vh"}}

      (d/div {:class "flex flex-row col-12"
              :style {:background-color "rgb(42, 215, 169)"
                      :color "white"
                      :justify-content "space-between"
                      :padding ".6rem"
                      :border "none"
                      :z-index "9"}}
         (d/div
          {:class "flex justify-end"}
          (d/button
           {:class    "btn"
            :style    {:background-color "rgb(42, 215, 169)"
                       :font-size "25px"}}
           "Reply"))
         (d/div
          {:class "flex justify-end"}
          (d/button
           {:class    "btn"
            :style    {:background-color "rgb(42, 215, 169)"}
            :on-click (fn [e]
                        (dispatch props :message-form :reset-form {})
                        (dispatch props :router :redirect! (dissoc route :reply)))}
           "X")))
      (d/div {:class "mt2"}
      ($ ModalForm {& props}))))))

(def Modal (with-keechma ModalRenderer))



















