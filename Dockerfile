FROM ballerina/jre11
COPY party-server/target/party-server-0.0.1-SNAPSHOT.jar /opt/project/imparty.jar
#COPY server.xml /usr/local/tomcat/conf/server.xml
#COPY web.xml /usr/local/tomcat/conf/web.xml
#WORKDIR /usr/local/tomcat/bin
ENTRYPOINT ["java", "-jar", "/opt/project/imparty.jar"]