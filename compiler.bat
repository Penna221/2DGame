@echo off
cls
javac -encoding UTF-8 -d bin -cp "lib/json.jar;lib/engine.jar" -sourcepath src src/main/*.java
javac -encoding UTF-8 -d bin -cp "lib/json.jar;lib/engine.jar" -sourcepath src src/states/*.java
javac -encoding UTF-8 -d bin -cp "lib/json.jar;lib/engine.jar" -sourcepath src src/gfx/*.java
javac -encoding UTF-8 -d bin -cp "lib/json.jar;lib/engine.jar" -sourcepath src src/tiles/*.java
javac -encoding UTF-8 -d bin -cp "lib/json.jar;lib/engine.jar" -sourcepath src src/world/*.java
javac -encoding UTF-8 -d bin -cp "lib/json.jar;lib/engine.jar" -sourcepath src src/entities/*.java
javac -encoding UTF-8 -d bin -cp "lib/json.jar;lib/engine.jar" -sourcepath src src/entities/mushrooms/*.java
javac -encoding UTF-8 -d bin -cp "lib/json.jar;lib/engine.jar" -sourcepath src src/io/*.java
javac -encoding UTF-8 -d bin -cp "lib/json.jar;lib/engine.jar" -sourcepath src src/ui/*.java
jar cfm MyGame.jar manifest.mf -C bin . -C lib .
java -jar MyGame.jar