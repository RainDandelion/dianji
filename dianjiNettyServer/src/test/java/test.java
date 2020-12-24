import java.util.Locale;

public class test {
//
//    public static String bytesToHexString(byte[] bytes) {
//        if (bytes == null)
//            return null;
//        StringBuffer stringBuffer = new StringBuffer(bytes.length * 2);
//        for (byte b : bytes) {
//            if ((b & 0xFF) < 0x10) {
//                stringBuffer.append('0');
//            }
//            stringBuffer.append(Integer.toHexString(0xFF & b));
//        }
//
//        return stringBuffer.toString().toUpperCase(Locale.getDefault());
//    }
//    /**
//     * 16进制转换成为string类型字符串
//     * @param s
//     * @return
//     */
//    public static String hexStringToString(String s) {
//        if (s == null || s.equals("")) {
//            return null;
//        }
//        s = s.replace(" ", "");
//        byte[] baKeyword = new byte[s.length() / 2];
//        for (int i = 0; i < baKeyword.length; i++) {
//            try {
//                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//            s = new String(baKeyword, "UTF-8");
//            new String();
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//        return s;
//    }
    /// <summary>
    /// 将字符串(16进制)转换为字节数组
    /// </summary>
    /// <param name="s">要转换的字符串</param>
    /// <returns></returns>

    public static byte[] getHexBytes(String str) {
        str = str.replaceAll(" ", "");
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return bytes;
}
    public static void main(String[] args) {
        String test = "f5 05 02 00 00 02 ff ff ff ff ff ff 12";
        byte[] bytes = test.getBytes();
//        String string = bytesToHexString(bytes);
//        System.out.println(string);
//        String string1 = hexStringToString(string);
//        System.out.println(string1);
    }

}
