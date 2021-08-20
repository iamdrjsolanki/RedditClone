# RedditClone

Clone of Reddit Web App.

	- User SignUp and SignIn and SSO pluggable application.
	- Tech Stack
		- Java 8
		- Maven
		- Spring Boot 2.4.5
		- MySQL
		- Angular
	- Functionalities
		- User Registration.
		- User Account activation.
		- User Login.
		- User JWToken for authenticating requests onto backend.
		- User can see all the posts, whether logged in or not.
		- Post login, User can create a subreddit.
		- Post login, User can create a post under a subreddit.
		- User profile, details of posts created and all the comments.
		- User logout.
 

## SMTP Mail Configuration
1. Go to mailtrap.io to create dummy SMTP server.
2. It will create an inbox after sigining up.
3. Go to settings and select integrations as JAVA.
4. Configure the given host, port, username, password and also protocol as smtp.


## Sping Security Configuration
	- Use the Client ID & Client Secret in the application.properties file to configure.
	- Created a service.
	- RedditClone
		- User registration & login. Runs on 8080 port.
		- Registers the user and stores into the database.
		- Generates a Private key while login & public key for authenticating incoming requests.
		- Functionality of refreshing the token after the expiration is added. User can get the fresh new token if the older is expired.
	- Libraries Added:
		- Spring Security, JPA, Mail, Validation, MySQL Connector
			- To give registration, login, logout & Json web token functionality.
			- To perform database operations.
			- To send the account activation link to user.
			- To validate entity fields.
			- MySQL driver to connect to the DB.
			- 
		- jsonwebtoken
			- To create json web tokens.
		- MapStruct library 
			- To create mappings from DTO to Beans and vice versa.
		- lombok.jar 
			= To the dependencies to create getters, setters, constructors at compile time.
		- marlonlom library
			- To calculate the time ago from the created date time field.
		- springfox
			- To create API documentation with Swagger 2.
			- Link to access the api-docs http://localhost:8080/reddit-service/swagger-ui.html
 

## User Inteface
	- UI has been created using Angular.
	- The Angular appliaction runs on 4200 port.
	- Libraries Added:
		- @ng-bootstrap/ng-bootstrap
			- To implement a bootstrap designed dropdown for user profile and logout functionality (post login).
		- @tinymce/tinymce-angular
			- To implement HTML enable editor to create POST & SUBREDDIT.
		- @fortawesome/angular-fontawesome
			- To implement the icons of COMMENT, UPVOTE & DOWNVOTE
		- ngx-toastr
			- To display toast messages for REGISTRATION, LOGIN & ACCESS DENIED.
		- ngx-webstorage
			- To save the user token and other info onto local browser, so they could be intercepted with every outgoing requests.
	- The application saves the JWToken on the local storage and retrieves it from there to intercept every request with that JWToken.
	- The interceptor is implemented using HTTP_INTERCEPTOR, and also intercepts only post requests.
	- The interceptor also sends the request to backend server to get the refreshed token if the existing token throws a HttpErrorResponse error.
	- The interceptor also resends the request after fetching the refreshed/updated token.
  

## Future applications
	- This project only has some functionalities out of all the different and vast functionalities Reddit offers.
	- Please contribute to the project. Feel free to do any modifications or enhancements.


