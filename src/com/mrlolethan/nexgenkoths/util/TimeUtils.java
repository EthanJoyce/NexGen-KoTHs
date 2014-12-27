package com.mrlolethan.nexgenkoths.util;

public class TimeUtils {
	
	public static String formatToMMSS(int seconds) {
        int millis = seconds * 1000;
        int sec = millis / 1000 % 60;
        int min = millis / 60000 % 60;
        int hr = millis / 3600000 % 24;
        
        return ((hr > 0) ? String.format("%02d", hr) : "") + String.format("%02d:%02d", min, sec);
    }
	
	
}
