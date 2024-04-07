FROM ubuntu:23.10 AS build
RUN apt-get update
RUN apt-get install build-essential -y
RUN apt-get install libz-dev -y
RUN rm /bin/sh && ln -s /bin/bash /bin/sh

# install sdkman
RUN apt-get -qq -y install curl wget unzip zip
RUN curl -s "https://get.sdkman.io" | bash
RUN source "$HOME/.sdkman/bin/sdkman-init.sh" \
    && sdk install java 22.3.r17-nik \
    && sdk install maven 3.9.3

WORKDIR buildspace
COPY ./src src
COPY ./pom.xml .

ENV MAVEN_HOME="/root/.sdkman/candidates/maven/current"
ENV JAVA_HOME="/root/.sdkman/candidates/java/current"
ENV PATH="$MAVEN_HOME/bin:$JAVA_HOME/bin:$PATH"
RUN mvn -Pnative native:compile -X

# Stage 2: Package into a Docker image
FROM debian:trixie-slim AS runtime
RUN apt update && apt install libfreetype-dev -y
WORKDIR /workspace
COPY --from=build /buildspace/target/trainticket .
CMD ["./trainticket"]