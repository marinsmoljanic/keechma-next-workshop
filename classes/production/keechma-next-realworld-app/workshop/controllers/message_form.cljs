(ns workshop.controllers.message-form
  (:require [keechma.next.controller :as ctrl]
            [keechma.next.controllers.pipelines :as pipelines]
            [keechma.next.controllers.entitydb :as edb]
            [keechma.next.controllers.form :as form]
            [keechma.next.controllers.dataloader :as dl]
            [keechma.next.toolbox.pipeline :as pp :refer [pswap! preset!] :refer-macros [pipeline!]]
            [app.api :as api]
            [app.validators :as v]
            [app.settings :as settings]
            [workshop.data :refer [emails]]
            [keechma.next.protocols :as pt]))

(derive :message-form ::pipelines/controller)


(def current-user "Camden_Schulist@hotmail.com")

(defn rand-str [len]
  (apply str (take len (repeatedly #(char (+ (rand 26) 65))))))

(defn prepare-data [data]
   {:id          (rand-str 15)
   :from        current-user
   :to          (:email data)
   :body        (:body data)
   :subject     (:subject data)
   :isImportant (:important data)})

(defn api-send-email [data]
  (->>
    (conj @emails data)
    (reset! emails)))

(def pipelines
  {:keechma.form/submit-data (pipeline! [value {:keys [deps-state* meta-state* state*] :as ctrl}]
                                        (prepare-data value)
                                        (api-send-email value)
                                        (ctrl/dispatch ctrl :router :redirect! {:page "sent"})
                                        (pswap! meta-state* form/mount-form {}))}) ;;;RESET FORM!!!

(defmethod ctrl/prep :message-form [ctrl]
  (pipelines/register ctrl (form/wrap pipelines (v/to-validator {:subject [:not-empty]
                                                                 :email   [:email :not-empty]
                                                                 :body    [:not-empty]}))))
