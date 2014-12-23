package com.mrlolethan.nexgenkoths.util;

public class NumberUtils {
    
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException ex) {
            return false;
        }
    }
    
    
    public static boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch(NumberFormatException ex) {
            return false;
        }
    }
    
    
    public static boolean isShort(String str) {
        try {
            Short.parseShort(str);
            return true;
        } catch(NumberFormatException ex) {
            return false;
        }
    }
    
    
    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException ex) {
            return false;
        }
    }
    
    
    public static boolean isLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch(NumberFormatException ex) {
            return false;
        }
    }
    
    
}
