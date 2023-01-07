# news_focused

[![wakatime](https://wakatime.com/badge/github/ragutislt/newsfocused.svg)](https://wakatime.com/badge/github/ragutislt/newsfocused) (time spent working on the repository)

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
6. Jsoup (for html response parsing)
7. MailCatcher (for e2e tests)
8. Jersey (for websites)
9. Spring and Spring boot (for websites)

## Requirements still left to implement (constantly changing list)
* ~~send emails from own domain~~ DONE
* configure automatic building and deployment
* front website for clients to register and configure their preferences
* configurable properties
    - ~~email address~~ DONE
    - ~~which days to send emails~~ DONE
    - what time of the day to send email
    - when the batch runs?
    - number of headlines per site
    - ~~where data is stored~~ DONE
    - ~~smtp server details~~ DONE
* scheduling when to send the email (for each client, lower priority, would need many resources)
* Reading sites should have some throttle to not ddos the sites (sleep between X requests, make configurable)
* ~~batch users - we shouldn't have one java process per user~~ DONE
    property format?
        store in one json file?
* User a business key for ideantifying users instead of email? (would help if email changes, we don't lose all history; but is it really needed?)
    * Would help also with deleting CIDs? could keep the id. Also would help with debugging/tracking
    * Can also help with forming URLs - the GET /user/:id is better than GET/user/:email (encoding, url spoofing, etc...)
* ~~after sending email, clear running list~~ DONE
    
    Here don't clean data right away, instead keep the 'old' week/period and just save new headlines in the new week/period

### Non-Functional requirements
* ~~Integrate in AWS, use lambda to run~~ DONE
* Run integration tests in AWS every day against each different site registered in the system, send email with results
* * Things to test: correctly parsed headlines (can easily check email format), correct encoding (easily checked again), whether email even exists

#### Planned gitops
When happy with dev, do a release manually (later, have a pipeline to click a button which will do a release)
Using githooks and aws, when a new release is detected, automatically pull it in aws and update the application for the next batch run

## How to run
1. Compile
2. Run using
```
docker run --rm \
    -e usersLocation="/usr/newsfocused/config/users.json" \
    -e repoLocation="/usr/newsfocused/config/repo" \
    -e emailPropertiesFile="/usr/newsfocused/config/emailProtocol.properties" \
    -v /some_folder_with_properties_files/newsfocused:/usr/newsfocused/config newsfocused:${version}
```
Parameters are:<br/>
    1. usersLocation - where user settings are stored (in json)<br/>
    2. repoLocation - repository location in the file system (file name)<br/>
    3. emailPropertiesFile - Email properties file (smtp)<br/>


See tests for sample properties files
