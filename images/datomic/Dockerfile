FROM amazoncorretto:11

ARG DATOMIC_CONFIG
ENV DATOMIC_CONFIG=$DATOMIC_CONFIG

ARG DATOMIC_VERSION=1.0.6726

RUN yum install -y unzip && yum clean all

WORKDIR /opt/
RUN curl -O https://datomic-pro-downloads.s3.amazonaws.com/${DATOMIC_VERSION}/datomic-pro-${DATOMIC_VERSION}.zip
RUN unzip datomic-pro-${DATOMIC_VERSION}.zip
RUN rm datomic-pro-${DATOMIC_VERSION}.zip
RUN mv datomic-pro-${DATOMIC_VERSION} datomic

ENTRYPOINT /opt/datomic/bin/transactor $DATOMIC_CONFIG