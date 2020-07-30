(ns workshop.controllers.login-form
  (:require [keechma.next.controller :as ctrl]
            [keechma.next.controllers.pipelines :as pipelines]
            [keechma.next.toolbox.pipeline :refer [pswap! preset!] :refer-macros [pipeline!]]
            [app.api :as api]
            [promesa.core :as p]
            [keechma.next.controllers.form :as form]
            [app.validators :as v]
            [promesa.core :as p]))

(derive :login-form ::pipelines/controller)

(def pipelines
  {:keechma.form/submit-data (pipeline! [value ctrl]
                                        (ctrl/broadcast ctrl :login {:token "213ASDFFSA23"})
                                        (ctrl/dispatch ctrl :router :redirect! {:page "inbox"}))})

(defmethod ctrl/prep :login-form [ctrl]
           (pipelines/register ctrl (form/wrap pipelines (v/to-validator {:email [:email :not-empty]
                                                                          :password [:not-empty :ok-password]}))))
