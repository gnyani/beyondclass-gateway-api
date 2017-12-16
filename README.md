Do not checkin .idea folder and .gradle folder.

Useful commands for gradle:
./gradlew build -- builds the project
./gradlew tasks -- to see all the tasks you can do with gradle (this will say you what tasks you can run)

Steps to Set Up Beyond Class.

You need to run three services to start the project. We follow microservices architecture and as of now we have three services built partially.

1. Engineering Everything - This Service acts as an API gateway to the system, It takes care user registration , user logging and some other functionalities.
    * This is a spring boot rest service built using spring-data-mongodb.
    * Steps to setup and run this project locally On mac:  
        * Install mongodb : ```brew install mongodb```   
        * Start Mongodb: ```mongod```
        * Git clone the project and export to intellij by checking use custom gradle wrapper configuration
        * ```cd ...../EngineeringEverything``` then run ```./gradlew clean build``` which fetches all the required dependencies and bundle them into a jar.
        * ```./gradlew bootRun``` starts the spring boot service , mongodb should be running before you start the spring boot application or else 
        it will fail to startup.

2. Assignments - This is a springboot service which deals with all the things related to assignments, teachers can create assignments and 
students can view and submit.
    * Steps to setup and run project locally On Mac:
        * Git clone the project and export to intellij by checking use custom gradle wrapper configuration
        * ```cd ..../Assignments``` then run ```./gradlew clean build``` to build the jar and ```./gradlew bootRun``` to start the service.

3. UIservice - This is a UI service to the project.Built using Reactjs bootstarped with create-react-app
    * Steps to setup and run project locally On Mac:
        * Git clone the project and export to any of your favourite editors I suggest Atom.
        * install yarn ```brew install yarn``` nodejs comes along with this if not interested you can exclude using ```--without-node``` param.
        * ```cd Uiproject/``` and type ```yarn -v``` to check if yarn got installed successfully or not.If you see command not found then something went 
            wrong with the yarn install.
        * Once yarn is set up and you are in the project root folder do ```yarn install``` which installs all the required react js modules.
        * Once above step is done do ```yarn start``` and your ui project should start on port 3000 if everything goes accepted. You can access the UI at
             http://localhost:3000/#/ .
        