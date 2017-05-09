package com.example.lvpeiling.nodddle.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSON;
import com.example.lvpeiling.nodddle.Constants;
import com.example.lvpeiling.nodddle.NodddleApplication;
import com.example.lvpeiling.nodddle.R;
import com.example.lvpeiling.nodddle.model.TokenResponse;
import com.example.lvpeiling.nodddle.network.Api;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SignInActivity extends BaseActivity {

    @BindView(R.id.webView)
    WebView webView;
    private static final String TAG = "SignInActivity";
    @BindView(R.id.progress)
    ContentLoadingProgressBar progress;


    @Override
    public int getContentViewId() {
        return R.layout.activity_sign_in;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initAllView(Bundle savedInstanceState) {
        webView.getSettings().setJavaScriptEnabled(true);//支持javascript
        webView.setWebViewClient(new OAuthWebViewClient());
        webView.loadUrl(String.format(Api.AUTHORIZE_URL, Constants.CLIENT_ID));

    }

    class OAuthWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progress.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progress.hide();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            progress.show();
            interceptUrlCompat(view, url);

            return true;
        }

        private void interceptUrlCompat(WebView view, String url) {
            if (isRedirectUriFound(url, Api.CALLBACK_URL)) {
                Uri uri = Uri.parse(url);
                String code = uri.getQueryParameter(Constants.PARAM_CODE);
                Log.e("code:", code);
                if (!TextUtils.isEmpty(code)) {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    FormBody.Builder formBody = new FormBody.Builder();
                    formBody.add("client_id", Constants.CLIENT_ID);
                    formBody.add("client_secret", Constants.CLIENT_SECRET);
                    formBody.add("code", code);
                    formBody.add("redirect_uri", Api.CALLBACK_URL);
                    Request request = new Request.Builder().url(Api.TOKEN_URL).post(formBody.build()).build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e(TAG, "fail:" + e.getMessage());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progress.hide();
                                }
                            });
                            String responseStr = response.body().string();
                            Log.e(TAG, "response:" + responseStr);
                            TokenResponse tokenResponse = JSON.parseObject(responseStr, TokenResponse.class);
                            if (tokenResponse.getAccess_token() != null) {
                                NodddleApplication.getInstance().setTokenCode(tokenResponse.getAccess_token());
                                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

                } else {
                    Log.e("sqsong", "code is null");
                    return;
                }
                return;
            }
            Log.i("sqsong", "url---> " + url);
            view.loadUrl(url);
        }

    }

    static boolean isRedirectUriFound(String uri, String redirectUri) {
        Uri u = null;
        Uri r = null;
        try {
            u = Uri.parse(uri);
            r = Uri.parse(redirectUri);
        } catch (NullPointerException e) {
            return false;
        }
        if (u == null || r == null) {
            return false;
        }
        boolean rOpaque = r.isOpaque();
        boolean uOpaque = u.isOpaque();
        if (rOpaque != uOpaque) {
            return false;
        }
        if (rOpaque) {
            return TextUtils.equals(uri, redirectUri);
        }
        if (!TextUtils.equals(r.getScheme(), u.getScheme())) {
            return false;
        }
        if (!TextUtils.equals(r.getAuthority(), u.getAuthority())) {
            return false;
        }
        if (r.getPort() != u.getPort()) {
            return false;
        }
        if (!TextUtils.isEmpty(r.getPath()) && !TextUtils.equals(r.getPath(), u.getPath())) {
            return false;
        }
        Set<String> paramKeys = getQueryParameterNames(r);
        for (String key : paramKeys) {
            if (!TextUtils.equals(r.getQueryParameter(key), u.getQueryParameter(key))) {
                return false;
            }
        }
        String frag = r.getFragment();
        if (!TextUtils.isEmpty(frag)
                && !TextUtils.equals(frag, u.getFragment())) {
            return false;
        }
        return true;
    }

    public static Set<String> getQueryParameterNames(Uri uri) {
        if (uri.isOpaque()) {
            throw new UnsupportedOperationException("This isn't a hierarchical URI.");
        }

        String query = uri.getEncodedQuery();
        if (query == null) {
            return Collections.emptySet();
        }

        Set<String> names = new LinkedHashSet<String>();
        int start = 0;
        do {
            int next = query.indexOf('&', start);
            int end = (next == -1) ? query.length() : next;

            int separator = query.indexOf('=', start);
            if (separator > end || separator == -1) {
                separator = end;
            }

            String name = query.substring(start, separator);
            names.add(Uri.decode(name));

            // Move start to end of name
            start = end + 1;
        } while (start < query.length());

        return Collections.unmodifiableSet(names);
    }
}
