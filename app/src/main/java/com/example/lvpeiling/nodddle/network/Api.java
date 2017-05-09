package com.example.lvpeiling.nodddle.network;

/**
 * Created by lvpeiling on 2017/4/28.
 */
public class Api {
    public static final String URL = "https://dribbble.com/";

    public static final String API_URL = "https://api.dribbble.com/v1/";

    /**
     * 用户认证url
     * 参数： client_id Required. The client ID you received from Dribbble when you registered.
     * redirect_uri
     * scope
     * state
     */
    public static final String AUTHORIZE_URL = URL + "oauth/authorize?client_id=%s";

    public static final String CALLBACK_URL = "http://www.nodddle.lvpeiling.com";

    public static final String TOKEN_URL = URL + "oauth/token";

    public static final String SHOTS_URL = API_URL + "shots";

}