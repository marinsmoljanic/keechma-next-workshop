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

(defn filter-user-emails [emails]
      (filterv #(= (:to %) current-user) emails))

(defn filter-important-emails [emails]
      (->> (filter-user-emails emails)
           (filterv #(:isImportant %))))

(defn filter-sent-emails [emails]
      (filterv #(= current-user (:from %)) emails))

(defn filter-data [emails page]
      (case page
            "inbox" (filter-user-emails emails)
            "important" (filter-important-emails emails)
            "sent" (filter-sent-emails emails)
            emails))

(defn get-email-by-id [id]
      (->
        (filterv #(= (:id %) id) @emails)
        (first)))

(defn html-scroll-top []
      (->
        (oget js/window :document)
        (ocall :getElementsByTagName "html")
        (oget :0)
        (oset! "scrollTop" 0)))

(defnc MessageContent [props]
       (let [route (use-sub props :router)
             {:keys [page subpage]} route
             email (get-email-by-id subpage)]

            (d/div {:class "mt2 col-5"}
                   (d/div {:class "col-11 flex flex-column rounded border"
                           :style {:background-color "rgb(233, 233, 233)"
                                   :box-shadow       "10px 10px 10px -2px #D7D7D7"}}
                          ($ Header {:email email & props})
                          (d/div {:class "px2 py2"}
                                 (:body email))
                          (d/button {:class    "btn col-2 ml2 mt2 mb2"
                                     :style    {:background-color "rgb(42, 215, 169)"
                                                :color            "white"}
                                     :on-click (fn [e]
                                                   (dispatch props :router :redirect! (-> (dissoc route :box)
                                                                                          (assoc :reply "true")))
                                                   (dispatch props :message-form :reset-form {})
                                                   (dispatch props :message-form :update-form-data (if-not (= page "sent")
                                                                                                           (:from email)
                                                                                                           (:to email))))} "Reply")))))

(defnc Message [{:keys [email] :as props}]
       (let [important (:isImportant email)
             route (use-sub props :router)]
            (d/div {:class    "flex flex-row mx2 px2"
                    :on-click (fn [e]
                                  (html-scroll-top)
                                  (dispatch props :router :redirect! (assoc route :subpage (:id email))))}

                   (d/div {:class "px1 border-bottom flex justify-center"
                           :style {:align-items  "center"
                                   :border-color "rgb(42, 215, 169)"}}
                          (if (true? important)
                            (d/img {:width  "50px"
                                    :height "50px"
                                    :src    "https://cdn3.iconfinder.com/data/icons/bold-blue-glyphs-free-samples/32/Info_Circle_Symbol_Information_Letter-512.png"})
                            (d/img {:width  "50px"
                                    :height "50px"
                                    :src    "https://cdn1.iconfinder.com/data/icons/system-1/1000/System-33-512.png"}))
                          )

                   (d/div {:class "flex flex-column border-bottom col-12 pl2 pt2"
                           :style {:border-color "rgb(42, 215, 169)"
                                   :height       "100px"}}
                          (d/b (:subject email))
                          (if-not (= (:page route) "sent")
                                  (d/div "From: " (:from email))
                                  (d/div "To: " (:to email)))))))

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
