# news_focused

## Functionality
1. Runs everyday as a batch
2. Collects headlines from different news sites
3. On a set time, aggregates them and sends an email with those headlines/links
4. Allows for new website addition
5. Runs e2e tests each X time and informs if they fail (by email?)
This avoids breakdown of parsing a site

## Stack
1. Java
2. Maven
3. Junit
4. Mockito
5. Jakarta mail
6. MailCatcher (for e2e tests)

## Requirements still left to implement (constantly changing list)
* configurable properties
    - email address
    - which days to send emails
    - what time of the day to send email
    - when the batch runs?
    - number of headlines per site
    - where data is stored
    - smtp server details
* scheduling when to send the email
* batch users - we shouldn't have one java process per user
    property format?
        store in one json file?
* after sending email, clear running list
    
    Here don't clean data right away, instead keep the 'old' week/period and just save new headlines in the new week/period

### Non-Functional requirements
* Integrate in AWS, use lambda to run
* Run integration tests in AWS every day against each different site registered in the system, send email with results
* * Things to test: correctly parsed headlines (can easily check email format), correct encoding (easily checked again), whether email even exists

#### Planned gitops
When happy with dev, do a release manually (later, have a pipeline to click a button which will do a release)
Using githooks and aws, when a new release is detected, automatically pull it in aws and update the application for the next batch run

## How to run
1. Compile
2. Run using
```
 java -jar newsfocused-1.0.0-SNAPSHOT.jar "sites.txt" "email@email.com" "Monday,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday" "repo\headlines.json" "emailProtocol.properties"
```
Parameters are:<br/>
    1. Sites file<br/>
    2. Email to send to<br/>
    3. Days to send on, ex: "Monday,Friday"<br/>
    4. Repository location in the file system (file name)<br/>
    5. Email properties file<br/>
