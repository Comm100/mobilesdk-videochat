package xwalkviewchatwindow.com100.com.comm100_livechat_android_xwalk_sample;

/**
 * Created by Jovi on 2018/1/01.
 */

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.view.KeyEvent;
import android.webkit.ValueCallback;
import  android.util.Log;

import org.xwalk.core.ClientCertRequest;
import org.xwalk.core.XWalkHttpAuthHandler;
import org.xwalk.core.XWalkSettings;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;
import org.xwalk.core.XWalkPreferences;
import  org.xwalk.core.XWalkResourceClient;
import  org.xwalk.core.XWalkJavascriptResult;
import org.xwalk.core.XWalkWebResourceRequest;
import org.xwalk.core.XWalkWebResourceResponse;
import org.xwalk.core.JavascriptInterface;
import  android.net.http.SslError;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

@SuppressWarnings("deprecation")
@SuppressLint("SetJavaScriptEnabled")
public class VisitorXWalkActivity extends Activity{

    private static final String TAG = "Comm100 Visitor Client";
    private int _siteId = 0;
    private int _planId = 0;
    private String _chatServer = "https://chatserver.comm100.com";
    private  boolean _debug=false;
    private XWalkView mXWalkView;

    private int _myWidth=640;
    private int _myHeight=480;
    private int  _myType=0;
    //常量
    public static final String SYS_EMUI = "sys_emui";
    public static final String SYS_MIUI = "sys_miui";
    public static final String SYS_FLYME = "sys_flyme";
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
    private static final String KEY_EMUI_API_LEVEL = "ro.build.hw_emui_api_level";
    private static final String KEY_EMUI_VERSION = "ro.build.version.emui";
    private static final String KEY_EMUI_CONFIG_HW_SYS_VERSION = "ro.confg.hw_systemversion";

    public static String getSystem(){
        String SYS="";
        try {
            Properties prop= new Properties();
            prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            if(prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null){
                SYS = SYS_MIUI;//小米
            }else if(prop.getProperty(KEY_EMUI_API_LEVEL, null) != null
                    ||prop.getProperty(KEY_EMUI_VERSION, null) != null
                    ||prop.getProperty(KEY_EMUI_CONFIG_HW_SYS_VERSION, null) != null){
                SYS = SYS_EMUI;//华为
            }else if(getMeizuFlymeOSFlag().toLowerCase().contains("flyme")){
                SYS = SYS_FLYME;//魅族
            };
        } catch (IOException e){
            e.printStackTrace();
            return SYS;
        }
        return SYS;
    }

    public static String getMeizuFlymeOSFlag() {
        return getSystemProperty("ro.build.display.id", "");
    }

    private static String getSystemProperty(String key, String defaultValue) {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method get = clz.getMethod("get", String.class, String.class);
            return (String)get.invoke(clz, key, defaultValue);
        } catch (Exception e) {
        }
        return defaultValue;
    }
    //js接口
    public class JsInterface {
        public JsInterface() {
        }
// width:{exact:640},height:{exact:480}
        @JavascriptInterface
        public int getCrossWidth() {
            return _myWidth;
        }
        @JavascriptInterface
        public int getCrossHeight() {
            return _myHeight;
        }
        @JavascriptInterface
        public int getMobleType() {
            return _myType;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

        //XWalkPreferences

        mXWalkView =new XWalkView(this);
        setContentView(mXWalkView);
        Intent intent = getIntent();
        _siteId = intent.getIntExtra("site_id", _siteId);
        _planId = intent.getIntExtra("plan_id", _planId);
        _chatServer = intent.getStringExtra("chat_server");
        //debug  model
        _debug = intent.getBooleanExtra("debug",_debug);
        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING,_debug);


        mXWalkView.setUIClient(new XWalkUIClient(mXWalkView) {
            @Override
            public void onPageLoadStarted(XWalkView view, String url) {
                super.onPageLoadStarted(view, url);
            }

            @Override
            public boolean onJsAlert(XWalkView view, String url, String message, XWalkJavascriptResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public void onScaleChanged(XWalkView view, float oldScale, float newScale) {
                super.onScaleChanged(view, oldScale, newScale);
            }

            @Override
            public void onPageLoadStopped(XWalkView view, String url, LoadStatus status) {
                super.onPageLoadStopped(view, url, status);
            }
  /*          @Override
            public  void onPermissionRequest(final PermissionRequest request)
            {
                Log.d(TAG,"OnPermissionRequest");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        request.grant(request.getResources());
                    }
                });

            }*/

        } );

        mXWalkView.setResourceClient(new XWalkResourceClient(mXWalkView) {

                                       //=========HTML5定位==========================================================
                                       //需要先加入权限
                                       //<uses-permission android:name="android.permission.INTERNET"/>
                                       //<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
                                       //<uses-permission android:name=
                                       // "android.permission.ACCESS_COARSE_LOCATION"/>

                                       @Override
                                       public boolean shouldOverrideUrlLoading(XWalkView view, String url) {
                                           view.load(url,null);
                                           return true;
                                       }

                                       @Override
                                       public void onReceivedSslError(XWalkView view, ValueCallback<Boolean> callback, SslError error) {
                                           super.onReceivedSslError(view, callback, error);
                                       }

                                       @Override
                                       public void onLoadFinished(XWalkView view, String url) {
                                           super.onLoadFinished(view, url);
                                       }

                                       @Override
                                       public void onLoadStarted(XWalkView view, String url) {
                                           super.onLoadStarted(view, url);
                                       }

                                       @Override
                                       public void onProgressChanged(XWalkView view, int progressInPercent) {
                                           super.onProgressChanged(view, progressInPercent);
                                       }

                                       @Override
                                       public void onReceivedClientCertRequest(XWalkView view, ClientCertRequest handler) {
                                           super.onReceivedClientCertRequest(view, handler);
                                       }

                                       @Override
                                       public void onDocumentLoadedInFrame(XWalkView view, long frameId) {
                                           super.onDocumentLoadedInFrame(view, frameId);
                                       }

                                       @Override
                                       public void onReceivedHttpAuthRequest(XWalkView view, XWalkHttpAuthHandler handler, String host, String realm) {
                                           super.onReceivedHttpAuthRequest(view, handler, host, realm);
                                       }

                                       @Override
                                       public void onReceivedLoadError(XWalkView view, int errorCode, String description, String failingUrl) {
                                           super.onReceivedLoadError(view, errorCode, description, failingUrl);
                                       }

                                       @Override
                                       public void onReceivedResponseHeaders(XWalkView view, XWalkWebResourceRequest request, XWalkWebResourceResponse response) {
                                           super.onReceivedResponseHeaders(view, request, response);
                                       }
                                   }
        );
        XWalkSettings webSettings = mXWalkView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(false);
        webSettings.setAllowUniversalAccessFromFileURLs(false);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        reloadChatWindow();
    }



    public void reloadChatWindow(){
        if(mXWalkView == null)
            return;

        String url = _chatServer + "/chatwindow.aspx?siteId=" + _siteId + "&planId=" + _planId;
        if(_chatServer.contains("ngrok.io"))
        {
            url=_chatServer + "/?siteId=" + _siteId + "&planId=" + _planId;
        }

/*        mXWalkView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mXWalkView.load(url ,null);
            }
        }, 500);*/

        // load page

            //小米手机
            if(getSystem()==SYS_MIUI)
            {
               /* if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M)
                {*/
                _myType=1;
                _myWidth=640;
                _myHeight=480;
               /* }*/
            }
            else
            {
                _myType=0;
            }
        //注入js
        mXWalkView.addJavascriptInterface(new JsInterface(), "NativeInterface");
        mXWalkView.load(url,null);
        //https://webrtc.github.io/samples/src/content/getusermedia/gum/
        //resolution
        // https://www.webrtc-experiment.com/websocket/
        //https://e6364e4f.ngrok.io/src/content/getusermedia/gum/
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(mXWalkView!=null) {
            mXWalkView.resumeTimers();
            mXWalkView.onShow();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(mXWalkView!=null)
        {
            mXWalkView.pauseTimers();
            mXWalkView.onHide();
        }

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(mXWalkView!=null)
        {
            mXWalkView.onDestroy();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.d(TAG, "onActivityResult");

        if(mXWalkView!=null)
        {
            mXWalkView.onActivityResult(requestCode,resultCode,data);
        }

    }

    @Override
    protected void onNewIntent(Intent intent){
        //if(!mChatWindow.onBackPressed()){return;}
        if(mXWalkView!=null)
        {
            mXWalkView.onNewIntent(intent);
        }
    }
    @Override
    public  boolean onKeyUp(int keyCode,KeyEvent event)
    {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            String mjs="javascript:var aEvent=document.createEvent(\"" + "HTMLEvents" + "\");" +
                    " aEvent.initEvent(\""+ "beforeunload" +   "\",true,true);" +
                    " aEvent.eventType=\""+ "message" +   "\"; document.dispatchEvent(aEvent);";
            mXWalkView.evaluateJavascript(mjs,null);
            // return true;
        }
        return  super.onKeyUp(keyCode,event);
    }
    /*    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // TODO Auto-generated method stub
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
           // return false;
        }
        return super.dispatchKeyEvent(event);
    }*/
}