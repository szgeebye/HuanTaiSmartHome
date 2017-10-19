package huantai.smarthome.utils;

import static huantai.smarthome.utils.ConvertUtil.hexStringToByte;

/**
 * Description: 发送数据Utils
 * Auther：luojie
 * E-mail：luojie@hnu.edu.cn
 * Time：2017/9/10 13:47
 */
public class ControlUtils {
          //通知网关上报设备状态
          public static final byte[] STATUS_UP_DATA = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x15};


          /*
          *    SWITCH_ONE_A_OPEN	00f1f2f3f40100
          *    SWITCH_ONE_A_CLOSE	00f1f2f3f40200
          *    SWITCH_TWO_A_OPEN	01f1f2f3f40300
          *    SWITCH_TWO_A_CLOSE	01f1f2f3f40400
          *    SWITCH_TWO_B_OPEN	01f1f2f3f40500
          *    SWITCH_TWO_B_CLOSE	01f1f2f3f40600
          *    SWITCH_THREE_A_OPEN	02f1f2f3f40700
          *    SWITCH_THREE_A_CLOSE	02f1f2f3f40800
          *    SWITCH_THREE_B_OPEN	02f1f2f3f40900
          *    SWITCH_THREE_B_CLOSE	02f1f2f3f40a00
          *    SWITCH_THREE_C_OPEN	02f1f2f3f40b00
          *    SWITCH_THREE_C_CLOSE	02f1f2f3f40c00
          *    PLUG_OPEN		10f1f2f3f40d00
          *    PLUG_CLOSE		10f1f2f3f40e00
          **/

          /***
           * @param DevType 开关的类型
           * @param CMD 开关的控制命令
           * @param address 开关的MAC地址
           * @return 返回开关的控制命令
           */
          public static byte[] getSwitchInstruction(byte DevType, byte CMD, String address) {

                    //指令长度
                    byte[] instruction = new byte[ControlProtocol.Data_Length.SWITCH_INSTRUCTION_LENGTH];
                    //设备类型
                    instruction[0] =DevType;
                    /**
                     * 填充设备ID
                     * @param address 8位16进制字符串（F1F2F3F4）
                     */
                    byte[] devID = hexStringToByte(address);
                    for (int i = 0; i < devID.length; i++) {
                              instruction[i + 1] = devID[i];
                    }
                    //控制命令
                    instruction[5]=CMD;
                    //指令长度
                    instruction[6]=ControlProtocol.Instruction_Length.SWITCH_INSTRUCTION_LENGTH;
                    //返回控制命令
                    return instruction;
          }

          /***
           *
           * @param CMD 开关的控制命令
           * @param address 开关的MAC地址
           * @return 返回开关的控制命令
           */
          public static byte[] getAirInstruction(String address,byte CMD) {
                    //指令长度
                    byte[] instruction = new byte[ControlProtocol.Data_Length.AIR_LENGTH];


                    //返回控制命令
                    return instruction;
          }


          /**
           *
           * @param address 窗帘的MAC地址
           * @param CMD 窗帘的控制命令
           * @return 返回窗帘的控制命令
           */
          public static byte[] getCurtainInstruction(String address,byte CMD) {
                    byte[] bytes = hexStringToByte(address);
                    //指令模板
                    byte[] BYTES_BESE = {(byte) 0x30,(byte) 0x30,(byte) 0x30,(byte) 0x30,(byte) 0x30,(byte) 0x00, (byte) 0x04, (byte) 0xE1,(byte) 0x0A,
                         (byte) 0x00, (byte) 0xEF};
                    //添加地址
                    for (int i=0;i<4;i++) {
                              BYTES_BESE[1+i]=bytes[i];
                    }
                    //添加控制命令
                    BYTES_BESE[8]=CMD;
                    return BYTES_BESE;
          }

}

