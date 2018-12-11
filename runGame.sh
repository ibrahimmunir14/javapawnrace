#!/bin/bash

# cd into the directory containing this script
cd "$(dirname "$0")"

javac -g -d out -cp "./lib/*" -sourcepath src src/*.java
java -ea -cp "./lib/*:out" PawnRace p C A h
