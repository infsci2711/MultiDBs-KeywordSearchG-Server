# MultiDBs-KeywordSearchG-Server


Description
====
The goal of this project is by transforming relational datases into neo4j database, we rebuild the datastructure so that when people search for the keyword occurring in the databases, all related result and how the system find the data will all shown on the screen.
please visit: http://54.174.121.19

====
1.Steps to set up the project


(1) Download the geyword.sh first on your local machine.

(2) run "geyword.sh"(without double quotation mark) along with following parameter

intall: install required package and build project     

rebuild: build/rebuild projects     

start: start server

(3) If you run successfully, you can run "lsof -i:7654", then you can see information of the process.

(4) Now you can see our main webpage on your local machine.

====
CLIP: METHOD & REST API
====
1. To understand the Method

1) Get SQL database
By visiting database provided by previous group, the structure of the database are gotten. ( mySQL.java)

2) Import into neo4j and rebuild data structure
We assume that no circle is in the nodes of database. (SQL2neo4j.java)

3) Keyword Search
(KeywordSearchDao.java)

For the joined columns, we need to find the shortest path with minimum cost value among tables. To implement this method, we use the Steiner Tree algorithm. This function is implement in Join.java, which is the main function, Kruskai.java or SteineerTree.java used to find the shortest path between two nodes.

2. To understand the Rest

1) access to previous data

https://github.com/infsci2711/MultiDBs-Query-Server/blob/master/restful-api-brief.txt

====
2) REST service

Output parameter: a json format string like [{"column":"c_name","database":"homework2","record":"Pittsburgh","table":"city"}].

Final result example

http://54.174.121.196/index2.html?parm1=s

