package com.cluster.math.model;

import java.math.BigInteger;

/**
 * Created by envoy on 15.04.2017.
 */
public class Bits {
    private StringBuilder sb;
    private BigInteger number;
    private int size;

    public Bits(int size) {
        this.size = size;
        sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append('0');
        }
        number = new BigInteger("0");
    }

    public Bits(StringBuilder bits) {
        this(bits.length());
        setBites(bits);
    }

    public Bits(String bits) {
        this(new StringBuilder(bits));
    }

    public Bits(int size, BigInteger number) {
        this(size);
        setBites(number);
    }

    private void updateNumber() {
        number = new BigInteger('0' + sb.toString(), 2); //TODO 1 bit
    }

    private void setBites(StringBuilder bits) {
        setBites(bits, true);
    }

    private void setBites(BigInteger number) {
        this.number = number;
        String temp = number.toString(2);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < size - temp.length(); i++) {
            stringBuilder.append('0');
        }
        stringBuilder.append(temp);
        setBites(stringBuilder, false);
    }

    private void setBites(StringBuilder bites, boolean isUpdateNumber) {
        if (bites.length() != size) {
            throw new IllegalArgumentException("Illegal bits argument");
        }

        sb = new StringBuilder(bites);

        if (isUpdateNumber) {
            updateNumber();
        }
    }

    public StringBuilder getBites() {
        return new StringBuilder(sb);
    }

    public char get(int index) {
        if (index < sb.length()) {
            return sb.charAt(index);
        }
        throw new IllegalArgumentException("invalid index");
    }

    public BigInteger getNumber() {
        return number;
    }

    public int getSize() {
        return size;
    }
}
