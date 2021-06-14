package com.allteran.pizzamira.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class StringUtils {

    public static String priceFormatter(int price) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        DecimalFormat df = (DecimalFormat) nf;
        return (df.format(price) + " р.");
    }
}
