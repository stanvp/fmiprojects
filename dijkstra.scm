; Dijkstra algorithm implementation in Scheme R5RS
; For more information see http://en.wikipedia.org/wiki/Dijkstra's_algorithm and http://www.schemers.org/Documents/Standards/R5RS/
; Example
; > (dijkstra G1 'b)
; ((a . 7.0) (b . 0.0) (c . 12.0) (d . 15.0) (e . 21.0) (f . 10.0))

(define G1 '(
             (a . ((b . 7) (f . 9) (c . 14)))
             (b . ((a . 7) (d . 15) (f . 10)))
             (c . ((a . 14) (f . 2) (e . 9)))
             (d . ((b . 15) (f . 11) (e . 6)))
             (e . ((c . 9) (d . 6)))  
             (f . ((c . 2) (d . 11) (b . 10) (a . 9)))
             ))

(define (foldr f z xs)
  (if (null? xs)
      z
      (f (car xs) (foldr f z (cdr xs)))))

(define (init graph val)
  (map (lambda (n) (cons (car n) val)) graph)
  )

(define (hash-set hash key val)
  (map (lambda (n) (if (eq? (car n) key) (cons key val) n)) hash)
  )

(define (hash-ref hash key default) 
  (cond 
    ((null? hash) default)
    ((eqv? (car (car hash)) key) (cdr (car hash)))
    (else (hash-ref (cdr hash) key default))
    )
  )

(define (delete item list)
  (cond
    ((null? list) list)
    ((equal? item (car list)) (cdr list))
    (else (cons (car list) (delete item (cdr list)))))  
  )

(define (vertices graph)
  (map (lambda (n) (car n)) graph)
  )

(define (min-cons-cdr x y)
  (if (> (cdr x) (cdr y)) y x)
  )

(define (min-vertex Q dist)   
  (car (foldr min-cons-cdr (cons '() +inf.0) (map (lambda (x) (cons x (hash-ref dist x +inf.0))) Q)))
  )

(define (distance u v graph)
  (hash-ref (hash-ref graph u '()) v #f)
  )

(define (dijkstra graph source)
  (define (update-dist u x dist)
    (let* ((v (car x))
           (d (distance u v graph))
           )
      (if d
          (cons v (min (hash-ref dist v +inf.0) (+ d (hash-ref dist u +inf.0))))
          x
          )
      )
    )
  
  (define (tail Q dist)
    (cond
      ((null? Q) dist)
      (else (let* ((u (min-vertex Q dist))
                   (Qu (delete u Q)))
              (if (= +inf.0 (hash-ref dist u +inf.0)) 
                  dist                 
                  (tail Qu (map (lambda (x) (update-dist u x dist)) dist)))                  
              ))
      ))
  
  (tail (vertices graph) (hash-set (init graph +inf.0) source 0))
  )