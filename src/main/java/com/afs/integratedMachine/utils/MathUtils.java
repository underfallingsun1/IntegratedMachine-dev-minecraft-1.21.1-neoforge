package com.afs.integratedMachine.utils;

public class MathUtils {

    //return whether x is between a and b
    public static boolean inRange(int x, int a, int b){
        return x >= a? (x <= b || x == a): x >= b;
    }
}
