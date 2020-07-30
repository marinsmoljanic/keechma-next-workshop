(ns workshop.app
  (:require ["react-dom" :as rdom]
            [keechma.next.controllers.router]
            [keechma.next.controllers.subscription]
            [keechma.next.controllers.entitydb]
            [keechma.next.controllers.dataloader]
            [workshop.controllers.message-form]))


(defn message-box-opend? []
  (fn [{:keys [router]}]
    (= "true" (:box router))))

(def app
  {:keechma.subscriptions/batcher rdom/unstable_batchedUpdates
   :keechma/controllers           {:router {:keechma.controller/params true
                                            :keechma.controller/type   :keechma/router
                                            :keechma/routes            [["" {:page "inbox"}]
                                                                        ":page"
                                                                        ":page/:subpage"
                                                                        ]}

                                   :message-form {:keechma.controller/deps [:router]
                                                  :keechma.controller/params true}}
   :keechma/apps                  {}})