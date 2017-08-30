package huantai.smarthome.utils;

/**
 * description:协议
 * auther：luojie
 * time：2017/8/30 18:35
 */
public class ControlProtocol {

          //设备类型
          public class DevType{
                    //一位开关
                    public static final byte SWITCH_ONE=0x00;
                    //二位开关
                    public static final byte SWITCH_TWO=0x01;
                    //三位开关
                    public static final byte SWITCH_THREE=0x02;
                    //四位开关
                    public static final byte SWITCH_FOUR=0x03;
                    //五孔插座
                    public static final byte PLUG_FIVE=0x10;
                    //五孔86插座
                    public static final byte PLUG_FIVE_86=0x11;
                    //红外转发空调
                    public static final byte CONTROL_AIR=0x20;
                    //红外转发电视
                    public static final byte CONTROL_TV=0x21;
                    //窗帘
                    public static final byte CONTROL_WINDOW=0x30;
                    //锁
                    public static final byte CONTROL_LOCK=0x40;
                    //增氧机
                    public static final byte CONTROL_OXYGEN=0x50;
          }

          //设备CMD
          public class DevCMD {
                    //正常控制
                    public static final byte NORMAL=0x00;
                    //一位开关——A——开
                    public static final byte SWITCH_ONE_A_OPEN=0x01;
                    //一位开关——A——关
                    public static final byte SWITCH_ONE__CLOSE=0x02;
                    //二位开关——A——开
                    public static final byte SWITCH_TWO_A_OPEN=0x03;
                    //二位开关——A——关
                    public static final byte SWITCH_TWO_A_CLOSE=0x04;
                    //二位开关——B——开
                    public static final byte SWITCH_TWO_B_OPEN=0x05;
                    //二位开关——B——关
                    public static final byte SWITCH_TWO_B_CLOSE=0x06;
                    //三位开关——A——开
                    public static final byte SWITCH_THREE_A_OPEN=0x07;
                    //三位开关——A——关
                    public static final byte SWITCH_THREE_A_CLOSE=0x08;
                    //三位开关——B——开
                    public static final byte SWITCH_THREE_B_OPEN=0x09;
                    //三位开关——B——关
                    public static final byte SWITCH_THREE_B_CLOSE=0x0a;
                    //三位开关——C——开
                    public static final byte SWITCH_THREE_C_OPEN=0x0b;
                    //三位开关——C——关
                    public static final byte SWITCH_THREE_C_CLOSE=0x0c;
                    //插座开
                    public static final byte PLUG_OPEN=0x0d;
                    //插座关
                    public static final byte PLUG_CLOSE=0x0e;
                    //添加新设备
                    public static final byte ADD_DEVICE=0x10;
                    //删除设备
                    public static final byte DELETE_DEVICE=0x11;
                    //获取设备信息
                    public static final byte GET_DATA=0x12;
                    //设备复位
                    public static final byte DEVIC_RESET=0x13;
                    //设备上传状态到网关
                    public static final byte DEVIC_REPORT_STATU=0x14;
                    //请求设备上报状态
                    public static final byte COMMAND_DEVIC_REPORT_DATA=0x15;
          }

          //指令长度
          public class Instruction_Length{
                    //空调指令长度 5字节
                    public static final byte AIR__LENGTH=0x05;
                    //开关指令长度 5字节
                    public static final byte SWITCH_INSTRUCTION_LENGTH=0x00;
                    //窗帘指令长度 5字节
                    public static final byte CURTAIN_INSTRUCTION_LENGTH=0x04;
                    //增氧机指令长度 8字节
                    public static final byte OXYGEN_INSTRUCTION_LENGTH=0x08;
          }
}
