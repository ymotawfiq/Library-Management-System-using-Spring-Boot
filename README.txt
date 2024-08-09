This is my implementation of 
(Projects Attachments - Backend Developer
Library Management System using
Spring Boot)

----------------------------------------
Let's start with How to run application |
----------------------------------------
1- Using visual studio code on windows or linux

a- Download and install jdk and jre using Java 21
b- Install and run My SQL Database
c- Create Database with name (library_management_system)
d- From terminal run command (mvn install) to install maven dependancies
e- Finally from terminal run command (mvn spring-boot:run) to run the application

2- Using Eclipse For Java EE 

a- Download and install eclipse on windows or linux 
b- Install and run My SQL Database
c- Create Database with name (library_management_system)
d- Open project in eclipse
e- Right click on project then choose (maven->maven install) to install maven dependencies
f- Go to main class then right click and choose (Run->Run as java application) to run the project

--------
About   |
--------
In brief,
This is a library management system, Firstly i add authentication using JWT,
I created basis authentication system allows user to Register, Login,
Logout, login using two factor authentication, update his information
like user name and full name as well as forget and reset password and 
email verification.
Then i created book service which allow users who has admin authorization
to add, update and delete books.
Then i created borrowing service which allows user to borrow book to 
specefic date.

-------------------------
Swagger API Documentation|
-------------------------
Just run application like mentioned and go to (http://localhost:8080/swagger-ui/index.html#/)

-----------------
API Documentation |
-----------------
Link: http://localhost:8080/api/patron/insert-roles

Parameter: No parameters

Request: Post Request

Response:
{
    "statusCode": 201,
    "data": null,
    "message": "Roles saved successfully",
    "success": true
}

Business: Used only one time to insert roles in database 
==================================================================
Link: http://localhost:8080/api/patron/register

Parameter: RegisterDto

Request: Post Request 
{
    "fullName":"fullName",
    "userName":"userName",
    "email":"email",
    "password":"password"
}

Response:
{
    "statusCode": 201,
    "data": {
        "fullName":"fullName",
        "userName":"userName",
        "email":"email",
        "password":"password",
        "id":"id"
    },
    "message": "Registered successfully check your inbox to confirm your email",
    "success": true
}

Business: Used to create account and send email confirmation message
==================================================================
Link: http://localhost:8080/api/patron/confirm-email?token=token&userName=userName

Parameter: token, userName

Request: Get Request 

Response:
{
  "statusCode": 200,
  "data": null,
  "message": "Email verified successfully",
  "success": true
}

Business: Used to confirm email
==================================================================
Link: http://localhost:8080/api/patron/login

Parameter: LoginDto

Request: Post Request 
{
    "userNameOrEmail":"userNameOrEmail",
    "password":"password"
}

Response:
if two factor authentication disabled
{
    "statusCode": 200,
    "data": {
        "token": "jwt token",
        "type": "Barer token"
    },
    "message": "Token generated successfully",
    "success": true
}
if two factor authentication enabled
{
    "statusCode": 200,
    "data": null,
    "message": "Check your inbox to get two factor code",
    "success": true
}

Business: Used to login and generate jwt token
==================================================================
Link: http://localhost:8080/api/patron/me

Parameter: No parameters

Auth: Barer token

Request: Get Request 

Response:
{
    "statusCode": 200,
    "data": {
        "fullName": "fullName",
        "id": "id",
        "userName": "userName",
        "email": "email"
    },
    "message": "User found successfully",
    "success": true
}

Business: Used to get context user information
==================================================================
Link: http://localhost:8080/api/patron/enable-two-factor

Parameter: No parameters

Auth: Barer token

Request: Put Request 

Response:
{
    "statusCode": 200,
    "data": null,
    "message": "Two factor enabled successfully",
    "success": true
}

Business: Used to enable two factor authentication
==================================================================
Link: http://localhost:8080/api/patron/disable-two-factor

Parameter: No parameters

Auth: Barer token

Request: Put Request 

Response:
{
    "statusCode": 200,
    "data": null,
    "message": "Two factor disable successfully",
    "success": true
}

Business: Used to disable two factor authentication
==================================================================
Link: http://localhost:8080/api/patron/2FA-login

Parameter: TwoFactorLoginDto

Request: Post Request 
{
    "email":"email",
    "code":code
}

{
    "statusCode": 200,
    "data": {
        "token": "jwt token",
        "type": "Barer token"
    },
    "message": "Token generated successfully",
    "success": true
}

Business: Used to login using two factor authentication
==================================================================
Link: http://localhost:8080/api/patron/logout

Parameter: No parameters

Auth: Barer token

Request: Post Request 

Response:
{
    "statusCode": 201,
    "success": true,
    "data": null,
    "message": "Logged out successfully"
}
Business: Used to kill token and logout
==================================================================
Link: http://localhost:8080/api/patron/update-username

Parameter: UpdateUserNameDto

Auth: Barer token

Request: Put Request 
{
    "userName":"userName"
}

Response:
{
    "statusCode": 200,
    "message": "User Name updated successfully",
    "data": {
        "id": "id",
        "fullName": "fullName",
        "userName": "userName",
        "email": "email"
    },
    "success": true
}

Business: Used to update user name
==================================================================
Link: http://localhost:8080/api/patron/send-email-verification-link?email=email

Parameter: email

Request: Get Request 

Response:
{
    "statusCode": 200,
    "message": "Check your inbox",
    "data": null,
    "success": true
}

Business: Used to resend email confirmation link
==================================================================
Link: http://localhost:8080/api/patron/update-fullname

Parameter: UpdateFullNameDto

Auth: Barer token

Request: Put Request 
{
    "fullName":"fullName"
}

Response:
{
    "statusCode": 200,
    "message": "Full name updated successfully",
    "data": {
        "id": "id",
        "fullName": "fullName",
        "email": "email",
        "userName": "userName"
    },
    "success": true
}

Business: Used to update user name
==================================================================
Link: http://localhost:8080/api/patron/forget-password

Parameter: ForgetPasswordDto

Request: Post Request 
{
    "email": "ymtawfiq2003@gmail.com"
}

Response:
{
    "statusCode": 200,
    "message": "Check your inbox to get reset password code",
    "data": null,
    "success": true
}

Business: Used to send forget password code via email
==================================================================
Link: http://localhost:8080/api/patron/reset-password

Parameter: ResetPasswordDto

Request: Put Request 
{
    "email": "email",
    "code": "code",
    "newPassword":"newPassword",
    "confirmPassword":"confirmPassword"
}

Response:
{
    "statusCode": 200,
    "message": "Password reset successfully",
    "data": null,
    "success": true
}
Business: Used to send forget password code via email
==================================================================
Link: http://localhost:8080/api/books

Parameter: No parameters

Request: Get Request 

Response:
{
    "statusCode": 200,
    "message": "Books found successfully",
    "data": {},
    "success": true
}
Business: Used to fetch all books
==================================================================
Link: http://localhost:8080/api/books

Parameter: AddBookDto-(Form data)

Auth: Barer token

Request: Post Request 
title:title
author:author
publicationYear:publicationYear
isbn:isbn
images:(multi images for one book)

Response:
{
    "statusCode": 201,
    "message": "Book saved successfully",
    "data": null,
    "success": true
}
Business: Used to add books to database
==================================================================
Link: http://localhost:8080/api/books/update-book

Parameter: UpdateBookDto

Auth: Barer token

Request: Put Request 
{
    "title":"title",
    "author":"author",
    "publicationYear": publicationYear,
    "isbn":"isbn",
    "id": "id"
}
Response:
{
    "statusCode": 200,
    "message": "Book updated successfully",
    "data": {
        "id": "id",
        "author": "author",
        "isbn": "isbn",
        "title": "title",
        "publicationYear": "publicationYear",
        "bookImages": [
            {
                "id": "id",
                "imagePath": "imagePath",
                "isDefault": isDefault,
                "createdAt": createdAt
            }
        ]
    },
    "success": true
}
Business: Used to update book info
==================================================================
Link: http://localhost:8080/api/books/update-book-title

Parameter: UpdateBookTitleDto

Auth: Barer token

Request: Put Request 
{
    "title":"title",
    "id": "id"
}
Response:
{
    "statusCode": 200,
    "message": "Book title updated successfully",
    "data": {
        "id": "id",
        "author": "author",
        "isbn": "isbn",
        "title": "title",
        "publicationYear": "publicationYear",
        "bookImages": [
            {
                "id": "id",
                "imagePath": "imagePath",
                "isDefault": isDefault,
                "createdAt": createdAt
            }
        ]
    },
    "success": true
}
Business: Used to update book title
==================================================================
Link: http://localhost:8080/api/books/update-book-author

Parameter: UpdateBookTitleDto

Auth: Barer token

Request: Put Request 
{
    "author": "author",
    "id": "id"
}
Response:
{
    "statusCode": 200,
    "message": "Book author updated successfully",
    "data": {
        "id": "id",
        "author": "author",
        "isbn": "isbn",
        "title": "title",
        "publicationYear": "publicationYear",
        "bookImages": [
            {
                "id": "id",
                "imagePath": "imagePath",
                "isDefault": isDefault,
                "createdAt": createdAt
            }
        ]
    },
    "success": true
}
Business: Used to update book author
==================================================================
Link: http://localhost:8080/api/books/update-book-isbn

Parameter: UpdateBookIsbnDto

Auth: Barer token

Request: Put Request 
{
    "isbn": "isbn",
    "id": "id"
}
Response:
{
    "statusCode": 200,
    "message": "Book isbn updated successfully",
    "data": {
        "id": "id",
        "author": "author",
        "isbn": "isbn",
        "title": "title",
        "publicationYear": "publicationYear",
        "bookImages": [
            {
                "id": "id",
                "imagePath": "imagePath",
                "isDefault": isDefault,
                "createdAt": createdAt
            }
        ]
    },
    "success": true
}
Business: Used to update book isbn
==================================================================
Link: http://localhost:8080/api/books/find-by-id/{bookId}

Parameter: bookId

Request: Get Request 

Response:
{
    "statusCode": 200,
    "message": "book found successfully",
    "data": {
        "id": "id",
        "author": "author",
        "isbn": "isbn",
        "title": "title",
        "publicationYear": "publicationYear",
        "bookImages": [
            {
                "id": "id",
                "imagePath": "imagePath",
                "isDefault": isDefault,
                "createdAt": createdAt
            }
        ]
    },
    "success": true
}
Business: Used to find book by id
==================================================================
Link: http://localhost:8080/api/books/find-by-isbn/{isbn}

Parameter: isbn

Request: Get Request 

Response:
{
    "statusCode": 200,
    "message": "book found successfully",
    "data": {
        "id": "id",
        "author": "author",
        "isbn": "isbn",
        "title": "title",
        "publicationYear": "publicationYear",
        "bookImages": [
            {
                "id": "id",
                "imagePath": "imagePath",
                "isDefault": isDefault,
                "createdAt": createdAt
            }
        ]
    },
    "success": true
}
Business: Used to find book by isbn
==================================================================
Link: http://localhost:8080/api/books/delete-by-isbn/{isbn}

Parameter: isbn

Request: Delete Request 

Response:
{
    "statusCode": 204,
    "message": "book deleted successfully",
    "data": null,
    "success": true
}
Business: Used to delete book by isbn
==================================================================
Link: http://localhost:8080/api/books/delete-by-id/{id}

Parameter: isbn

Request: Delete Request 

Response:
{
    "statusCode": 204,
    "message": "book deleted successfully",
    "data": null,
    "success": true
}
Business: Used to delete book by id
==================================================================
Link: http://localhost:8080/api/books/delete-by-isbn/{isbn}

Parameter: isbn

Request: Delete Request 

Response:
{
    "statusCode": 204,
    "message": "book deleted successfully",
    "data": null,
    "success": true
}
Business: Used to delete book by isbn
==================================================================
Link: http://localhost:8080/api/books/{bookId}/{imgId}

Parameter: bookId, imgId

Request: Put Request 

Response:
{
    "statusCode": 200,
    "message": "Image set default successfully",
    "data": null,
    "success": true
}
Business: Used to set default photo for book
==================================================================
Link: http://localhost:8080/api/borrow

Parameter: BorrowBookDto

Auth: Barer token

Request: Post Request 
{
    "bookId":"9493b19b-57b6-4a8e-9923-6e72aacf6ad8"
}

Response:
{
    "statusCode": 201,
    "message": "Borrowed successfully",
    "data": null,
    "success": true
}
Business: Used to borrow book
==================================================================
Link: http://localhost:8080/api/borrow

Parameter: No parameters

Auth: Barer token

Request: Get Request 

Response:
{
    "statusCode": 200,
    "message": "Borrowed books found successfully",
    "data": borrowed books,
    "success": true
}
Business: Used to get current user borrowed books
==================================================================
Link: http://localhost:8080/api/borrow/{bookId}

Parameter: bookId

Auth: Barer token

Request: Get Request 

Response:
{
    "statusCode": 200,
    "message": "Patrons found successfully",
    "data": patrons,
    "success": true
}
Business: Used to get book patrons
==================================================================
Link: http://localhost:8080/api/borrow/{bookId}

Parameter: bookId

Auth: Barer token

Request: Delete Request 

Response:
{
    "statusCode": 204,
    "message": "Un Borrowed successfully",
    "data": null,
    "success": true
}
Business: Used to un borrow book
==================================================================