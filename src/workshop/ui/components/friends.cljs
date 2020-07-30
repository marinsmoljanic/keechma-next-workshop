(ns workshop.ui.components.friends
  (:require [keechma.next.helix.core :refer [with-keechma use-sub use-meta-sub dispatch call]]
            [keechma.next.helix.lib :refer [defnc]]
            [helix.core :as hx :refer [$ <> suspense]]
            [helix.dom :as d]
            ["react" :as react]
            ["react-dom" :as rdom]
            [keechma.next.controllers.pipelines :refer [throw-promise!]]
            [workshop.ui.components.sidebar :refer [Sidebar]]
            [workshop.ui.components.messages :refer [Messages]]
            [workshop.ui.components.compose :refer [Compose]]
            [workshop.ui.components.modal :refer [Modal]]
            [keechma.next.controllers.router :as router]
            [oops.core :refer [ocall oget oset!]]
            [app.settings :as settings]
            [clojure.string :as str]
            [workshop.api :as api]
            [workshop.ui.components.header :refer [Header]]
            [workshop.data :refer [emails]]))

(defn html-scroll-top []
      (->
        (oget js/window :document)
        (ocall :getElementsByTagName "html")
        (oget :0)
        (oset! "scrollTop" 0)))


(defnc TagList
       [props]
       #_(throw-promise! (use-meta-sub props :tags) :keechma.on/start)
       (let [tags (use-sub props :tags)]
            (<>
              (d/div
                {:class "tag-list"}
                (map
                  (fn [{:keys [tag]}]
                      (d/a
                        {:class "tag-pill tag-default"
                         :key   tag
                         :href  (router/get-url props :router {:page "friends" :tag tag})}
                        tag
                        ))
                  tags)))))

(defnc FriendsList [{:keys [item] :as props}]
       (let [route (use-sub props :router)]
            (d/div {:class    "flex flex-row mx2 px2 border-bottom"
                    :style    {:border-color "rgb(42, 215, 169)"}
                    :on-click (fn [e]
                                  (html-scroll-top)
                                  (dispatch props :router :redirect! (assoc route :email (:email item) :box true))
                                  )}

                   (d/div {:class "px1 flex justify-center"
                           :style {:align-items "center"}}
                          (d/img {:width  "50px"
                                  :height "50px"
                                  :src    (:avatar item)
                                  :style  {:border-radius "50%"}})
                          )

                   (d/div {:class "flex flex-column border-bottom col-12 pl2 pt2"
                           :style {:border-color "white"
                                   :height       "100px"}}
                          (d/div (d/b "First Name: ")
                                 (:first_name item))
                          (d/div
                            (d/b "Last Name: ")
                            (:last_name item))
                          (d/div
                            (d/b "Email: ")
                            (:email item))
                          ))))

(defnc FriendsRenderer [props]
       (let [{:keys [page subpage box]} (use-sub props :router)
             friends (use-sub props :friends)
             width (if subpage "col-5 " "col-10 ")]
            (<> (d/div {:class (str width "mt2")}
                       (map
                         (fn [frnd]
                             ($ FriendsList {:item frnd & props})) friends)))))

(def Friends (with-keechma FriendsRenderer))
