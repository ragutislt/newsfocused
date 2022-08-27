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

* Admin - aggregate root. Performs operating associated to a user, like registering new users, modifying them, viewing them...
* User - an entity
* Site - a value object

# Architecture Decision Record

## ADR-1: Choice of architecture for an admin application
### Issue
An admin site could really be considered just a CRUD application. Or is it more than that?
Therefore, once we answer that question, we can choose an architecture that better supports it, is easier to get up and running, and easier to maintain/evolve in the long term.
### Considered alternatives
1. It's a CRUD application - opt for a very simple architecture, like a https://martinfowler.com/eaaCatalog/transactionScript.html.
We could use here, for example, Spring Data Rest, to have mostly a procedural application.
<br><br>Upsides: Easy to get going with, there would probably be less code in the end to maintain.
<br><br>Downsides: we never know the full complexity of the application until after it is built. I suspect if we opted for this approach, it would be harder to maintain since the 'business' of the application will outgrow the simple procedural architecture.<br><br>
2. It's a proper business application with a clear domain. In this case a more complex approach makes sense, like Domain-Driven-Design (DDD).
<br><br>Upsides: As the business (domain) grows, we get new requirements, evolutions, etc... it can become difficult to maintain a procedural application and not end up with it becoming a big ball of mud. DDD helps to flush out a clear domain which is highly maintainable and readable.
<br><br>Downsides: It is more difficult to get started with. We need more analysis at the beginning to discover the entities within our domain. It might also be costly later if we identify our domain entities incorrectly. Last but not least, it requires a bit more effort to avoid any infrastructure-related code or dependencies being used in our domain.
### Decision
2022-08-27 - As I'm not sure that the domain of the admin app will not evolve much in the future, I think it is more valuable in the long-term to go with an architecture based on DDD. It will also be good from a personal growth point of view - I can learn and practice more of DDD patterns.
### Consequences
To be filled later...

---