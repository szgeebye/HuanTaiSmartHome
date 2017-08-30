package huantai.smarthome.utils;

import java.security.MessageDigest;

public class Encryption {

	/**
	 * MD5单向加密，32位，用于加密密码，因为明文密码在信道中传输不安全，明文保存在本地也不安

全  
	 * @param str
	 * @return
	 */
    public static String encryption_data(String str)
    {  
        MessageDigest md5 = null;
        try  
        {  
            md5 = MessageDigest.getInstance("MD5");
        }catch(Exception e)
        {  
            e.printStackTrace();  
            return "";  
        }  
          
        char[] charArray = str.toCharArray();  
        byte[] byteArray = new byte[charArray.length];  
          
        for(int i = 0; i < charArray.length; i++)  
        {  
            byteArray[i] = (byte)charArray[i];  
        }  
        byte[] md5Bytes = md5.digest(byteArray);  
          
        StringBuffer hexValue = new StringBuffer();
        for( int i = 0; i < md5Bytes.length; i++)  
        {  
            int val = ((int)md5Bytes[i])&0xff;  
            if(val < 16)  
            {  
                hexValue.append("0");  
            }  
            hexValue.append(Integer.toHexString(val));
        }  
        return hexValue.toString();  
    }  
      
}
