package com.majeur.ars;

public final class Constants {

    public static final Object[][] VIEW_SCALE_KEY_MAP = {{0.25, "25%"}, {0.50, "50%"}, {0.75, "75%"},
            {1.00, "100%"}, {1.25, "125%"}, {1.50, "150%"}, {2.00, "200%"}};

    public static final class Strings {

        public static final String WINDOW_TILE_LOG = "Adb Remote Screen: Log";

        public static final String WINDOW_TILE_INPUT = "Adb Remote Screen: Input Keys";

        public static final String WINDOW_TILE_REGULAR = "Adb Remote Screen";
        public static final String WINDOW_TILE_DEVICE = "Adb Remote Screen - %s - %.1f FPS";

        public static final String MESSAGE_NO_ADB_PATH = "Make shure you specified adb path in local.properties file. This file must be in the same folder as .jar file.\n"
                + "Content must be:\nadbPath=/path/to/adb binary";
        public static final String TITLE_NO_ADB_PATH = "Error: No adb path specified";

        public static final String MESSAGE_NO_ADB_FILE = "Specified adb path isn't valid, cannot find adb binary.";
        public static final String TITLE_NO_ADB_FILE = "Error: Adb binary not found";

        public static final String GITHUB_PROJECT_URL = "https://github.com/MajeurAndroid/Adb-Remote-Screen";
        public static final String GITHUB_MAJEUR_URL = "https://github.com/MajeurAndroid";

        public static final String TITLE_NO_DEVICE = "No device found";
        public static final String MESSAGE_NO_DEVICE = "Please connect a device to your computer.\n(Make sure you have enabled USB Debugging and unlocked your device if any security PIN or pattern has been set)";

        public static final String ABOUT_MESSAGE = "<html><h1>Adb Remote Screen</h1><h3>Version 3.0</h3><h4>Author</h4><p>This program is developed by MajeurAndroid (<a href=\"mailto:majeur.android@gmail.com\">majeur.android@gmail.com</a>).</p><h4>Source</h4><p>This program is Open Source. Source code can be found on <a href=\"https://github.com/MajeurAndroid/Adb-Remote-Screen\">GitHub</a>.</p><h4>License</h4><q>Copyright 2020 MajeurAndroid<br/>Licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this<br/>file except in compliance with the License.";
    }

    public static final class Adb {

        public static final Object[][] INPUT_KEY_MAP = {{0, "UNKNOWN"}, {1, "MENU"}, {2, "SOFT_RIGHT"},
                {3, "HOME"}, {4, "BACK"}, {5, "CALL"}, {6, "ENDCALL"}, {7, "0"}, {8, "1"}, {9, "2"},
                {10, "3"}, {11, "4"}, {12, "5"}, {13, "6"}, {14, "7"}, {15, "8"}, {16, "9"},
                {17, "STAR"}, {18, "POUND"}, {19, "DPAD_UP"}, {20, "DPAD_DOWN"}, {21, "DPAD_LEFT"},
                {22, "DPAD_RIGHT"}, {23, "DPAD_CENTER"}, {24, "VOLUME_UP"}, {25, "VOLUME_DOWN"},
                {26, "POWER"}, {27, "CAMERA"}, {28, "CLEAR"}, {29, "A"}, {30, "B"}, {31, "C"}, {32, "D"},
                {33, "E"}, {34, "F"}, {35, "G"}, {36, "H"}, {37, "I"}, {38, "J"}, {39, "K"}, {40, "L"},
                {41, "M"}, {42, "N"}, {43, "O"}, {44, "P"}, {45, "Q"}, {46, "R"}, {47, "S"}, {48, "T"},
                {49, "U"}, {50, "V"}, {51, "W"}, {52, "X"}, {53, "Y"}, {54, "Z"}, {55, "COMMA"},
                {56, "PERIOD"}, {57, "ALT_LEFT"}, {58, "ALT_RIGHT"}, {59, "SHIFT_LEFT"}, {60, "SHIFT_RIGHT"},
                {61, "TAB"}, {62, "SPACE"}, {63, "SYM"}, {64, "EXPLORER"}, {65, "ENVELOPE"}, {66, "ENTER"},
                {67, "DEL"}, {68, "GRAVE"}, {69, "MINUS"}, {70, "EQUALS"}, {71, "LEFT_BRACKET"},
                {72, "RIGHT_BRACKET"}, {73, "BACKSLASH"}, {74, "SEMICOLON"}, {75, "APOSTROPHE"},
                {76, "SLASH"}, {77, "AT"}, {78, "NUM"}, {79, "HEADSETHOOK"}, {80, "FOCUS"}, {81, "PLUS"},
                {82, "MENU"}, {83, "NOTIFICATION"}, {84, "SEARCH"}, {85, "TAG_LAST_KEYCODE"}};
    }

}
