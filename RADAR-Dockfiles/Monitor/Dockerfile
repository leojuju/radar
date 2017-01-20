FROM tomcat:9
COPY . /usr/local/tomcat/webapps
WORKDIR /usr/local/tomcat/bin
VOLUME ["/logs"]
EXPOSE 8080
CMD ["catalina.sh", "run"]
