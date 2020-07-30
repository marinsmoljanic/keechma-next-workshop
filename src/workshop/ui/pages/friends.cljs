(ns workshop.ui.pages.friends
  (:require [keechma.next.helix.core :refer [with-keechma use-sub use-meta-sub dispatch call]]
            [keechma.next.helix.lib :refer [defnc]]
            [helix.core :as hx :refer [$ <> suspense]]
            ["react" :as react]
            ["react-dom" :as rdom]
            [keechma.next.controllers.pipelines :refer [throw-promise!]]
            [keechma.next.controllers.router :as router]
            [helix.dom :as d]
            [app.ui.components.inputs :refer [wrapped-input]]
            [workshop.ui.components.sidebar :refer [Sidebar]]
            [workshop.ui.components.compose :refer [Compose]]
            [workshop.ui.components.modal :refer [Modal]]
            [workshop.ui.components.friends :refer [Friends]]))

(defnc FriendsRenderer [{:keys [reply?] :as props}]
       (let [role (use-sub props :role)]
            (if (= role :guest)
              (dispatch props :router :redirect! {:page "login"})
              (<>
                ($ Sidebar)
                ($ Friends)
                ($ Compose)
                ))))

(def Friendspage (with-keechma FriendsRenderer))
