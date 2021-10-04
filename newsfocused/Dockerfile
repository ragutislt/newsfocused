FROM openjdk:18-alpine

RUN "mkdir" "/usr/newsfocused"
RUN "mkdir" "/usr/newsfocused/config"
RUN "mkdir" "/usr/newsfocused/repo" # to be externalized as well
COPY target/newsfocused-*.jar /usr/newsfocused/newsfocused.jar
WORKDIR /usr/newsfocused
ENV siteFile="/sites.txt"
ENV email "/mail@newsfocused.com"
ENV daysToSendOn "Monday,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday"
ENV repoFile "/repo/headlines.json"
ENV emailPropertiesFile "/emailProtocol.properties"

CMD java -jar /usr/newsfocused/newsfocused.jar ${siteFile} ${email} ${daysToSendOn} ${repoFile} ${emailPropertiesFile}