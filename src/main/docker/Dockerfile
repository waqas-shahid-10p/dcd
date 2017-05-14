FROM ubuntu:14.04

#changing repos
RUN sed -i 's|http://archive.ubuntu.com|http://la-mirrors.evowise.com|g' /etc/apt/sources.list

RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y  software-properties-common && \
    add-apt-repository ppa:webupd8team/java -y && \
    apt-get update && \
    echo oracle-java7-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections && \
    apt-get install -y oracle-java8-installer && \
    apt-get clean Install Java.

ADD deadcode-detection-0.1.0.jar app.jar
# Install Scitools
RUN wget http://builds.scitools.com/all_builds/b844/Understand/Understand-4.0.844-Linux-64bit.tgz && tar -zxvf Understand-4.0.844-Linux-64bit.tgz
RUN echo "Server: scitools-license.devfactory.com 00000000 9000" > scitools/conf/license/locallicense.dat

RUN apt-get install libxrender1 -y
RUN apt-get install libxi6 libgconf-2-4 -y
RUN apt-get install libxtst6 -y

# Define commonly used JAVA_HOME variable
ENV JAVA_HOME /usr/lib/jvm/java-8-oracle
ENV JAVA_OPTS=""
ENV LD_LIBRARY_PATH /usr/lib/jvm/java-8-oracle/jre/lib/amd64:/scitools/bin/linux64/Java:/scitools/bin/linux64
ENV SCITOOLS_HOME /scitools/
ENV SCITOOLS_BIN /scitools/bin/linux64
ENV REPO_FOLDER /repositories
ENV PATH "${SCITOOLS_BIN}:${PATH}"

#Run the app
RUN sh -c 'touch /app.jar'
EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]