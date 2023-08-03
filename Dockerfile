FROM ballerina/jre11:v1
RUN mkdir /opt/log
VOLUME ["/opt/log","/opt/log"]
COPY party-server/target/party-server-0.0.1-SNAPSHOT.jar /opt/project/imparty.jar
#COPY server.xml /usr/local/tomcat/conf/server.xml
#COPY web.xml /usr/local/tomcat/conf/web.xml
WORKDIR /opt
CMD ["java", "-jar", "/opt/project/imparty.jar", "-Dprofile.active=docker", ">> /opt/log/run-$date.log"]