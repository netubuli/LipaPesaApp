package com.otemainc.mlipa.util;

public class AppConfig {
    // Server user login url
    public static String URL_LOGIN = "http://192.168.100.250:8082/mlipaapi/client/login.php";
    // Server user register url
    public static String URL_REGISTER = "http://192.168.100.250:8082/mlipaapi/client/register.php";
    //Server send money url
    public static String URL_SEND = "http://192.168.100.250:8082/mlipaapi/account/transfer.php";
    //Server send money url
    public static String URL_WITHDRAW = "http://192.168.100.250:8082/mlipaapi/account/withdraw.php";
    //balance url
    public static String URL_BALANCE ="http://192.168.100.250:8082/mlipaapi/account/getbalance.php";
    //transaction details url
    public static String URL_TRANSAC_DETAILS ="http://192.168.100.250:8082/mlipaapi/account/get_transaction_history.php";

}