package com.inksci.js_02;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
    private WebView mWebView;

    private Button mButton;

    private WebAPPInterface mInterface;
    @SuppressLint("SetJavaScriptEnabled") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        
        setContentView(R.layout.activity_main);
        
        mWebView = (WebView) findViewById(R.id.webView);
        //打开本包内assets目录下的index.html文件
        mWebView.loadUrl("file:///android_asset/index.html");
        
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
                }
        });
        
        //1、设置允许执行的JS脚本
        mWebView.getSettings().setJavaScriptEnabled(true);
        
//        触摸焦点起作用
//        requestFocus();
//        取消滚动条
//        this.setScrollBarStyle(SCROLLBARS_OUTSIDE_OVERLAY);
        
        mInterface = new WebAPPInterface(this);
        //2、添加通信接口
        mWebView.addJavascriptInterface(mInterface, "AndroidApp");

        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
	         public void onClick(View v) {
	        	 //do something
	        	 mInterface.showName("hello javascript");
	         }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
    
    

    class WebAPPInterface {

        public Context mContext;

        public WebAPPInterface(Context context) {
            this.mContext = context;
        }

        //js调用as
        @JavascriptInterface  //加上这一句话，不然的话在高版本的时候有可能报错
        public void sayHello(String name) {
            Toast.makeText(mContext, "name= " + name, Toast.LENGTH_SHORT).show();
        }

        //as调用js

        public void showName(final String name) {
            //要在子线程中更新ui，这里我们使用了runOnUiThread（）线程
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:showName('" + name + "')");
                }
            });
        }
    }
    
    
    
}
