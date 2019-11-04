package com.noob.storage.calc.bit;

/**
 * @author 卢云(luyun)
 * @since 2019.08.19
 */
public class HexConverter {

    public static String longToBinary(long l) {
        StringBuilder s = new StringBuilder();
        for (int i = 63; i >= 0; i--) {
            long bit = (l >> i) & 1;
            s.append(bit == 1 ? '1' : '0');
            if (i < 63 && i % 8 == 0) {
                s.append(" ");
            }
        }
        return s.toString();
    }

    public static String intToBinary(int num) {
        StringBuilder s = new StringBuilder();
        for (int i = 31; i >= 0; i--) {
            long bit = (num >> i) & 1;
            s.append(bit == 1 ? '1' : '0');
            if (i < 31 && i % 8 == 0) {
                s.append(" ");
            }
        }
        return s.toString();
    }

    public static String byteToBinary(byte num) {
        StringBuilder s = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            long bit = (num >> i) & 1;
            s.append(bit == 1 ? '1' : '0');
        }
        return s.toString();
    }

    // 二进制 0b11111111 = 255
    private static final int MASK_2 = 0b11111111;
    // 八进制 0144 = 100
    @SuppressWarnings("OctalInteger")
    private static final int MASK_8 = 0144;
    // 十六进制 0xff = 255
    private static final int MASK_16 = 0xff;


    public static byte[] longToBytes(long l) {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) ((l >> 56) & MASK_2);
        bytes[1] = (byte) ((l >> 48) & MASK_2);
        bytes[2] = (byte) ((l >> 40) & MASK_2);
        bytes[3] = (byte) ((l >> 32) & MASK_2);
        bytes[4] = (byte) ((l >> 24) & MASK_2);
        bytes[5] = (byte) ((l >> 16) & MASK_2);
        bytes[6] = (byte) ((l >> 8) & MASK_2);
        bytes[7] = (byte) (l & MASK_2);
        return bytes;
    }

    public static void main(String[] args) {
        System.out.println(MASK_2);
        System.out.println(MASK_8);
        System.out.println(MASK_16);
        System.out.println(intToBinary(64));
        System.out.println(longToBinary(64));
        byte[] bytes = longToBytes(64);
        for (byte b : bytes) {
            System.out.print(byteToBinary(b) + " ");
        }
    }
}