package huantai.smarthome.utils;

/**
 * Description: 发送数据Utils
 * Auther：luojie
 * E-mail：luojie@hnu.edu.cn
 * Time：2017/9/10 13:47
 */

public class ControlUtils {
          //通知网关上报设备状态
          public static final byte[] STATUS_UP_DATA={(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,0x15};


          /**
           * description:开关控制类
           * auther：luojie
           * time：2017/9/10 15:35
           */
          public class SwitchControl {

                    //指令长度
                    private byte[] instruction=new byte[7];
                    /**
                     * 填充设备ID
                     * @param address 8位16进制字符串（F1F2F3F4）
                     */
                    public SwitchControl(String address) {
                              byte[] devID=ConvertUtil.hexStringToByte(address);
                              for (int i=0;i<devID.length;i++) {
                                        instruction[i+1]=devID[i];
                              }
                    }
                    public byte[] getSwitchOpenInstruction()
                    {

                              return instruction;
                    }


          }

}

