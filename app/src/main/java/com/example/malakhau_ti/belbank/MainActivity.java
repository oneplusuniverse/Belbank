package com.example.malakhau_ti.belbank;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener{


    private SwipeRefreshLayout swipeRefreshLayout;
    private WebView wv;
    public String htmlContent;
    boolean isloggedin;
    SharedPreferences sPref;
    private static final String LOGIN  = "Saved_Login";
    private static final String PASSWORD = "Saved_Pass";
    private static final String IS_USER_EXISTING = "Is_User_Existing";
    int trig=0;
    MyTask mt;
    boolean isdatafilled;
    String thislogin;
    String thispassword;
    String[] codes = new String[40];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.green, R.color.red, R.color.blue);
        parent.setContentInsetsAbsolute(0,0);
        loadUserData();
        isloggedin = false;
        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = sPref.edit();
        String login = sPref.getString(LOGIN, "");
        String pass = sPref.getString(PASSWORD, "");

        if(login!=null&&pass!=null){
            ed.putBoolean(IS_USER_EXISTING, true);
            ed.commit();
        }

        for(int i = 0;i<40;i++){
            codes[i] = sPref.getString("code"+String.valueOf(i), "none");
        }

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
        wv.getSettings().setDomStorageEnabled(true);
        wv.setWebChromeClient(new WebChromeClient());
        wv.setInitialScale(1);

        wv.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                swipeRefreshLayout.setRefreshing(false);
                Intent intent = getIntent();
                finish();
                startActivity(intent);
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
                view.getSettings().setLoadsImagesAutomatically(false);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (isloggedin == false) {
                    view.loadUrl("javascript:(function() { " +
                            "document.getElementById('userID').value='" + thislogin + "';" +
                            "document.getElementById('password').value='" + thispassword + "';" +
                            "this.disabled=true;document.forms.LoginForm1.bbIbLoginAction.value='in-action';document.forms.LoginForm1.submit();" +
                            "})()");


                    view.loadUrl("javascript:(function() { " +
                            "javascript:window.HTMLOUT.processHTML(document.getElementsByTagName('span')[0].innerHTML.replace(/\\D+/g,\"\"));" +
                            "})()");
//
                    if (htmlContent != null) {
                        if (codes[Integer.valueOf(htmlContent) - 1] != "none") {
//

                            view.loadUrl("javascript:(function() {" +
                                    "document.getElementById('codevalue').value='" + String.valueOf(codes[Integer.valueOf(htmlContent) - 1]) + "';" +
                                    "this.disabled=true;document.forms.LoginForm1.bbIbLoginAction.value='in-action';document.forms.LoginForm1.submit();" +
                                    "})()");

//                    TODO add javascript to convert web page content V
                            view.loadUrl("javascript:(function() { " +
                                    "hide('header');"+
                                    "hide('footer');"+
//                                    "hide('menu');"+
//                                    "document.getElementsByClassName('component-container contentMain ibmDndColumn id-Z7_0AG41KO0JOFV20AC1O152V30G2')[0].style.visibility='hidden';"+
                                    "function hide(id){if (document.getElementById(id)){document.getElementById(id).style['display'] = 'none';}}"+
                                    "})()");

                            if (trig > 1) {
                                findViewById(R.id.wv).setVisibility(View.VISIBLE);
                                findViewById(R.id.progressBar).setVisibility(View.GONE);
                                swipeRefreshLayout.setRefreshing(false);
                                isloggedin = true;

                            }
                            trig++;
                        }
                    }

                }
            }
        });
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
//                    .setIcon(R.drawable.belbank_purple_mini)
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

    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            wv.loadUrl("https://ibank.asb.by");

        }
    }
    @Override
    public void onRefresh() {
        findViewById(R.id.wv).setVisibility(View.GONE);
        boolean isdatafilled = true;
        loadUserData();
        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        for (int i = 0; i < 40; i++) {
            if (sPref.getString("code"+String.valueOf(i),"none").equals("none")) {
                isdatafilled = false;
                Toast.makeText(getApplicationContext(),
                        String.valueOf(codes[i])+" - вот этого не должно быть", Toast.LENGTH_SHORT).show();

            }
        }
        if (isdatafilled == true){
            trig = 0;
            CookieSyncManager.createInstance(this);
            CookieManager cookieManager = CookieManager.getInstance();


            if (isloggedin == false) {
                wv.loadUrl("https://ibank.asb.by");
                mt = new MyTask();
                mt.execute();

            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    cookieManager.removeAllCookies(null);
                    isloggedin = false;
                } else {
                    cookieManager.removeAllCookie();
                    isloggedin = false;
                }
                swipeRefreshLayout.setRefreshing(false);
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Вы чо, алё!")
                    .setMessage("Введите все коды, иначе приложение попытается залогиниться пустым значением и система заблокирует вас к херам!")
                    .setCancelable(false)
                    .setNegativeButton("Заполнить",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
                                    MainActivity.this.startActivity(myIntent);
                                    overridePendingTransition(R.anim.center_to_left, R.anim.right_to_center);
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    void loadUserData() {

        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        thislogin = sPref.getString(LOGIN, "none");
        thispassword = sPref.getString(PASSWORD, "none");
        for(int i = 0;i<40;i++){
            codes[i] = sPref.getString("code"+String.valueOf(i),"none");
            if(codes[i].equals("none")){
                isdatafilled = false;
            }
        }
    }
}
