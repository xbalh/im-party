FROM ballerina/jre11:v1
RUN mkdir /opt/log
ARG databaseUrl
ARG databaseUser=root
ARG databasePassword
ARG musicApiHost
VOLUME ["/opt/log","/opt/log"]
COPY party-server/target/party-server-0.0.1-SNAPSHOT.jar /opt/project/imparty.jar
#COPY server.xml /usr/local/tomcat/conf/server.xml
#COPY web.xml /usr/local/tomcat/conf/web.xml
WORKDIR /bin
ENTRYPOINT ["java", "-jar", "/opt/project/imparty.jar", "-DdatabaseUser=$databaseUser", "-DdatabaseUrl=$databaseUrl", "-DdatabasePassword=$databasePassword", "-DneteaseCloudMusicApiUrl=$musicApiHost", ">> /opt/log/run-$date.log"]