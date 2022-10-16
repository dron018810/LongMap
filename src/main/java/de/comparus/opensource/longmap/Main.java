package de.comparus.opensource.longmap;

import java.util.HashMap;

public class Main {

    private static final int MAX = 10000000;

    public static void main(String[] args) {

        //Compare put() performance with HashMap

        LongMapImpl<String> longMap = new LongMapImpl<>();
        HashMap<Long, String> hashMap = new HashMap<>();

        long longMapStartTime = System.currentTimeMillis();
        for(int i = 0; i < MAX; i++) {
            longMap.put(Integer.MAX_VALUE + i, String.valueOf(i));
        }
        long longMapResult = System.currentTimeMillis() - longMapStartTime;

        long hashMapStartTime = System.currentTimeMillis();
        for(int i = 0; i < MAX; i++) {
            hashMap.put((long)(Integer.MAX_VALUE + i), String.valueOf(i));
        }
        long hashMapResult = System.currentTimeMillis() - hashMapStartTime;

        System.out.println("LongMapImplResult in ms: " + longMapResult);
        System.out.println("hashMapResult in ms: " + hashMapResult);
    }
}
