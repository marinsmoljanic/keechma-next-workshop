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

(defn structured-email [data]
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
  {:reset-form               (pipeline! [value {:keys [deps-state* meta-state* state*] :as ctrl}]
                                        (pswap! meta-state* form/wrap {}))

   :update-form-data         (pipeline! [value {:keys [deps-state* meta-state* state*] :as ctrl}]
                                        (let [email value
                                              {:keys [page]} (:router @deps-state*)]
                                          (pswap! meta-state* form/assoc-data-in :email email)))

   :keechma.form/submit-data (pipeline! [value {:keys [deps-state* meta-state* state*] :as ctrl}]
                                        (structured-email value)
                                        (api-send-email value)
                                        (ctrl/dispatch ctrl :router :redirect! {:page "sent"})
                                        (ctrl/dispatch ctrl :message-form :reset-form {}))})

"Prep is called once when the app is started (for each controller)"
(defmethod ctrl/prep :message-form [ctrl]
  (pipelines/register ctrl (form/wrap pipelines (v/to-validator {:subject [:not-empty]
                                                                 :email   [:email :not-empty]
                                                                 :body    [:not-empty]}))))

