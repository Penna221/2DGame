@echo off
cls
javac -d bin -cp "lib/json.jar;lib/engine.jar" -sourcepath src src/main/*.java
javac -d bin -cp "lib/json.jar;lib/engine.jar" -sourcepath src src/states/*.java
javac -d bin -cp "lib/json.jar;lib/engine.jar" -sourcepath src src/gfx/*.java
javac -d bin -cp "lib/json.jar;lib/engine.jar" -sourcepath src src/tiles/*.java
javac -d bin -cp "lib/json.jar;lib/engine.jar" -sourcepath src src/world/*.java
javac -d bin -cp "lib/json.jar;lib/engine.jar" -sourcepath src src/entities/*.java
javac -d bin -cp "lib/json.jar;lib/engine.jar" -sourcepath src src/entities/mushrooms/*.java
javac -d bin -cp "lib/json.jar;lib/engine.jar" -sourcepath src src/io/*.java
jar cfm MyGame.jar manifest.mf -C bin . -C lib .
java -jar MyGame.jar