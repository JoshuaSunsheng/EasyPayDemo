package com.easypay;

public class StringUtils {
    public static final String EMPTY = "";
    public static final int INDEX_NOT_FOUND = -1;
    private static final int PAD_LIMIT = 8192;
    private static final String[] PADDING = new String['\uffff'];

    static {
        PADDING[32] = "                                                                ";
    }

    public StringUtils() {
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
    public static boolean isBlank(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }
    public static boolean isNotEmpty(String str) {
        return str != null && str.length() > 0;
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        } else {
            int sz = str.length();

            for(int i = 0; i < sz; ++i) {
                if (!Character.isDigit(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isNumericSpace(String str) {
        if (str == null) {
            return false;
        } else {
            int sz = str.length();

            for(int i = 0; i < sz; ++i) {
                if (!Character.isDigit(str.charAt(i)) && str.charAt(i) != ' ') {
                    return false;
                }
            }

            return true;
        }
    }
    public static boolean isNotBlank(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }
    public static String substringBefore(String str, String separator) {
        if (!isEmpty(str) && separator != null) {
            if (separator.length() == 0) {
                return "";
            } else {
                int pos = str.indexOf(separator);
                return pos == -1 ? str : str.substring(0, pos);
            }
        } else {
            return str;
        }
    }
    public static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2);
    }


    public static String trim(String str) {
        return str == null ? null : str.trim();
    }


    public static String uncapitalize(String str) {
        int strLen;
        return str != null && (strLen = str.length()) != 0 ? (new StringBuffer(strLen)).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1)).toString() : str;
    }

    public static String upperCase(String str) {
        return str == null ? null : str.toUpperCase();
    }



    public static String bytesToHexStr(byte[] b) {
        return bytesToHexStr(b, 0, b.length);
    }

    public static String bytesToHexStr(byte[] b, int start, int len) {
        StringBuffer var3 = new StringBuffer();

        for(int var4 = start; var4 < start + len; ++var4) {
            var3.append(String.format("%02x", b[var4]));
        }

        return var3.toString();
    }

    public static byte[] hexStrToBytes(String str) {
        if (str.length() % 2 != 0) {
            str = "0" + str;
        }

        byte[] var1 = new byte[str.length() / 2];

        for(int var2 = 0; var2 < str.length(); var2 += 2) {
            var1[var2 / 2] = (byte)((Byte.parseByte(str.substring(var2, var2 + 1), 16) << 4) + Byte.parseByte(str.substring(var2 + 1, var2 + 2), 16));
        }

        return var1;
    }

    public static void hexStrToBytes(String str, byte[] b, int from) {
        if (str.length() % 2 != 0) {
            str = "0" + str;
        }

        hexStrToBytes(str, b, from, str.length() / 2);
    }

    public static void hexStrToBytes(String str, byte[] b, int from, int length) {
        if (str.length() % 2 != 0) {
            str = "0" + str;
        }

        for(int var4 = 0; var4 < Math.min(str.length(), length << 1) && from + var4 / 2 < b.length; var4 += 2) {
            b[from + var4 / 2] = (byte)((Byte.parseByte(str.substring(var4, var4 + 1), 16) << 4) + Byte.parseByte(str.substring(var4 + 1, var4 + 2), 16));
        }

    }

    public static byte[] hexStrToInverseBytes(String str) {
        if (str.length() % 2 != 0) {
            str = "0" + str;
        }

        byte[] var1 = new byte[str.length() / 2];

        for(int var2 = 0; var2 < str.length(); var2 += 2) {
            var1[var1.length - 1 - var2 / 2] = (byte)((Byte.parseByte(str.substring(var2, var2 + 1), 16) << 4) + Byte.parseByte(str.substring(var2 + 1, var2 + 2), 16));
        }

        return var1;
    }

    public static String inverseBytesToHexStr(byte[] b, int start, int len) {
        StringBuffer var3 = new StringBuffer();

        for(int var4 = start + len - 1; var4 >= start; --var4) {
            var3.append(String.format("%02x", b[var4]));
        }

        return var3.toString();
    }

    public static String lengthFix(String text, int length, char ch, boolean end) {
        if (text == null) {
            text = "";
        }

        int var4 = text.getBytes().length;
        if (length == var4) {
            return text;
        } else if (length <= var4) {
            return end ? new String(text.getBytes(), 0, length) : new String(text.getBytes(), var4 - length, length);
        } else {
            char[] var5 = new char[length - var4];

            for(int var6 = 0; var6 < var5.length; ++var6) {
                var5[var6] = ch;
            }

            StringBuffer var7 = new StringBuffer(text);
            if (end) {
                var7 = var7.append(var5);
            } else {
                var7 = var7.insert(0, var5);
            }

            return var7.toString();
        }
    }

    public static String lengthFix(String text, int length, String ch, boolean end) {
        return lengthFix(text, length, ch.charAt(0), end);
    }
}
