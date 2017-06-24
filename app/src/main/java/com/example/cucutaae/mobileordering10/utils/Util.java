package com.example.cucutaae.mobileordering10.utils;

import java.math.BigDecimal;

/**
 * Created by cucut on 5/25/2017.
 */

public class Util {

    public static BigDecimal truncateDecimal(double x, int numberOfDecimals){
        if(x>0){
            return new BigDecimal(String.valueOf(x)).setScale(numberOfDecimals, BigDecimal.ROUND_FLOOR);
        }else{
            return new BigDecimal(String.valueOf(x)).setScale(numberOfDecimals, BigDecimal.ROUND_CEILING);
        }
    }
}
