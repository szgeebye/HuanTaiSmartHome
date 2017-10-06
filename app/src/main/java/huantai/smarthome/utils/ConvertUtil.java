package huantai.smarthome.utils;

/**
 * description:数据转换Util
 * auther：luojie
 * time：2017/8/30 16:39
 */
public class ConvertUtil {
          /**
           * 合并两个byte数组
           * @param byte_1
           * @param byte_2
           * @return byte_1+byte_2
           */
          public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){
                    byte[] byte_3 = new byte[byte_1.length+byte_2.length];
                    System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
                    System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
                    return byte_3;
          }

          /**
           * 把16进制字符串转换成字节数组
           * @param hex 16进制字符串F1F2F3F4
           * @return 返回对应的byte数据
           */
          public static byte[] hexStringToByte(String hex) {
                    int len = (hex.length() / 2);
                    byte[] result = new byte[len];
                    char[] achar = hex.toCharArray();
                    for (int i = 0; i < len; i++) {
                              int pos = i * 2;
                              result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
                    }
                    return result;
          }

          /**
           * char转byte
           * @param c 字符转比特
           * @return byte
           */
          private static byte toByte(char c) {
                    byte b = (byte) "0123456789ABCDEF".indexOf(c);
                    return b;
          }

          /**
           * char转byte
           * @param i int转byte
           * @return byte
           */
          public static byte[] intToByteArray(int i) {
                    byte[] result = new byte[4];
                    result[0] = (byte)((i >> 24) & 0xFF);
                    result[1] = (byte)((i >> 16) & 0xFF);
                    result[2] = (byte)((i >> 8) & 0xFF);
                    result[3] = (byte)(i & 0xFF);
                    return result;
          }



          /***
           * 把字节数组转换成16进制字符串
           * @param bytes 字节数组
           * @return 16进制字符串 F1F2F3F4
           */
          public static String byteStringToHexString(byte[] bytes) {
                    StringBuilder stringBuilder = new StringBuilder("");
                    if (bytes == null || bytes.length <= 0) {
                              return null;
                    }
                    for (int i = 0; i < bytes.length; i++) {
                              int v = bytes[i] & 0xFF;
                              String hv = Integer.toHexString(v);
                              if (hv.length() < 2) {
                                        stringBuilder.append(0);
                              }
                              stringBuilder.append(hv);
                    }
                    return stringBuilder.toString();
          }


          /**
           * 两byte转int
           * @param high 高位
           * @param low 地位
           * @return int
           */
          public static int byte2ToInt(byte high,byte low) {
                    int value;
                    value = (int) ((low&0xFF)
                         | ((high<<8) & 0xFF00)
                         | ((0x0<<16)& 0xFF0000)
                         | ((0x0<<24) & 0xFF000000));
                    return value;
          }




}
