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

(defn select-bg-color [page text]
  (if (= (str/lower-case page) (str/lower-case text))
    "rgb(50,127,196)"
    "white"))

(defn select-text-color [page text]
  (if (= (str/lower-case page) (str/lower-case text))
    "white"
    "rgb(50,127,196)"))

(defnc SidebarButton [{:keys [text href active-page] :as props}]
       (let [bg-color (select-bg-color active-page text)
             text-color (select-text-color active-page text)]

         (d/a {:class "mt2 px2 py1 rounded"
               :style {:color text-color :background-color bg-color}
               :href  href} text)))

(defnc SidebarRenderer [props]
       (let [{:keys [page]} (use-sub props :router)]
         (d/div {:class "flex flex-column px2 col-2"}
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
                                  &     props}))))

(def Sidebar (with-keechma SidebarRenderer))
