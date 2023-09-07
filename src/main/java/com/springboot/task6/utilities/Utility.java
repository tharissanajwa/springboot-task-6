package com.springboot.task6.utilities;

public class Utility {
    // Metode untuk menghilangkan spasi berlebih
    public static String inputTrim(String input) {
        String inputTrim;
        if (input == null) {
            inputTrim = null;
        } else {
            inputTrim = input.trim().replaceAll("\\s+", " ");
        }
        return inputTrim;
    }

    // Metode untuk memberikan validasi inputan pengguna
    public static int inputCheck(String input) {
        int valid = 0;
        if (input == null || input.isEmpty()) {
            valid = 1; // Jika inputan pengguna berupa null atau kosong, maka akan memberikan nilai 1(yang berarti error)
        } else if (!input.matches("^[a-zA-Z\\s]+$")) {
            valid = 2; // Jika inputan pengguna selain huruf dan spasi, maka akan memberikan nilai 2(yang berarti error)
        }
        return valid;
    }

    // Metode untuk memberikan validasi inputan pengguna
    public static int inputContainsNumber(String input) {
        int valid = 0;
        if (input == null || input.isEmpty()) {
            valid = 1; // Jika inputan pengguna berupa null atau kosong, maka akan memberikan nilai 1(yang berarti error)
        } else if (!input.matches("^[a-zA-Z0-9\\s]+$")) {
            valid = 2; // Jika inputan pengguna selain huruf, angka dan spasi, maka akan memberikan nilai 2(yang berarti error)
        }
        return valid;
    }
}