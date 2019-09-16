/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.travelbanc;

public class ProjectConstant {

    public static final String APIUSERNAME = "admin";
    public static final String APIPASSWORD = "admin";

    public static final String AFFILIATECODE = "";

    //Test Affiliate Keys
    public static final String SECRETKEY = "019986A2B1E3525F77B95771AC7C04A0";
    public static final String PUBLICKEY = "A2E4A16A850945D88962AF0EF3CF9659";
    public static final String BASEURL = "http://139.162.210.123:8086/v1/";

    //Live Affiliate Keys
//    public static final String SECRETKEY = "8C3434DF21BDB61847BB37BB9E253891";
//    public static final String PUBLICKEY = "3B6E6BE0060B1E2DD83A5695CEA3534D";
//    public static final String BASEURL = "https://api.travelbeta.com/v1/";
    //Email Settings
    public static final String APIBASEURL = "https://api.mailgun.net/v3/mail.mytravelbanc.com";
    public static final String APIKEY = "key-2a70ec6347f24ecc939c1be4797fa80d";
    public static final String NOREPLYADDRESS = "noreply@mail.mytravelbanc.com";
    public static final String BOOKINGADDRESS = "booking@mail.mytravelbanc.com";
    public static final String HOTELEMAILSENDER = "TravelBanc Hotels <noreply@mail.mytravelbanc.com>";
    public static final String FLIGHTEMAILSENDER = "TravelBanc Flight <noreply@mail.mytravelbanc.com>";
    public static final String ACTIVATIONEMAILSENDER = "Account Activation <noreply@mail.mytravelbanc.com>";
    public static final String PASSWORDRESETSENDER = "Password Reset <noreply@mail.mytravelbanc.com>";

    //Client Web URL
    public static final String CLIENTTESTURL = "http://localhost:4200/";
    public static final String CLIENTLIVEURL = "https://www.mytravelbanc.com/";

    //Response Status
    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";

    //Payment Verification
    public static final String LIVE_PAYMENT_VERIFY_ENDPOINT = "https://api.ravepay.co/flwv3-pug/getpaidx/api/xrequery";
    public static final String TEST_PAYMENT_VERIFY_ENDPOINT = "https://ravesandboxapi.flutterwave.com/flwv3-pug/getpaidx/api/xrequery";
    public static final String TEST_SECRET_KEY = "FLWSECK-1b58418c24909e64c469f9ac6c50a002-X";
    public static final String LIVE_SECERT_KEY = "FLWSECK-a300c1366e3a49b3bcdedc97f4129767-X";

    //SMS
    public static final String SMS_API_KEY = "07da2cc3ddbd55aa294f323df48aefc64a4ffe5b";
    public static final String SMS_USERNAME = "bruno.okafor@gmail.com";
    public static final String SMS_URL = "http://api.ebulksms.com:8080/sendsms.json";

}
