package com.czj.socket.netty.netty_tcp;

public class ConstantValue {

    public static final byte HEADER_FLAG = 0x7e;

    public static byte[] decodeData(byte[] buf, int len) {
        int dataBuf_i = 0;
        byte[] dataBuf = new byte[len * 2];
        for (int i = 0; i < len; i++) {

            if (buf[i] == (byte) 0x7d)  /*转义字符*/ {
                if ((i + 1 < len) && (buf[i + 1] == (byte) 0x5e))  /*数据为7e*/ {
                    dataBuf[dataBuf_i] = (byte) 0x7e;
                    dataBuf_i++;
                    i++;
                } else if ((i + 1 < len) && buf[i + 1] == (byte) 0x5d) /*数据为7d*/ {
                    dataBuf[dataBuf_i] = (byte) 0x7d;
                    dataBuf_i++;
                    i++;
                }
            } else /*其他数据*/ {
                dataBuf[dataBuf_i] = buf[i];
                dataBuf_i++;
            }
        }

        byte[] re = new byte[dataBuf_i];
        System.arraycopy(dataBuf, 0, re, 0, dataBuf_i);
        return re;
    }

    public static byte[] encodeData(byte[] buf, int len) {
        int dataBuf_i = 0;
        byte[] dataBuf = new byte[len * 2];
        for (int i = 0; i < len; i++) {

            if (buf[i] == (byte) 0x7e)  /*转义0x7d 0x5e字符*/ {
                dataBuf[dataBuf_i] = (byte) 0x7d;
                dataBuf_i++;
                dataBuf[dataBuf_i] = (byte) 0x5e;
                dataBuf_i++;
                i++;
            } else if (buf[i] == (byte) 0x7d)  /*转义0x7d 0x5e字符*/ {
                dataBuf[dataBuf_i] = (byte) 0x7d;
                dataBuf_i++;
                dataBuf[dataBuf_i] = (byte) 0x5d;
                dataBuf_i++;
                i++;
            } else /*其他数据*/ {
                dataBuf[dataBuf_i] = buf[i];
                dataBuf_i++;
            }
        }

        byte[] re = new byte[dataBuf_i];
        System.arraycopy(dataBuf, 0, re, 0, dataBuf_i);
        return re;
    }

}
