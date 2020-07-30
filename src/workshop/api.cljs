(ns workshop.api
  (:require [keechma.next.toolbox.ajax :refer [GET POST DELETE PUT]]
            [workshop.settings :as settings]
            [promesa.core :as p]))

(def default-request-config
  {:response-format :json
   :keywords? true
   :format :json})

(defn add-auth-header [req-params jwt]
  (if jwt
    (assoc-in req-params [:headers :authorization] (str "Token " jwt))
    req-params))

(defn add-params [req-params params]
  (if params
    (assoc req-params :params params)
    req-params))

(defn req-params [& {:keys [jwt data]}]
  (-> default-request-config
      (add-auth-header jwt)
      (add-params data)))

(defn process-users [data]
      (:data data))

(defn rand-user-page [_]
      (str _
           (let [rand-num (rand-int 10)]
                (if (< rand-num 5) 1 2))))

(defn users-get [_]
      (->> (GET (str settings/users-api-endpoint (rand-user-page "?page="))
                (req-params))
           (p/map process-users)))