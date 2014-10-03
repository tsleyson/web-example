(ns web-example.web
  (:require
   [net.cgrand.enlive-html :as enlive]
   [clojure.java.io :refer [file reader writer]]))


(enlive/deftemplate page-template (file "resources/public/page.html")
  [greeting]
  [:h1] (enlive/content greeting))

;; Since I'm an idiot and forgot about how Enlive works, know this:
;; If clone-for complains that it can't change a lazy-seq into an IFn,
;; it's because it expects a function as its argument. You can use fn,
;; or use do-> or content, append, etc. to give you a suitable function.
(enlive/defsnippet post-list (file "resources/public/post_list.html") [:.post]
  [post]
  [:.post-header] (enlive/content (str (:auth_name post) " on " (:post_date post)))
  [:.post-text] (enlive/clone-for [para (-> post
                                            :post_text
                                            (clojure.string/split #"\n{2,}"))]
                           [:.post-text] (enlive/content para)))
;; Note: there's a library markdown-clj that parses Markdown.
;; Also, look into MathJax.

(enlive/deftemplate front-page (file "resources/public/front_page.html")
  [posts]
  [:body :.container :.post_container]
  (enlive/clone-for [post posts]
                    (enlive/append (post-list post))))

(defn render-template
  [template & args]
  (apply str (apply template args)))

