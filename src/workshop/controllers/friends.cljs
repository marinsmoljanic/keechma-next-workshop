(ns workshop.controllers.friends
  (:require [keechma.next.controller :as ctrl]
            [keechma.next.controllers.pipelines :as pipelines]
            [keechma.next.toolbox.pipeline :refer [pswap! preset!] :refer-macros [pipeline!]]
            [workshop.api :as api]
            [keechma.next.controllers.dataloader :as dl]))

;; "Testing file this is!"
(derive :friends ::pipelines/controller)

(def dataloader-options
  {:keechma.dataloader/max-stale true
   :keechma.dataloader/stale-while-revalidate true})

(def pipelines
  {:keechma.on/start (pipeline! [value {:keys [state*] :as ctrl}]
                                (dl/req ctrl :dataloader api/users-get {} dataloader-options)
                                (preset! state* value))})

(defmethod ctrl/prep :friends [ctrl]
           (pipelines/register ctrl pipelines))