FROM tomcat:8

MAINTAINER montero.gonzalo90@gmail.com

COPY target/*.war $CATALINA_HOME/webapps/
RUN sh -c 'touch $CATALINA_HOME/webapps/diff-search-service.war'
