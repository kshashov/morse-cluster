package com.cluster.math.model;

/**
 * Created by envoy on 15.04.2017.
 */
public class Bits {
    private StringBuilder sb;
    private long number;
    private int size;

    public Bits(int size) {
        this.size = size;
        sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append('0');
        }
        number = 0;
    }

    public Bits(int size, StringBuilder bits) {
        this(size);
        setBites(bits);
    }

    public Bits(int size, String bits) {
        this(size, new StringBuilder(bits));
    }

    public Bits(int size, long number) {
        this(size);
        setBites(number);
    }

    private void updateNumber() {
        int power = 0;
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == '1') {
                power++;
            }
        }
        number = (long) Math.pow(2, power);
    }

    private void setBites(StringBuilder bits) {
        setBites(bits, true);
    }

    private void setBites(long number) {
        this.number = number;
        String temp = Long.toBinaryString(number);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < size - temp.length(); i++) {
            stringBuilder.append('0');
        }
        stringBuilder.append(temp);
        setBites(stringBuilder, false);
    }

    private void setBites(StringBuilder bites, boolean isUpdateNumber) {
        this.sb = sb.delete(size - 1, sb.length() - 1);
        if (sb.length() < size) {
            for (int i = 0; i < size - sb.length(); i++) {
                sb.append('0');
            }
        }

        if (isUpdateNumber) {
            updateNumber();
        }
    }

    /*private void set(int index, int value) {
        if (index >= sb.length()) {
            throw new IllegalArgumentException("invalid index");
        }
        sb.setCharAt(index, value == 0 ? '0' : '1');
        updateNumber();
    }*/

    public StringBuilder getBites() {
        return new StringBuilder(sb);
    }

    public char get(int index) {
        if (index < sb.length()) {
            return sb.charAt(index);
        }
        throw new IllegalArgumentException("invalid index");
    }

    public long getNumber() {
        return number;
    }

    public int getSize() {
        return size;
    }
}
