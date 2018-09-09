package pdftoword.pdfeditor.edit.pdf.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import pdftoword.pdfeditor.edit.pdf.R;


/**
 * Created by shailendra on 7/27/18.
 */

public class AppRate {
    private final static String APP_TITLE = "Pdf Searcher";// App Name
    private final static String APP_PNAME ="pdf.anypdf.pdfsearcher";// Package Name

    private final static int DAYS_UNTIL_PROMPT = 3;//Min number of days
    private final static int LAUNCHES_UNTIL_PROMPT = 3;//Min number of launches

    public static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }

        editor.commit();
    }

    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {
        final Dialog dialog = new Dialog(mContext);
        //dialog.setTitle("Rate " + APP_TITLE);
        View v= LayoutInflater.from(mContext).inflate(R.layout.rate_us_dialog,null,false);
        Button b1=v.findViewById(R.id.rate);
        Button b2=v.findViewById(R.id.later);
        TextView tv = new TextView(mContext);
        tv.setText("If " + APP_TITLE + " has helped you , please take a moment to rate it. Thanks for your support!");
        tv.setWidth(240);
        tv.setPadding(4, 0, 4, 10);
        //rate
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + APP_PNAME)));
                dialog.dismiss();
                editor.putBoolean("dontshowagain", true);
                editor.commit();
            }
        });

        //later
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

//        Button b3 = new Button(mContext);
//        b3.setText("No, thanks");
//        b3.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if (editor != null) {
//                    editor.putBoolean("dontshowagain", true);
//                    editor.commit();
//                }
//                dialog.dismiss();
//            }
//        });
       // ll.addView(b3);

        dialog.setContentView(v);
        dialog.show();
    }
}