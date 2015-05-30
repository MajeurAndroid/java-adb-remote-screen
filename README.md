# Adb-Remote-Screen

Simple tool to control your android phone from your pc through ADB.
It can be usefull for retrieving data from broken devices.
Supports only touches and swipes.

This project takes screenshots with adb every seconds. In fact taking screenshot, converting it from raw to png, and displaying it take little bit more than one second. So increase screenshot period will overflow your phone and result will not be better.

To start programm, right click on jar file > open with > Oracle Java X Runtime or OpenJDK X Runtime.
In command line 

```
cd {path to jar file}
java -jar AdbRemoteScreen.jar
```

## Sometimes, when starting program window stay empty, just close and relaunch.

![alt tag](https://raw.githubusercontent.com/MajeurAndroid/Adb-Remote-Screen/master/web_demo.png)
