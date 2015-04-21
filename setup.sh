#!/bin/sh
PROJECT_DIR="/opt/project"


setupEnvironment(){
	printf "Check Environment setting ...\n"

	printf "Check maven installed ... \n"
		sudo apt-get install maven;

	printf "Check git installed ... \n"
		sudo apt-get install git;

	printf "Check openjdk-7-jdk installed ... \n"
		sudo apt-get install openjdk-7-jdk;

	printf "Check nginx installed ... \n"
		sudo apt-get install nginx;


	printf "Environment setting ... OK\n"

	printf "Install project ...\n"
	if [ ! -d "/opt" ]; then
		sudo mkdir /opt
	fi
	if [ ! -d "/opt/project" ]; then
		sudo mkdir /opt/project
	fi

	build
}

build(){
	printf "Checnk project directory ... OK\n"
	printf "Sync project ... \n"
	
	if [ ! -d "$PROJECT_DIR/MultiDBs-KeywordSearchG-Server" ]; then
		(cd $PROJECT_DIR && git clone https://github.com/infsci2711/MultiDBs-KeywordSearchG-Server.git)
	fi
	if [ ! -d "$PROJECT_DIR/MultiDBs-KeywordSearchG-WebCleint" ]; then
		(cd $PROJECT_DIR && git clone https://github.com/infsci2711/MultiDBs-KeywordSearchG-WebCleint.git)
	fi
	if [ ! -d "$PROJECT_DIR/MultiDBs-Utils" ]; then
		(cd $PROJECT_DIR && git clone https://github.com/infsci2711/MultiDBs-Utils.git)
	fi

	(cd "$PROJECT_DIR/MultiDBs-Utils" && git pull)
	printf "update MultiDBs-Utils ... OK\n"
	printf "build MultiDBs-Utils ...\n"
	(cd "$PROJECT_DIR/MultiDBs-Utils" && mvn install )
	printf "build MultiDBs-Utils ... OK\n"
	
	(cd "$PROJECT_DIR/MultiDBs-KeywordSearchG-Server" && git pull )
	printf "update MultiDBs-KeywordSearchG-Server ... OK\n"
	printf "build MultiDBs-KeywordSearchG-Server ...\n"
	(cd "$PROJECT_DIR/MultiDBs-KeywordSearchG-Server" && mvn install )
	printf "build MultiDBs-KeywordSearchG-Server ... OK\n"

	(cd "$PROJECT_DIR/MultiDBs-KeywordSearchG-WebCleint" && git pull)
	printf "update MultiDBs-KeywordSearchG-WebCleint ... OK\n"
	printf "build MultiDBs-KeywordSearchG-WebClient ...\n"
	(cd "/usr/share/nginx" && sudo rm html && sudo ln -s "$PROJECT_DIR/MultiDBs-KeywordSearchG-WebCleint" html  )
	printf "build MultiDBs-KeywordSearchG-WebClient ... OK\n"

}

startServer() {
	printf "Check socket ... \n"
	kill -9 $(ps aux | grep java | grep multidbskeywordsearchgserverapi-0.1-SNAPSHOT.jar | awk '{print $2}')
	printf "Check socket ... OK\n"

	printf "Start server ... \n"
	cd /opt/project/MultiDBs-KeywordSearchG-Server/MultiDBsKeywordSearchGServerAPI
	
	nohup java -jar target/multidbskeywordsearchgserverapi-0.1-SNAPSHOT.jar > log.out 2> error.log < /dev/null &


}



case $1 in
  install)
    setupEnvironment
    exit 0
    ;;
  start)
    startServer
    exit 0;;
  rebuild)
    build
    exit 0;;
  *)
    echo "Usage: setup.sh {install|start|rebuild}"
    echo "    intall: install required package and build project"
    echo "    rebuild: build/rebuild projects"
    echo "    start: start server"
    exit 0;;
esac

exit $?
