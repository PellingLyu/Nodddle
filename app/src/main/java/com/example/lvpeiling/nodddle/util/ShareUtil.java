package com.example.lvpeiling.nodddle.util;

import android.content.Context;
import android.content.Intent;

/**
 * Created by lvpeiling on 2017/5/23.
 */
public class ShareUtil {
    public static boolean shareUrl(Context context,String subject,String text,String url){
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);
        share_intent.setType("text/plain");
        share_intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        share_intent.putExtra(Intent.EXTRA_TEXT, text + url);
        share_intent = Intent.createChooser(share_intent, "share");
        context.startActivity(share_intent);
        return false;
    }
}
