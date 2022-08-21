# news_focused admin website

## Functionality
Meant to allow an admin to visualize current users in the system and their configuration.
Additionally, it allows admin rights to change users' preferences or even manually enroll a new user.

## Stack
1. Java
2. Maven
3. Junit
4. Mockito
5. Jersey
6. Spring and Spring boot
7. Spring security
8. DDD (Domain-Driven Design)

## Requirements
* A table of all users in the system. Must have pagination (50 users per page)
* A search bar to search for users (by email). Must be able to detect results on partial search
* When clicking on a user in the table, a details page must open with:
    * User email
    * Registration date
    * Sites subscribed to
    * Nr of headlines wanted
    * Which days of the week they want to receive emails
    * History of emails sent in the past (only dates, but in the future could even return the html)
    * Any other settings that will be implemented in the future
* 'Register New User' button, which will open a page to register a new user. Very similar to the details one except modifiable


# Documentation

## Aggregates

* User
* Site
* Admin
