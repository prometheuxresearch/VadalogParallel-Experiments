#!/bin/bash

# Run the Java application with the required options
java \
  --add-opens java.base/java.util=ALL-UNNAMED \
  --add-opens java.base/java.lang.invoke=ALL-UNNAMED \
  --add-opens java.base/java.nio=ALL-UNNAMED \
  --add-opens java.base/sun.nio.ch=ALL-UNNAMED \
  --add-opens java.base/sun.util.calendar=ALL-UNNAMED \
  --add-opens java.base/java.lang=ALL-UNNAMED \
  --add-opens java.base/jdk.internal.ref=ALL-UNNAMED \
  -Xmx4G \
  -jar vadalogparallelapp.jar
