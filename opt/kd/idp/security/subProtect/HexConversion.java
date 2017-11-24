package kd.idp.security.subProtect;

/**
 * <p>Title: Function Class</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: WatchData</p>
 * @author Huang Peng
 * @version 1.0
 */

import java.math.*;

public class HexConversion {
    protected final static String[] hexChars = { "0", "1", "2", "3", "4", "5",
        "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };

    /**
    * Hexify a byte array.
    *
    * @param data
    *            Byte array to convert to HexString
    *
    * @return HexString
    */
    public static String hexify(byte[] data) {
        if (data == null)
            return "null";

        StringBuffer out = new StringBuffer(256);
        int n = 0;

        for (int i = 0; i < data.length; i++) {
            if (n > 0){
                    out.append(' ');
            }
            out.append(hexChars[(data[i] >> 4) & 0x0f]);
            out.append(hexChars[data[i] & 0x0f]);
        }
        return out.toString();
    }

    /**
     * Hexify a byte array.
     *
     * @param data
     *            Byte array to convert to HexString
     *
     * @return HexString
     */
    public static String hexify_haveSpace(byte[] data) {
        if (data == null)
            return "null";

        StringBuffer out = new StringBuffer(1000);
        int n = 0;

        for (int i = 0; i < data.length; i++) {
            if (n == 0){
                    out.append(' ');
            }
            out.append(hexChars[(data[i] >> 4) & 0x0f]);
            out.append(hexChars[data[i] & 0x0f]);
        }
        return out.toString();
    }

    /**
     * Hexify a byte.
     *
     * @param data
     *            Byte to convert to HexString
     *
     * @return HexString
     */
    public static String hexify(byte data) {
        StringBuffer out = new StringBuffer(256);
        out.append(hexChars[(data >> 4) & 0x0f]);
        out.append(hexChars[data & 0x0f]);

        return out.toString();
    }

    /**
     * Parse bytes encoded as Hexadecimals into a byte array.
     * <p>
     *
     * @param byteString
     *            String containing HexBytes.
     *
     * @return byte array containing the parsed values of the given string.
     */
    public static byte[] parseHexString(String byteString) {
        byte[] result = new byte[byteString.length() / 2];
        for (int i = 0; i < byteString.length(); i += 2) {
            String toParse = byteString.substring(i, i + 2);
            result[i / 2] = (byte) Integer.parseInt(toParse, 16);
        }
        return result;
    }

    // e.g.00 84 00 00 08;
    public static byte[] parseHexString_haveSpace(String byteString) {
        byteString = byteString + " ";
        byte[] result = new byte[byteString.length() / 3];
        for (int i = 0; i < byteString.length(); i += 3) {
            String toParse = byteString.substring(i, i + 2);
            result[i / 3] = (byte) Integer.parseInt(toParse, 16);
        }
        return result;
    }

    public static long GetSW1SW2(byte[] resp) {
        if (resp == null)
            return 0xFFFF;
        int len = resp.length;
        long recode = Integer.parseInt(hexify(resp[len - 2]), 16) * 256
                    + Integer.parseInt(hexify(resp[len - 1]), 16);
        return recode;
    }

    /**
     * °ÑString×ª»»³ÉHex
     *
     * @param data
     * @return returnData
     */
    public static String parseStringToHexString(String data) {
        String result = "";
        try {
            BigInteger bi = new BigInteger(data.getBytes());
            result = bi.toString(16);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result.toUpperCase();
    }
}
