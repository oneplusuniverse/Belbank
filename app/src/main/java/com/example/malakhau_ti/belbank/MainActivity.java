package com.example.malakhau_ti.belbank;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout swipeRefreshLayout;
    private CharSequence mTitle;
    private WebView wv;
    private String LASTURL = "";
    public String htmlContent;
    boolean isloggedin = false;

    // init your codes array here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.green, R.color.red, R.color.yellow,  R.color.blue );

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
//                if(!isloggedin){
                view.loadUrl("javascript:(function() { " +
                        "document.getElementById('userID').value='"+(String)YourLogin+"';" +
                        "document.getElementById('password').value='"+(String)YourPassword+"';" +
                        "this.disabled=true;document.forms.LoginForm1.bbIbLoginAction.value='in-action';document.forms.LoginForm1.submit();" +
                        "})()");

                wv.loadUrl("javascript:(function() { " +
                        "javascript:window.HTMLOUT.processHTML(document.getElementsByTagName('span')[0].innerHTML.replace(/\\D+/g,\"\"));" +
                        "})()");

                if(htmlContent!=null){
                 wv.loadUrl("javascript:(function() { " +
                        "document.getElementById('codevalue').value='"+String.valueOf(codes[Integer.valueOf(htmlContent)-1])+"';"+
                        "this.disabled=true;document.forms.LoginForm1.bbIbLoginAction.value='in-action';document.forms.LoginForm1.submit();"+
                        "})()");
                Toast toast = Toast.makeText(getApplicationContext(),
                        String.valueOf(codes[Integer.valueOf(htmlContent)-1]), Toast.LENGTH_SHORT);
                toast.show();
                    isloggedin = true;
                   // htmlContent=null;
                }
                swipeRefreshLayout.setRefreshing(false);
//                }
            }
        });
        wv.loadUrl("https://ibank.asb.by");
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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        wv.loadUrl("https://ibank.asb.by");
    }
}
