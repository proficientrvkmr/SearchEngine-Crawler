package com.domain.searchengine.service;

public class Temp {

    static void smiley_print(int times_to_call) {
        System.out.println("  * * * *\n");
        System.out.println("*          *\n");
        System.out.println("    .    .\n");
        System.out.println("*          *\n");
        System.out.println("*    \\_/   *\n");
        System.out.println("*          *\n");
        System.out.println("  * * * *\n");

        if (--times_to_call > 0)
            smiley_print(times_to_call);
    }

    public static void main(String[] args) {
        smiley_print(3);
    }
}
