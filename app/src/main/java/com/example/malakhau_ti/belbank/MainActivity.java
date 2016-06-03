package com.example.malakhau_ti.belbank;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout swipeRefreshLayout;
    private CharSequence mTitle;
    private WebView wv;
    private String LASTURL = "";
    public String htmlContent;
    boolean isloggedin = false;
    SharedPreferences sPref;
    private static final String LOGIN  = "Saved_Login";
    private static final String PASSWORD = "Saved_Pass";
    private static final String IS_USER_EXISTING = "Is_User_Existing";

    String thislogin;
    String thispassword;
    // init your codes array here
    String[] codes = new String[40];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadUserData();

        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.action_bar_custom_view, null);

        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText("Беларусбанк");

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        Toolbar parent =(Toolbar) mCustomView.getParent();
        parent.setContentInsetsAbsolute(0,0);

        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = sPref.edit();
        String login = sPref.getString(LOGIN, "");
        String pass = sPref.getString(PASSWORD, "");
        if(login!=null&&pass!=null){
            ed.putBoolean(IS_USER_EXISTING, true);
            ed.commit();
        }

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.green, R.color.red, R.color.blue);

       class MyJavaScriptInterface {
            @JavascriptInterface
            @SuppressWarnings("unused")
            public void processHTML(String html) {
                htmlContent = html;
            }
        }

        wv = (WebView) findViewById(R.id.wv);

        WebSettings webSettings = wv.getSettings();
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        wv.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");

        wv.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getApplicationContext(), "Error: " + description + " " + failingUrl, Toast.LENGTH_LONG).show();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.indexOf("asb") <= 0) {
//                    // the link is not for a page on my site, so launch another Activity that handles URLs
                    return true;
                }
                return false;
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                LASTURL = url;

                view.getSettings().setLoadsImagesAutomatically(false);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
//                if(){
                view.loadUrl("javascript:(function() { " +
                        "document.getElementById('userID').value='" + thislogin + "';" +
                        "document.getElementById('password').value='" + thispassword + "';" +
//                        "this.disabled=true;document.forms.LoginForm1.bbIbLoginAction.value='in-action';document.forms.LoginForm1.submit();" +
                        "})()");

                wv.loadUrl("javascript:(function() { " +
                        "javascript:window.HTMLOUT.processHTML(document.getElementsByTagName('span')[0].innerHTML.replace(/\\D+/g,\"\"));" +
                        "})()");

                if (htmlContent != null) {
                    if(codes[Integer.valueOf(htmlContent)-1]!="none"){
                    wv.loadUrl("javascript:(function() { " +
                            "document.getElementById('codevalue').value='" + String.valueOf(codes[Integer.valueOf(htmlContent) - 1]) + "';" +
                            "this.disabled=true;document.forms.LoginForm1.bbIbLoginAction.value='in-action';document.forms.LoginForm1.submit();" +
                            "})()");
                    Toast.makeText(getApplicationContext(),
                            String.valueOf(codes[Integer.valueOf(htmlContent) - 1]), Toast.LENGTH_SHORT).show();
//                    toast.show();
                    isloggedin = true;
                    // htmlContent=null;
                    }
                }
                swipeRefreshLayout.setRefreshing(false);
//                }
            }
        });
//        wv.loadUrl("https://ibank.asb.by");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
            MainActivity.this.startActivity(myIntent);
            overridePendingTransition(R.anim.center_to_left,R.anim.right_to_center);
        }
        if (id == R.id.action_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("О приложении")
                    .setMessage(R.string.about)
                    .setIcon(R.drawable.belbank_mini)
                    .setCancelable(false)
                    .setNegativeButton("Закрыть",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        loadUserData();
        wv.loadUrl("https://ibank.asb.by");
    }

    void loadUserData() {

        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        thislogin = sPref.getString(LOGIN, "none");
        thispassword = sPref.getString(PASSWORD, "none");
        for(int i = 0;i<40;i++){
            codes[i] = sPref.getString("code"+String.valueOf(i),"none");
        }
    }
}
