Building:

In base directory run:

mvn clean package

The buit jar will be under target folder with filename: adls-gen2-write-1.0-SNAPSHOT.jar

Config File:

Place application.properties file with following filled properties:

adlsapp.accountName=
adlsapp.accountKey=
adlsapp.containerName=
adlsapp.pathToDumpFile=
adlsapp.triggerFile=

Place the file inside config folder (create one if does not exist)

Running:

Having placed the properties file in config and the built the jar - in base directory run:

java -jar target/adls-gen2-write-1.0-SNAPSHOT.jar




