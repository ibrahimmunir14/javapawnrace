#!/bin/bash

# cd into the directory containing this script
cd "$(dirname "$0")"

javac -g -d out -cp "./lib/*" -sourcepath src:test src/*.java test/*.java
java -cp "./lib/*:out" org.junit.runner.JUnitCore PawnRaceTest
