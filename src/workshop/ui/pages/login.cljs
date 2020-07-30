(ns workshop.ui.pages.login
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
            [workshop.ui.components.messages :refer [Messages]]
            [workshop.ui.components.modal :refer [Modal]]))

(defnc LoginForm [props]
  (d/form
   {:on-submit (fn [e]
                 (.preventDefault e)
                 (dispatch props :login-form :keechma.form/submit))}
   (wrapped-input {:keechma.form/controller :login-form
                   :input/type              :text
                   :input/attr              :email
                   :placeholder             "Email"
                   :style                   {:border "1px solid black"}})
   (wrapped-input {:keechma.form/controller :login-form
                   :input/type              :text
                   :input/attr              [:password]
                   :type                    "password"
                   :placeholder             "Password"
                   :style                   {:border "1px solid black"}})
   (d/button
    {:class "btn btn-lg"
     :style {:margin  "auto"
             :display "block"
             :background-color "white"
             :color "black"
             :border "1px solid black"}} "Sign In")))

(defnc LoginRenderer
  [props]
  ;; use-sub funkciju proucit
  (let [role (use-sub props :role)]
    ;; gdje je poveznica ovog filea sa kljucem :user. Dal se radi o kljucu na globalnoj razini?
    ;; ako je korisnik ulogiran preusmjeri ga na inbox, a ako nije sto se onda dogadja? Rendera se donji html?
    (when (= role :user)
          (dispatch props :router :redirect! {:page "inbox"}))
    (d/div
     {:class "col-12 flex justify-center"
      :style {:height      "100vh"
              :align-items "center"
              :background-color "#5cb85c"}}
     (d/div
      {:class "col-6"}
      (d/h1
       {:class "text-xs-center pb4"
        :style {:color "white"
                :text-shadow "1px 1px black"}}
       "Sign in")
      ($ LoginForm {& props})))))

(def Login (with-keechma LoginRenderer))