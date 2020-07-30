(ns workshop.ui.components.compose
  (:require [keechma.next.helix.core :refer [with-keechma use-sub dispatch]]
            [keechma.next.helix.lib :refer [defnc]]
            [helix.core :as hx :refer [$ <> suspense]]
            [helix.dom :as d]
            ["react" :as react]
            [app.ui.components.inputs :refer [wrapped-input]]
            ["react-dom" :as rdom]
            [app.settings :as settings]
            [keechma.next.controllers.router :as router]
            [clojure.string :as str]))

(defnc ComposeForm [props]
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
                               :input/id                "important"
                               :input/attr              :important})
               (d/button
                 {:class "btn btn-lg pull-xs-right"} "Send")))


(defnc ComposeRenderer [props]
       (let [route (use-sub props :router)
             {:keys [page subpage box]} route]
         (if (not box)
           (d/div {:class    "fixed bottom-0 right-0 mr2 rounded"
                   :style    {:background-color "black"
                              :color            "white"
                              :font-size        "1.2rem"
                              :padding          "0px 6rem"}
                   :on-click #(dispatch props :router :redirect! (assoc route :box true))}
                  "Compose Message")
           (d/div {:class "fixed bottom-0 right-0 rounded col-4 border mr2"
                   :style {:color            "black"
                           :font-size        "1.2rem"
                           :background-color "white"
                           :min-height "400px"
                           :z-index "1000"}}
                  (d/div {:class "col-12 flex flex-row "
                          :style {:background-color "rgb(71,183,215)"
                                  :color "white"
                                  :justify-content "space-between"
                                  :padding ".6rem"}}
                    (d/div "New Message")
                    (d/button {:class "px1 py1 flex justify-center rounded btn"
                               :style {:background-color "#414177"
                                       :padding ".2rem .4rem"}
                                    :on-click #(dispatch props :router :redirect! (dissoc route :box))} "x"))
                  ($ ComposeForm {& props})))))

(def Compose (with-keechma ComposeRenderer))
