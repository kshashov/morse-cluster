package com.cluster.math.utils;

import com.cluster.math.model.Bits;

/**
 * Created by envoy on 26.04.2017.
 */
public class InfSupFinder {
    public static StringBuilder[] findInfSup(String x, int Natom, int Mcl) {
        StringBuilder sbX = new StringBuilder(x);

        int N = 0;
        int M = Natom;
        for (int i = 0; i < sbX.length(); i++) {
            if (sbX.charAt(i) == '1') {
                N++;
            }
        }
        StringBuilder zeros = new Bits(Mcl).getBites();
        if (N == M) {
            return new StringBuilder[]{new StringBuilder(sbX), new StringBuilder(sbX)};
        }

        StringBuilder sbInf;
        StringBuilder sbSup;
        if (N > M) {
            int NM = N - M;

            //xInf
            sbInf = new StringBuilder(zeros);
            int k = 0;
            for (int i = Mcl - 1; i >= 0; i--) {
                if ((sbX.charAt(i) == '1') && (k != NM)) {
                    sbInf.setCharAt(i, '0');
                    k++;
                } else {
                    sbInf.setCharAt(i, sbX.charAt(i));
                }
            }

            //xSup
            sbSup = new StringBuilder(sbX);
            k = N - M + 1;
            int j = 0;
            int i;
            for (i = Mcl - 1; i >= 0; i--) {
                if (sbSup.charAt(i) == '1') {
                    j++;
                } else {
                    if (j >= k) {
                        break;
                    }
                }
            }
            sbSup.setCharAt(i, '1');
            j = 0;
            for (int z = i + 1; z < Mcl; z++) {
                if (j == k) {
                    break;
                }
                if (sbSup.charAt(z) == '1') {
                    sbSup.setCharAt(z, '0');
                    j++;
                }
            }
            toRight(sbSup, Mcl, i, '1');
        } else {
            int MN = M - N;

            //xSup
            sbSup = new StringBuilder(zeros);
            int k = 0;
            for (int i = Mcl - 1; i >= 0; i--) {
                if ((sbX.charAt(i) == '0') && (k != MN)) {
                    sbSup.setCharAt(i, '1');
                    k++;
                } else {
                    sbSup.setCharAt(i, sbX.charAt(i));
                }
            }

            //xInf
            k = M - N + 1;
            int j = 0;
            sbInf = new StringBuilder(sbX);
            int np = Mcl;
            for (int i = Mcl - 1; i >= 0; i--) {
                if (sbInf.charAt(i) == '1') {
                    np = i; //позиция в векторе
                }
            }
            int i;
            for (i = Mcl - 1; i >= np; i--) {
                if (sbInf.charAt(i) == '0') {
                    j++;
                } else {
                    if (j >= k) {
                        break;
                    }
                }
            }
            if (j < k) {
                sbInf = zeros; //TODO
            } else {
                sbInf.setCharAt(i, '0');
                j = 0;
                for (int z = i + 1; z < Mcl; z++) {
                    if (j == k) {
                        break;
                    }
                    if (sbInf.charAt(z) == '0') {
                        sbInf.setCharAt(z, '1');
                        j++;
                    }
                }
                toRight(sbInf, Mcl, i, '0');
            }
        }

        return new StringBuilder[]{sbSup, sbInf};
    }

    private static void toRight(StringBuilder v, int Mcl, int index, char bit) {
        int j = 0;
        for (int i = index + 1; i < Mcl; i++) {
            if (v.charAt(i) != bit) {
                j++;
            }
        }

        for (int i = index + 1; i < Mcl; i++) {
            if (j > 0) {
                v.setCharAt(i, (bit == '1') ? '0' : '1');
                j--;
            } else {
                v.setCharAt(i, bit);
            }
        }
    }
}
