# Adb-Remote-Screen
A simple tool to control your device through ADB. Useful to retrieve data for broken devices.
Supports touch, swipe and key inputs.

[**DOWNLOAD**](https://github.com/MajeurAndroid/Adb-Remote-Screen/releases/download/3.0/AdbRemoteScreen-3.0.jar)

![alt tag](https://raw.githubusercontent.com/MajeurAndroid/Adb-Remote-Screen/master/web_demo.png)

### How to use

###### From UI:

Right click on jar file > open with > Oracle Java X Runtime or OpenJDK X Runtime.

###### In command line :
```shell
cd path/to/jar/file
java -jar AdbRemoteScreen.jar
#or if custome adb binary
java -jar AdbRemoteScreen.jar --adb-path=/path/to/adb/binary
#or if any issue related to UI frameworks on linux
java -jar AdbRemoteScreen.jar --use-default-theme
#(or both arguments obviously)
```

### New features in 3.0
- Reviewed UI
- Platform specific adb binaries are now bundled with the app
- Optimizations and better resource handling
- Fixed bugs

### TODOs
- Add support for MAC OS
- Add directory dialog to let user choose adb file through UI
- Better handle device changes
- ...

### License

Copyright Majeur 2015-2020

Licensed under Apache License 2.0
