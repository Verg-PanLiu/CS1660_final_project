FROM openjdk:12-oraclelinux7

RUN yum -y install \
    libXi \
   libXrender

RUN yum -y install libXtst
COPY . /usr/app/
WORKDIR /usr/app/

RUN javac UserInterface.java

ENTRYPOINT java UserInterface