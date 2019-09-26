package com.unknown.sdust.jwgl_tp.utils;

public class EncodeInp {
    public static String e(String input){
        StringBuilder output = new StringBuilder();
        int chr1, chr2, chr3;
        int enc1, enc2, enc3, enc4;
        int i = 0;
        do {
            chr1 = i < input.length() ? input.codePointAt(i++) : -1;
            chr2 = i < input.length() ? input.codePointAt(i++) : -1;
            chr3 = i < input.length() ? input.codePointAt(i++) : -1;
            enc1 = chr1 >> 2;
            enc2 = (((chr1 == -1 ? 0 : chr1) & 3) << 4) | ((chr2 == -1 ? 0 : chr2) >> 4);
            enc3 = (((chr2 == -1 ? 0 : chr2) & 15) << 2) | ((chr3 == -1 ? 0 : chr3) >> 6);
            enc4 = (chr3 < 0 ? 0 : chr3) & 63;
            if (chr2 == -1) {
                enc3 = enc4 = 64;
            } else if (chr3 == -1) {
                enc4 = 64;
            }
            String keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
            output.append(keyStr.charAt(enc1)).append(keyStr.charAt(enc2)).append(keyStr.charAt(enc3)).append(keyStr.charAt(enc4));
        } while (i < input.length());
        return output.toString();
    }
}
