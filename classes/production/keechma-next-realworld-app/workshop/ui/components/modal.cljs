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


(defnc ModalRenderer [props]
       (let [route (use-sub props :router)]
         (d/div
           {:class "fixed col-12 flex justify-center"
            :style {:height           "100vh"
                    :width            "100vw"
                    :align-items      "center"
                    :background-color "rgba(0,0,0,0.4)"}}
           (d/div
             {:class "relative col-6"
              :style {:background-color "white"
                      :min-height       "60vh"}}
             (d/div
               {:class "flex justify-end"}
               (d/button
                 {:class    "btn"
                  :style    {:background-color "white"}
                  :on-click #(dispatch props :router :redirect! (dissoc route :form))}
                 "x"))
             (d/h3 {:class "pl2"} "Reply")
             (d/div {:class "mt2"}
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
                              {:class "btn btn-lg pull-xs-right"} "Send")))))))

(def Modal (with-keechma ModalRenderer))