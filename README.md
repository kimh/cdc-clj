# cdc-clj

Contract Testing boils down to the following steps

### STEP1 Record Expectation
 - write service mock
 - write client test against servie mock
 - record interaction

### STEP2 Verify Expectation
 - read expected request
 - make the real HTTP call with the request
 - save response
 - compare the response with recorded expecation

Let's do above with bare metal clojure code!
