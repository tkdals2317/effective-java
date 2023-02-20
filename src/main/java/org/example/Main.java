package org.example;


public class Main {
    public static void main(String[] args) {
        System.out.println(Utensil.NAME + Dessert.NAME);
    }

    private static class Utensil {
        static final String NAME = "pot";
    }

    private static class Dessert {
        static final String NAME = "tie";
    }
}