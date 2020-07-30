(ns workshop.ui.components.sidebar
  (:require [keechma.next.helix.core :refer [with-keechma use-sub dispatch]]
            [keechma.next.helix.lib :refer [defnc]]
            [helix.core :as hx :refer [$ <> suspense]]
            [helix.dom :as d]
            ["react" :as react]
            ["react-dom" :as rdom]
            [app.settings :as settings]
            [keechma.next.controllers.router :as router]
            [clojure.string :as str]))

(defn selected-bg-color [page text]
  (if (= (str/lower-case page) (str/lower-case text))
    "rgb(57, 226, 226)"
    "rgb(42, 215, 169)"))

(defn selected-text-color [page text]
  (if (= (str/lower-case page) (str/lower-case text))
    "white"
    "white"))



(defnc SidebarButton [{:keys [text href active-page] :as props}]
  (let [bg-color (selected-bg-color active-page text)
        text-color "white"]

    (d/a {:class "mt2 px2 py1"
          :style {:color text-color :background-color bg-color}
          :href  href} text)))

(defnc SidebarRenderer [props]
  (let [{:keys [page]} (use-sub props :router)]
    (d/div {:class "flex flex-column px2 col-2" :style {:background-color "rgb(214, 253, 253)"
                                                        :height "100vh"
                                                        :box-shadow "10px 0 10px -2px #ECECEC"
                                                        :border-right "1px solid #83c3b2"}}
           ($ SidebarButton {:text "Inbox"
                             :href (router/get-url props :router {:page "inbox"})
                             :active-page page
                             &     props})
           ($ SidebarButton {:text "Important"
                             :href (router/get-url props :router {:page "important"})
                             :active-page page
                             &     props})
           ($ SidebarButton {:text "Sent"
                             :href (router/get-url props :router {:page "sent"})
                             :active-page page
                             &     props})
           (d/div (d/hr))
           ($ SidebarButton {:text "Friends"
                             :href (router/get-url props :router {:page "friends"})
                             :active-page page
                             &     props})

           (d/button
            {:class "btn mt4"
             :style {:background-color "rgb(193, 193, 193)"
                     :color "white"}
             :on-click #(dispatch props :jwt :clear)}
            "Log out"))))

(def Sidebar (with-keechma SidebarRenderer))
