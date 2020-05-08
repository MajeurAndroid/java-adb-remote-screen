package com.majeur.ars;

import javax.swing.*;
import java.lang.ref.WeakReference;
import java.util.Calendar;

public class Logger {

    private static final StringBuilder sStringBuilder = new StringBuilder("<html>");
    private static WeakReference<JTextPane> sTextAreaRef = new WeakReference<>(null);

    static {
        begin();
    }

    private static void begin() {
        log(new LogEntry(LogEntry.TYPE_BEGIN, "Beginning of LOG"));
    }

    public static void d(String msg) {
        log(new LogEntry(LogEntry.TYPE_DEBUG, msg));
        updateUI();
    }

    public static void i(String msg, Object... args) {
        i(String.format(msg, args));
    }

    public static void i(String msg) {
        log(new LogEntry(LogEntry.TYPE_INFO, msg));
        updateUI();
    }

    public static void w(String msg, Object... args) {
        w(String.format(msg, args));
    }

    public static void w(String msg) {
        log(new LogEntry(LogEntry.TYPE_WARN, msg));
        updateUI();
    }

    public static void e(String msg, Object... args) {
        e(String.format(msg, args));
    }

    public static void e(String msg) {
        log(new LogEntry(LogEntry.TYPE_ERR, msg));
        updateUI();
    }

    private static void log(LogEntry entry) {
        sStringBuilder.append("<font face=monospaced color='")
                .append(entry.htmlColor())
                .append("'>")
                .append(entry.timeStamp)
                .append("&nbsp;&nbsp;&nbsp;&nbsp;")
                .append(entry.readableType())
                .append(": ")
                .append(entry.message)
                .append("</font>")
                .append("<br>");
    }

    private static void updateUI() {
        JTextPane textArea;
        if ((textArea = sTextAreaRef.get()) != null) {
            textArea.setText(sStringBuilder.toString() + "</html>");

            JScrollPane scrollPane;
            JScrollBar scrollBar;
            if (textArea.getParent() instanceof JScrollPane && (scrollPane = (JScrollPane) textArea.getParent()) != null
                    && (scrollBar = scrollPane.getVerticalScrollBar()) != null)
                scrollBar.setValue(scrollBar.getMaximum());
        }
    }

    static void bindUI(JTextPane textArea) {
        textArea.setContentType("text/html");
        sTextAreaRef = new WeakReference<>(textArea);
        updateUI();
    }

    static void unbindUI() {
        sTextAreaRef.clear();
    }

    private static class LogEntry {
        static final int TYPE_INFO = 0;
        static final int TYPE_WARN = 1;
        static final int TYPE_ERR = 2;
        static final int TYPE_DEBUG = 3;
        static final int TYPE_BEGIN = 4;

        final String message;
        final String timeStamp;
        final int type;

        LogEntry(int type, String message) {
            this.type = type;
            this.message = message;
            Calendar calendar = Calendar.getInstance();
            this.timeStamp = String.format("%02d:%02d:%02d.%03d", calendar.get(Calendar.HOUR),
                    calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND));
        }

        String readableType() {
            switch (type) {
                case TYPE_INFO:
                    return "INFO";
                case TYPE_WARN:
                    return "WARNING";
                case TYPE_ERR:
                    return "ERROR";
                case TYPE_DEBUG:
                    return "DEBUG";
                case TYPE_BEGIN:
                    return "LOG";
                default:
                    return null;
            }
        }

        String htmlColor() {
            switch (type) {
                case TYPE_INFO:
                    return "black";
                case TYPE_WARN:
                    return "orange";
                case TYPE_ERR:
                    return "red";
                case TYPE_DEBUG:
                    return "green";
                case TYPE_BEGIN:
                    return "blue";
                default:
                    return null;
            }
        }
    }

}
