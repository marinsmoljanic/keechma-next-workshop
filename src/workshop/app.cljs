(ns workshop.app
  (:require ["react-dom" :as rdom]
            [keechma.next.controllers.router]
            [keechma.next.controllers.subscription]
            [keechma.next.controllers.entitydb]
            [keechma.next.controllers.dataloader]
            [workshop.controllers.message-form]
            [workshop.controllers.jwt]
            [workshop.controllers.login-form]
            [workshop.controllers.friends]
            ))

(defn page-eq? [page]
      (fn [{:keys [router]}]
          (= page (:page router))))

(defn role-eq? [role]
      (fn [deps]
          (= role (:role deps))))


(def homepage? (page-eq? "inbox"))
(def profile? (page-eq? "profile"))


(def app
  {:keechma.subscriptions/batcher rdom/unstable_batchedUpdates
   :keechma/controllers           {:router       {:keechma.controller/params true
                                                  :keechma.controller/type   :keechma/router
                                                  :keechma/routes            [["" {:page "inbox"}]
                                                                              ":page"
                                                                              ":page/:subpage"
                                                                              ]}
                                   :message-form {:keechma.controller/deps   [:router]
                                                  :keechma.controller/params true}
                                   :dataloader   {:keechma.controller/params true
                                                  :keechma.controller/type   :keechma/dataloader}

                                   :jwt          {:keechma.controller/params true}

                                   :role         {:keechma.controller/params (fn [{:keys [jwt]}] (if jwt :user :guest))
                                                  :keechma.controller/type   :keechma/subscription
                                                  :keechma.controller/deps   [:jwt]}

                                   :login-form   {:keechma.controller/params (page-eq? "login")
                                                  :keechma.controller/deps   [:router]}

                                   :friends      #:keechma.controller {:params (page-eq? "friends")
                                                                       :deps   [:router :dataloader]}

                                   }
   :keechma/apps                  {}})