package huantai.smarthome.utils;

import static huantai.smarthome.utils.ConvertUtil.hexStringToByte;


public class CurtainControlUtils {
          private byte[] BYTES_BESE = {(byte) 0x30,(byte) 0x30,(byte) 0x30,(byte) 0x30,(byte) 0x30,(byte) 0x00, (byte) 0x04, (byte) 0xE1,(byte) 0x0A,
               (byte) 0x00, (byte) 0xEF};
          public final static int OPEN=1;
          public final static int CLOSE=2;
          public final static int STOP=3;
          public final static int REDIC=4;

          /**
           * @param address
           * @param command
           * @return
           *
           */
          public byte[] getCommand(String address,int command) {
                    byte[] bytes = hexStringToByte(address);
                    for (int i=0;i<4;i++) {
                              BYTES_BESE[1+i]=bytes[i];
                    }
                    switch (command) {
                              case OPEN:BYTES_BESE[8]=10;
                                        break;
                              case CLOSE:BYTES_BESE[8]=12;
                                        break;
                              case STOP:BYTES_BESE[8]=11;
                                        break;
                              case REDIC:BYTES_BESE[8]=17;
                                        break;
                    }
                    return BYTES_BESE;
          }
}
