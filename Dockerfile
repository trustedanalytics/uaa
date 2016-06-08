FROM tap-base-java:java8-jessie

COPY uaa/build/libs/cloudfoundry-identity-uaa-*.war .

ENV TOMCAT_VERSION=7.0.69
ENV CATALINA_HOME=apache-tomcat-${TOMCAT_VERSION}

RUN TOMCAT_TAR=${CATALINA_HOME}.tar.gz \
	&& wget http://www-eu.apache.org/dist/tomcat/tomcat-7/v${TOMCAT_VERSION}/bin/${TOMCAT_TAR} \
	&& tar -xvf ${TOMCAT_TAR} && rm ${TOMCAT_TAR} \
	&& rm -r ${CATALINA_HOME}/webapps/* \
	&& mv cloudfoundry-identity-uaa-*.war ${CATALINA_HOME}/webapps/ROOT.war

EXPOSE 8080

CMD $CATALINA_HOME/bin/catalina.sh run
