# Adb-Remote-Screen

[Download link](http://majeurandroid.github.io/Adb-Remote-Screen/AdbRemoteScreen.zip)

Simple tool to control your android phone from your pc through ADB.
**It can be usefull for retrieving data from broken devices.**
Supports only touches and swipes.

You must provide adb binary path. To do this, provide your adb binary path in the local.properties file. In command line, you can pass as argument the adb path, see code snippet below

To start programm: 
 - From UI:

Right click on jar file > open with > Oracle Java X Runtime or OpenJDK X Runtime.

 - In command line :
```shell
cd path/to/jar/file
java -jar AdbRemoteScreen.jar
#or
java -jar AdbRemoteScreen.jar /path/to/adb/binary
```

This project takes screenshots with adb every seconds. In fact taking screenshot, converting it from raw to png, and displaying it take little bit more than one second. So increase screenshot period will overflow your phone and result will not be better.

## Sometimes, when starting program window stay empty, just close and relaunch.

![alt tag](https://raw.githubusercontent.com/MajeurAndroid/Adb-Remote-Screen/master/web_demo.png)
