package com.hackathon.cardless;

import com.microsoft.aad.adal.AuthenticationResult;

/**
 * Created by Jack on 18/02/2017.
 */

public class Constants {
    public static final String SDK_VERSION = "1.0";
    public static final String UTF8_ENCODING = "UTF-8";

    /***********************************************************
     *                    ADAL PARAMETERS                       *
     ************************************************************/
    public static String AUTHORITY_URL = "https://login.microsoftonline.com/bluebankb2c." +
            "onmicrosoft.com/v2.0/";
    public static String CLIENT_ID = "0f7ef810-2f9c-424c-942a-48c6ea361d9a";
    public static String[] SCOPES = {"0f7ef810-2f9c-424c-942a-48c6ea361d9a"};
    public static String[] ADDITIONAL_SCOPES = {};
    public static String REDIRECT_URL = "urn:ietf:wg:oauth:2.0:oob";
    public static String CORRELATION_ID = "";
    public static String USER_HINT = "";
    public static String EXTRA_QP = "";
    public static String FB_POLICY = "";
    public static String EMAIL_SIGNIN_POLICY = "B2C_1_BlueBankSUSI";
    public static String EMAIL_SIGNUP_POLICY = "B2C_1_BlueBankSUSI";
    public static boolean FULL_SCREEN = true;
    public static AuthenticationResult CURRENT_RESULT = null;
    public static String LOGOUT_URL = "https://login.microsoftonline.com/bluebankb2c.onmicrosoft." +
            "com/oauth2/v2.0/logout?post_logout_redirect_uri=" + REDIRECT_URL;

    public static String TOKEN;
    public static String PRIMARY_DEV = "c1c6b0e768fe4ef4a510da6dd9f4f666";

}
