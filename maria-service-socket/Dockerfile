#
#    Copyright 2023 the original author, Lam Tong
#
#    Licensed under the Apache License, Version 2.0 (the "License");
#    you may not use this file except in compliance with the License.
#    You may obtain a copy of the License at
#
#          http://www.apache.org/licenses/LICENSE-2.0
#
#    Unless required by applicable law or agreed to in writing, software
#    distributed under the License is distributed on an "AS IS" BASIS,
#    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#    See the License for the specific language governing permissions and
#    limitations under the License.
#

FROM openjdk:8

LABEL service="maria-service-socket" version="0.0.1" email="lemonlovepy@163.com" author="Lam Tong"

ARG BASE_PATH=/home/deploy

COPY target/maria-service-socket-0.0.1.jar $BASE_PATH/app.jar
COPY script/run-java.sh $BASE_PATH/run-java.sh

RUN ["chmod", "+x", "/home/deploy/run-java.sh"]

WORKDIR $BASE_PATH

ENV LANG=C.UTF-8 SERVER_PORT=8080 JAVA_OPTIONS="-server"

ENTRYPOINT ["/home/deploy/run-java.sh"]