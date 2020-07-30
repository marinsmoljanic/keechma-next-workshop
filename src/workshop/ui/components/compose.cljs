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

(defnc ComposeForm [{:keys [recepient] :as props}]
  (d/form {:class     "px2 py2"
           :on-submit (fn [e]
                        (.preventDefault e)
                        (dispatch props :message-form :keechma.form/submit)
                        )}
          (wrapped-input {:keechma.form/controller :message-form
                          :input/type              :text
                          :input/attr              :email
                          :value                   recepient
                          :placeholder             "E-mail address"})
          (wrapped-input {:keechma.form/controller :message-form
                          :input/type              :text
                          :input/attr              :subject
                          :placeholder             "Subject"})
          (wrapped-input {:keechma.form/controller :message-form
                          :input/type              :textarea
                          :input/attr              :body
                          :placeholder             "Message"})
          (wrapped-input {:keechma.form/controller :message-form
                          :input/type              :checkbox
                          :class                   "ml2"
                          :input/label             "Important"
                          :input/id                "important"
                          :input/attr              :important})
          (d/button
           {:class "btn btn-lg pull-xs-right"} "Send")))


(defnc ComposeRenderer [props]
  (let [route (use-sub props :router)
        {:keys [page subpage box email]} route]
    "Ako u ruti nema box-a onda prikazuj dolje fiksni link za compose message, a inace message formu"
    (if (not box)
      (d/div {:class    "fixed bottom-0 right-0 "
              :style    {:background-color "rgb(42, 215, 169)"
                         :color            "white"
                         :font-size        "1.2rem"
                         :padding          "0px 6rem"
                         :border-radius "5px 0px 0px"}
              :on-click #(dispatch props :router :redirect! (assoc route :box true))}
             "Compose Message")
      (d/div {:class "fixed bottom-0 right-0 col-4 border"
              :style {:color            "black"
                      :box-shadow "0px 20px 10px 10px #D7D7D7"
                      :font-size        "1.2rem"
                      :background-color "white"
                      :min-height "400px"
                      :z-index "5"
                      :border-radius "5px 0px 0px"
                      :border-color "#83c3b2"}
                      }
             (d/div {:class "col-12 flex flex-row "
                     :style {:background-color "rgb(42, 215, 169)"
                             :color "white"
                             :justify-content "space-between"
                             :padding ".6rem"
                             :border "none"
                             :z-index "7"}
                     :on-click #(dispatch props :router :redirect! (dissoc route :box))}
                    (d/div "New Message")
                    )
             ($ ComposeForm {:recepient email & props})))))

(def Compose (with-keechma ComposeRenderer))
