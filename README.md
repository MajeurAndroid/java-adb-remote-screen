# Adb-Remote-Screen
Simple tool to control your device through ADB. Useful to retrieve data for broken devices.
Supports touches, swipes and keys input.

[**DOWNLOAD**](https://github.com/MajeurAndroid/Adb-Remote-Screen/releases/download/2.0/AdbRemoteScreen.zip)

![alt tag](https://raw.githubusercontent.com/MajeurAndroid/Adb-Remote-Screen/master/web_demo.png)

### How to use
First unzip the downloaded file anywhere you want. "local.properties" and "AdbRemoteScreen.jar" files **must** be in the same folder.
AdbRemoteScreen needs ADB to work. You must provide an adb binary path in the local.properties file. (Or as an argument if you are running jars from command line)

###### From UI:

Right click on jar file > open with > Oracle Java X Runtime or OpenJDK X Runtime.

###### In command line :
```shell
cd path/to/jar/file
java -jar AdbRemoteScreen.jar
#or
java -jar AdbRemoteScreen.jar /path/to/adb/binary
```

### New features in 2.0
- Screenshots are now retrieved directly from device to computer's RAM through adb shell stream
- Support for multiple connected devices
- Added keys input support (all keys supported by Android are available)
- Hot-Plug support (you can now unplug or plug a new device while running ARS, it will automatically detect changes)
- Fixed bugs

### TODOs
- Add support for Windows
- Add directory dialog to let user choose adb file through UI
- Better handle device changes
- 
### Note
I am new to Swing environement, do not hesitate to fork and pullrequest, even for just reimplementing functionalities already present but in a more convinient way.

### License

Copyright Majeur 2015-2016

Licensed under Apache License 2.0
