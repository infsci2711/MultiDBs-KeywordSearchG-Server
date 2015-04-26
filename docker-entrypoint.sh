#!/bin/bash
set -e

echo "creating folder for servers source code"

cd /opt
mkdir -p project
cd project

echo "cloning two git repos"

git clone https://github.com/infsci2711/MultiDBs-KeywordSearchG-Server.git
git clone https://github.com/infsci2711/MultiDBs-Utils.git

echo "building utils project"

cd /opt/project/MultiDBs-Utils
mvn install

echo "building server project"

cd /opt/project/MultiDBs-KeywordSearchG-Server
mvn install

echo "creating folder for deployed code"

cd /opt/project
mkdir -p deployed

echo "copying jar and config file to deploed folder"

cp /opt/project/MultiDBs-KeywordSearchG-Server/MultiDBsKeywordSearchGServerAPI/target/multidbskeywordsearchgserverapi-0.1-SNAPSHOT.jar /opt/project/deployed
cp /opt/project/MultiDBs-KeywordSearchG-Server/MultiDBsKeywordSearchGServerAPI/src/main/resources/config.properties /opt/project/deployed

echo "starting java server"

nohup java -jar  /opt/project/deployed/multidbskeywordsearchgserverapi-0.1-SNAPSHOT.jar /opt/project/deployed/config.properties > /opt/project/deployed/log.out 2> /opt/project/deployed/error.log < /dev/null &

exec "$@"