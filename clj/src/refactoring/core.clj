(ns refactoring.core)

(defprotocol Movie
  (determine-amount [this days-rented])
  (determine-frequent-renter-points [this days-rented]))

(defrecord ChildrensMovie [title]
  Movie
  (determine-amount [this days-rented]
    (+ 1.5
       (if (> days-rented 3)
         (* (- days-rented 3) 1.5)
         0.0)))
  (determine-frequent-renter-points [this _] 1))

(defrecord RegularMovie [title]
  Movie
  (determine-amount [this days-rented]
    (+ 2
       (if (> days-rented 2)
         (* (- days-rented 2) 1.5)
         0.0)))
  (determine-frequent-renter-points [this _] 1))

(defrecord NewReleaseMovie [title]
  Movie
  (determine-amount [this days-rented]
    (* days-rented 3.0))
  (determine-frequent-renter-points [this days-rented]
    (if (> days-rented 1) 2 1)))

(defrecord Rental [movie days-rented])
(defrecord Statement [lines total points])
(defrecord StatementLine [title amount])

(defn add-to-statement [statement {movie :movie days-rented :days-rented}]
  (-> statement
      (assoc
        :lines (conj (:lines statement)
                          (StatementLine. (:title movie)
                                          (determine-amount movie days-rented)))
        :total (+ (:total statement)
                  (determine-amount movie days-rented))

        :points (+ (:points statement)
                   (determine-frequent-renter-points movie days-rented)))))

(defn make-statement [rentals]
  (reduce add-to-statement (Statement. [] 0.0 0) rentals))

(defn format-statement [customer-name statement]
  (str "Rental Record for " customer-name "\n"
       (apply str (map #(str "\t" (:title %) "\t" (:amount %) "\n") (:lines statement)))
       "You owed " (:total statement) "\n"
       "You earned " (:points statement) " frequent renter points \n"))

(defn -main []
  (let [rentals [(Rental. (RegularMovie. "Reg1") 1)
                 (Rental. (NewReleaseMovie. "NR2") 2)
                 (Rental. (ChildrensMovie. "Childrens3") 3)]]
    (->> rentals
         (make-statement)
         (format-statement "Tom")
         (print))))
