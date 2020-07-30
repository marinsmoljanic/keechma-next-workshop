(ns workshop.ui.main
  (:require [keechma.next.helix.core :refer [with-keechma dispatch use-sub]]
            [keechma.next.helix.lib :refer [defnc]]
            [helix.core :as hx :refer [$ <>]]
            ["react" :as react]
            ["react-dom" :as rdom]
            [keechma.next.controllers.router :as router]
            [helix.dom :as d]
            [app.ui.components.inputs :refer [wrapped-input]]
            [workshop.ui.components.sidebar :refer [Sidebar]]
            [workshop.ui.components.messages :refer [Messages]]
            [workshop.ui.components.compose :refer [Compose]]
            [workshop.ui.components.modal :refer [Modal]]
            [workshop.ui.components.friends :refer [Friends]]
            [workshop.ui.pages.login :refer [Login]]
            [workshop.ui.pages.friends :refer [Friendspage]]
            [workshop.ui.pages.mailbox :refer [MailboxPage]]
            ))

(defnc UserPages [{:keys [reply?] :as props}]
  (let [role (use-sub props :role)]
    (if (= role :guest)
      (dispatch props :router :redirect! {:page "login"})
      (<>
       ($ Sidebar)
       ($ Messages)
       ($ Compose)
       (when reply?
             ($ Modal {& props}))
       ))))

(defnc MainRenderer [props]
  (let [route (use-sub props :router)
        reply? (:reply route)
        page (:page route)]
    (d/div {:class "flex flex-row col-12"}
           (case page
                 "login" ($ Login {& props})
                 "friends" ($ Friendspage {:reply? reply? & props})
                 "inbox" ($ MailboxPage {:reply? reply? & props})
                 "important" ($ MailboxPage {:reply? reply? & props})
                 "sent" ($ MailboxPage {:reply? reply? & props})
                 (d/div (d/h1 "404"))))))

(def Main (with-keechma MainRenderer))