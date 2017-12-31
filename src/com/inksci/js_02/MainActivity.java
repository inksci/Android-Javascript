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
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��������

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//ȥ����Ϣ��
        
        setContentView(R.layout.activity_main);
        
        mWebView = (WebView) findViewById(R.id.webView);
        //�򿪱�����assetsĿ¼�µ�index.html�ļ�
        mWebView.loadUrl("file:///android_asset/index.html");
        
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
                }
        });
        
        //1����������ִ�е�JS�ű�
        mWebView.getSettings().setJavaScriptEnabled(true);
        
//        ��������������
//        requestFocus();
//        ȡ��������
//        this.setScrollBarStyle(SCROLLBARS_OUTSIDE_OVERLAY);
        
        mInterface = new WebAPPInterface(this);
        //2�����ͨ�Žӿ�
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

        //js����as
        @JavascriptInterface  //������һ�仰����Ȼ�Ļ��ڸ߰汾��ʱ���п��ܱ���
        public void sayHello(String name) {
            Toast.makeText(mContext, "name= " + name, Toast.LENGTH_SHORT).show();
        }

        //as����js

        public void showName(final String name) {
            //Ҫ�����߳��и���ui����������ʹ����runOnUiThread�����߳�
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:showName('" + name + "')");
                }
            });
        }
    }
    
    
    
}
