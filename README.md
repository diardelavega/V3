# V3
V3


This is a tool for hotel comparison based on a specific topic.
The evaluation for that topic is done by searching in the comments of the reviews left from previous users

to run requires:
-maven
-java 1.7+

-forder structure as shown below
Dir*
|____reviw1.json, review2.json …. , , , , (.json files)
|
|______Semantic (directory)
| |
| |___semantic.json (file)

It is an Eclipse maven and java project.
It is run in the terminal, inside the project file (@ the pom.xml location)

1) mvn clean install
2) mvn clean compile assembly:single
3) java -cp target/***.jar run.Demo "<the Dir* path>" "<some topic>"
