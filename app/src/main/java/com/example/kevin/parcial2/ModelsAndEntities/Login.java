package com.example.kevin.parcial2.ModelsAndEntities;

public class Login {

    //El token que devuelve la API al iniciar sesi�n
    private String token;

    //La API tambi�n puede devolver un mensaje ante un login fallido
    private String message;

    //Getters
    public String getToken() { return token; }
    public String getMessage() { return message; }
}
