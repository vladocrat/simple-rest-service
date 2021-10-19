# simple-rest-service

This is a simple rest service using HTTPServer java class. SQL operations are also done using JDBC.

# How to install
  
1. make sure you have java and maven installed

<i>java version used: 15.0.2</i>
  ```Java
  java --version
  ```
  
  ```Maven
  mvn --version
  ```

2. clone the reposiroty
Go to the directory you want it to be cloned to, and use git clone command inside git bash
```
git clone https://github.com/vladocrat/simple-rest-service.git
```
3. add db config file
step into main folder and create a new folder called <b>resources</b>, step into it and create a new yaml file called <b>config.yaml</b>. 

```
simple-rest-service 
- src 
   -main
     -java 
     -resources
        -config.yaml
-pom.xml
```

Inside this yaml you should have at least 3 fields <b>url</b> <b>username</b> <b>password</b>
* url is responsible for the link needed to connect to the db <b>only MySql is supported</b>
* username and password are used to login into db
<p>Example, config.yaml:</p>

```yaml
url: "jdbc:mysql://localhost:3306/your-db" 
username: "your-username"
password: "your-password"
```

# How to start
When you are done with downloading and configuring a config.yaml file, open the terminal at the root of the project and use mvn compile command

```maven
mvn compile
```

if the build was successful write the following

```maven
mvn exec:java -Dexec.mainClass="Main"
```

if you see the message "server was successfully launched" you are good to go!


# Usage

The api has 5 endpoints

* /api/get
* /api/all
* /api/save
* /api/update
* /api/delete

<h3>get</h3>
syntax:

```
/api/get?id=
```
returns selected person by a given id

<h3>all</h3>
syntax:

```
/api/all
```
returns all people from the table

<h3>save</h3>
syntax:

```
/api/save?surname= &name= & secondname= &date= 
```
* date - birth date of the person (yyyy-MM-dd format)

<i>when you are saving a person all the attributes must be there, order doesn't matter</i>

<h3>update</h3>
syntax:

```
/api/update/?id= &surname= &name= &secondname= &date= 
```
* id - the id of the person you want to change

<i>all the params <b>must</b> be present, if you only want to alter specific fields you can provide "default" param</i>

<p>Example:</p>

```
http://localhost:8081/api/update?id=1&surname=default&name=default&secondname=john&date=2020-01-01
```

<h3>delete</h3>
syntax: 

```
/api/delete?id= 
```
deletes a person by a given id and returns the deleted person
