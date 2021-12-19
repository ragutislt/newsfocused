FROM openjdk:18-alpine

#RUN "mkdir" "/usr/newsfocused"
#RUN "mkdir" "/usr/newsfocused/config"
#RUN "mkdir" "/usr/newsfocused/repo" # to be externalized as well
COPY target/newsfocused-*.jar /usr/newsfocused/newsfocused.jar
WORKDIR /usr/newsfocused

ENV usersLocation="/users.json"
ENV repoLocation "/repo"
ENV emailPropertiesFile "/emailProtocol.properties"

#CMD echo "root dir content:" && ls / && echo && echo "/ncurrent dir:" && pwd && echo && echo "/nuser dir content:" && ls .

ENTRYPOINT java --add-opens java.base/java.time=ALL-UNNAMED -jar /usr/newsfocused/newsfocused.jar ${usersLocation} ${repoLocation} ${emailPropertiesFile}

#ENTRYPOINT ["tail", "-f", "/dev/null"]