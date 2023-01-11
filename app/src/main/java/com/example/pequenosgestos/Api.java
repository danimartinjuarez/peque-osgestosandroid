package com.example.pequenosgestos;

public class Api {
    private static final String ROOT_URL = "http://192.168.0.20:8080/api/v1/users/";

    public static final String URL_CREATE_USER = ROOT_URL + "createUser";
    public static final String URL_READ_USERS = ROOT_URL;
    public static final String URL_UPDATE_USER = ROOT_URL + "update/id";
    public static final String URL_DELETE_USER = ROOT_URL + "id";
}
