# Labforward Code Challenge for Backend Engineer



## Installation

1- clone project.

2- open terminal inside ProjectDir and execute ```./gradlew build``` to build jar

3- run your jar inside build/libs by ```java -jar lf-code-challenge-0.1.0.jar ```

4- check postman collection from [here](https://www.getpostman.com/collections/4ab73c714c5a25bad487) or file attached in project


## Assumption

Created missing APIs for list all and delete.

Changing APIMessage.java class for more well structure as following:
```
{
status: "OK",
message: "Success",
error: null,
result: <T>
} 
``` 

Postman collection as my UI ;) .

In PR notes you will find description of all implemented functions.
