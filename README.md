# NumberGameImplementation

1) With this implementation, a new custom header (# number-game-id) will be added whenever requesting a new question.

2) The values of the header starts with 0, 1, and so on.

3) While validating the response, client has to send the corresponding header id# that it received previously from the response.

4) If from same client the request comes, then the corresponding generated header value will be used to validate the answer.

5) Corresponding error status code will be sent when the request question corresponding to the client is tampered, or when the response for the sum is wrong.

6) The collection file for the testing on Postman is also attached.
