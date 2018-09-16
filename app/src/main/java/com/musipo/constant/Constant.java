package com.musipo.constant;


public class
Constant {

    /*
     *  API_KEY used for web service authentication.
     *  API_KEY value = md5(android-package_name-version-launch_year)
     *  for this version =  md5(android-com.ibillingpad.customer-1.0-2015)
     */
    public static final String API_KEY = "59d42f84b255fe6991a27086e3b0d8a1";
    public static final String BUNDLE_ARGUMENT_SESSION_RESTORED = "session_restored";
    public static final String BUNDLE_ARGUMENT_ACTION = "action";
    public static final String USER_FILE = "user_file";
    public static final String BUNDLE_ARGUMENT_USER_DATA = "user_data";
    public static final String SERVER_ROOT_DEV = "http://52.7.138.222/ibillingpad/sugarcrm_ibillingpad";
    public static final String SERVER_ROOT = "https://testssl.ibillingpad.com";
    public static final String SERVER_ROOT_TEST = "hST = \"/custom/service/rest.php\";\nttp://52.7.138.222/ibillingpad/qa_sugarcrm_ibillingpad";
    // type of push messages
    public static final int PUSH_TYPE_CHATROOM = 1;
    public static final int PUSH_TYPE_USER = 2;

    /// public static final String BASE_URL = "http://52.66.148.147:8005/musipo/";


      public static final String BASE_URL ="http://192.168.0.105:8080/musipo/";


}
