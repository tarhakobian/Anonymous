# Anonymous Posting Module

This module provides functionality for managing anonymous posts, comments,
and users in a school-related system, currently only at GCC CA. It allows users to create,
edit, and delete posts, comment on posts, and interact with other
users' posts through likes and replies. The module also includes role-based user
authentication and authorization features.

## Features

- **User Management:** Users can register, log in, log out and manage their accounts via activating or deactivating.
- **Post Management:** Users can view (cached), create, edit, and delete posts. Posts can be made public, also sharing the
  username of the publisher.
- **Commenting:** Users can comment on posts, and comments can be replied to.
- **Likes:** Users can like posts and comments.
- **Frames:** Every post contains of two parts: frame and content. Frames are stored in S3, and provided to the user to choose in
  process of making a new post.
- **Security:** JWT based security measures are in place to ensure that users can only perform actions authorized for
  their role.
- **Admin Features:** Admin users have additional privileges, such as viewing and managing all posts and comments.

## Technologies Used

- **Java:** Version : 17.
- **Spring Boot:** Version : 3.1.5.
- **Spring Security:** (ROLE based JWT) for user authentication and authorization.
- **Spring Data JPA**
- **Amazon S3:** for storing post images and frames.
- **PostgresSQL**
- **Flyway:** for the database version controlling.
- **Docker** 

## Getting Started

1. Clone the repository
2. Configure application.yml setting your serer, database and S3 properties.
3. Run : docker-compose up -d
4. Run : mvn clean package.
5. Access the application in your web browser or Postman.

## Contributors

- [Taron Hakobyan](https://www.linkedin.com/in/tarhakobian)


