(ns ecommerce.core
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce.db :as db]
            [ecommerce.model :as model]))


(def conn (db/open-connection))


(db/create-schema conn)

(let [notebook (model/new-product
                 "Notebook"
                 "/notebook"
                 3550.89M)]
  (d/transact conn [notebook]))

; db no instante que executou a linha
(def db (d/db conn))

(pprint (d/q '[:find ?entidade
       :where [?entidade :product/name]] db))

(let [celular (model/new-product
                 "Celular"
                 "/celular"
                 3550.89M)]
  (d/transact conn [celular]))

; db no instante que executou a linha
; NOVO SNAPSHOT
(def db (d/db conn))

(pprint (d/q '[:find ?entidade
       :where [?entidade :product/name]] db))


;;datomic suporta somente um dos indentificadores
(let [calc {:product/name "Calculadora"}]
  (d/transact conn [calc]))


(let [celular-barato (model/new-product
                 "Celular Barato"
                 "/celular-barato"
                 3550.89M)
      resultado @(d/transact conn [celular-barato])
      id-entidade (first (vals (:tempids resultado)))]
  (pprint resultado)
  (pprint @(d/transact conn [[:db/add id-entidade :product/price 0.1M]]))
  (pprint (first (vals (:tempids resultado)))))

