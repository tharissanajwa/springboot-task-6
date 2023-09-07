package com.springboot.task6.model;

public class ApiResponse {

    private String message;  // Pesan yang akan dikirimkan bersama respons
    private Object data;     // Data yang akan dikirimkan bersama respons

    public ApiResponse() {
        // Constructor default
    }

    public ApiResponse(String message, Object data) {
        this.message = message;  // Mengatur pesan respons
        this.data = data;        // Mengatur data respons
    }

    public String getMessage() {
        return message;  // Mengembalikan pesan respons
    }

    public void setMessage(String message) {
        this.message = message;  // Mengatur pesan respons
    }

    public Object getData() {
        return data;  // Mengembalikan data respons
    }

    public void setData(Object data) {
        this.data = data;  // Mengatur data respons
    }
}
