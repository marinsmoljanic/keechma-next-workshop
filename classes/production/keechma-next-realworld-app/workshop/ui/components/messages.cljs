(ns workshop.ui.components.messages
  (:require [keechma.next.helix.core :refer [with-keechma use-sub dispatch]]
            [keechma.next.helix.lib :refer [defnc]]
            [helix.core :as hx :refer [$ <> suspense]]
            [helix.dom :as d]
            ["react" :as react]
            ["react-dom" :as rdom]
            [oops.core :refer [ocall oget oset!]]
            [app.settings :as settings]
            [keechma.next.controllers.router :as router]
            [clojure.string :as str]
            [workshop.ui.components.header :refer [Header]]
            [workshop.data :refer [emails]]))

(def current-user "Camden_Schulist@hotmail.com")

(defn filter-important-emails [emails]
  (filterv #(:isImportant %) emails))

(defn filter-sent-emails [emails]
  (filterv #(= current-user (:from %)) emails))

(defn filter-data [emails page]
  (case page
    "inbox" emails
    "important" (filter-important-emails emails)
    "sent" (filter-sent-emails emails)
    emails))

(defn get-email-by-id [id]
  (->
    (filterv #(= (:id %) id) @emails)                        ;;VRACA NIZ SA JEDNIM ELEMENTOM, BOLJI NACIN ZA VRATITI SAMO ELEMENT (BEZ NIZA)????
    (first)))

(defn html-scroll-top []
  (->
    (oget js/window :document)
    (ocall :getElementsByTagName "html")
    (oget :0)
    (oset! "scrollTop" 0)))

(defnc MessageContent [props]
       (let [route (use-sub props :router)
             {:keys [subpage]} route
             email (get-email-by-id subpage)]

         (d/div {:class "mt2 col-5"}
                (d/div {:class "col-11 flex flex-column rounded border"
                        :style {:background-color "rgb(249,249,249)"}}
                       ($ Header {:email email & props})
                       (d/div {:class "px2 py2"}
                              (:body email))
                       (d/button {:class "btn col-2 ml2 mt2 mb2"
                                  :style {:background-color "rgb(71,183,215)"
                                          :color "white"}
                                  :on-click #(dispatch props :router :redirect! (-> (dissoc route :box)
                                                                                    (assoc :form "true")))} "Reply")))))

(defnc Message [{:keys [email] :as props}]
       (let [important (:isImportant email)
             route (use-sub props :router)]
         (d/div {:class    "flex flex-row mx2 px2"
                 :on-click (fn [e]
                             (html-scroll-top)
                             (dispatch props :router :redirect! (assoc route :subpage (:id email))))}
                (d/div {:class "px1 border flex justify-center"
                        :style {:align-items "center"
                                :border-color "gray"}}
                       (d/img {:width  "50px"
                               :height "50px"
                               :src    (:avatar email)}))
                (d/div {:class "flex flex-column border col-12 pl2 pt2"
                        :style {:border-color "gray"}}
                       (if (true? important)
                         (d/h5 {:class "bold"} (d/span {:class "px2 mr2 rounded"
                                        :style {:color "white"
                                                :width "110px"
                                                :font-size "1.1rem"
                                                :background-color "rgb(71,183,215)"}} "IMPORTANT")(:subject email))
                         (d/h5 {:class "bold"} (:subject email)))
                       (d/p (:from email))))))


(defnc MessagesRenderer [props]
       (let [{:keys [page subpage box]} (use-sub props :router)
             data (filter-data @emails page)
             width (if subpage "col-5 " "col-10 ")]
         (<> (d/div {:class (str width "mt2")}
                    (map
                      (fn [ele]
                        ($ Message {:key ele :email ele & props})) data))
             (when subpage
               ($ MessageContent {& props})))))

(def Messages (with-keechma MessagesRenderer))
