# MultiDBs-KeywordSearchG-Server
Keyword Search using graph dbs


Description
The goal of this project is by transforming relational datases into neo4j database, we rebuild the datastructure so that when people search for the keyword occurring in the databases, all related result and how the system find the data will all shown on the screen.


1.Steps to set up the project
(1) Download the geyword.sh first on your local machine.
(2) run "geyword.sh"(without double quotation mark).
(3) If you run successfully, you can run "lsof -i:7654", then you can see information of the process.
(4) Now you can see our main webpage on your local machine.


2. To understand the Rest
1) access to previous data
https://github.com/infsci2711/MultiDBs-Query-Server/blob/master/restful-api-brief.txt
2) REST service
The path is http://54.174.121.196:7654/Result/ 
Result in this path represents the query string. 
Output parameter: a json format string like [{"column":"c_name","database":"homework2","record":"Pittsburgh","table":"city"}].
