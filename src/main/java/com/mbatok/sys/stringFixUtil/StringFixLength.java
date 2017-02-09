package com.mbatok.sys.stringFixUtil;

/**
 * Created by mateusz on 29.10.16.
 */
public class StringFixLength {

    public static String generateFixLengthString(String toBeFixed,int fixedSize) {
        if(fixedSize < toBeFixed.length()) {
            throw  new IllegalStateException(toBeFixed  + " longer than " + fixedSize );
        }
        String stringSuffixWithSpaces = defineExtendingString(toBeFixed, fixedSize);
        return extendStringSize(toBeFixed, stringSuffixWithSpaces);
    }

    private static String defineExtendingString(String toBeFixed, int fixedSize) {
        int currentLenght = toBeFixed.length();
        String replaced = "";
        for (int i =0; i <= fixedSize - currentLenght;i++) {
            replaced +=" ";
        }
        return replaced;
    }

    private static String extendStringSize(String toBeFixed, String stringSuffixWithSpaces) {
        String returnString =  new String();
        String[] wordsArray =  toBeFixed.split(" ");

        for(int i = 0; i <wordsArray.length;i++) {
            if((i+1) < wordsArray.length) {
                if(isFirstCharacterInWordDigit(wordsArray[i + 1])) {
                    wordsArray[i] += stringSuffixWithSpaces;
                } else if( i + 1 != wordsArray.length) {
                    wordsArray[i] += " ";
                }
            }
            returnString+= wordsArray[i];
        }
        return returnString;
    }

    private static boolean isFirstCharacterInWordDigit(String s) {
        if(Character.isDigit(s.charAt(0))) return true;
        if(s.charAt(0) == 45) return true;
        else return false;
    }

}
