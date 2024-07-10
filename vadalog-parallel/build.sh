#!/bin/bash

mvn -f pom.xml clean package
mv target/vadalogparallelapp-1.0.0.jar vadalogparallelapp.jar
