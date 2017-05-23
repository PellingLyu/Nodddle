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
    public static final String AUTHORIZE_URL = URL + "oauth/authorize?client_id=%s&scope=%s";

    public static final String CALLBACK_URL = "http://www.nodddle.lvpeiling.com";

    public static final String TOKEN_URL = URL + "oauth/token";

    public static final String SHOTS_URL = API_URL + "shots";

    public static final String USER_URL = API_URL + "user";

    public static final String GET_SHOT_URL = SHOTS_URL + "/%s";

    public static final String LIKE_SHOT = GET_SHOT_URL + "/like";

    public static final String COMMENT_SHOT = GET_SHOT_URL + "/comments";

    public static final String CHECK_COMMENT_LIKE = COMMENT_SHOT +"/%s/like";

    public static final String USERINFO = API_URL + "users/%s";

    public static final String USER_SHOT = USERINFO + "/shots";

    public static final String USER_FOLLOWING = USER_URL + "/following";

    public static final String CHECK_USER_FOLLOWING = USER_FOLLOWING +"/%s";

    public static final String FOLLOW_USER = USERINFO + "/follow";

    public static final String USER_TEAM = USERINFO + "/teams";

}