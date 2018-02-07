package xwalkviewchatwindow.com100.com.comm100_livechat_android_xwalk_sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {
    private  Button mChatButton=null;
    private EditText mSiteId=null;
    private EditText mPlanId=null;
    private EditText mChatServer=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSiteId=(EditText)findViewById(R.id.ed_siteId);
        mPlanId=(EditText)findViewById(R.id.ed_planId);
        mChatServer=(EditText)findViewById(R.id.ed_chatserver);

        mChatButton=(Button)findViewById(R.id.btn_chat_now);
        mChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //your site id
                int siteId=Integer.parseInt(mSiteId.getText().toString());
                // your code plan id
                int planId=Integer.parseInt(mPlanId.getText().toString());
                //chat server path,shared platform is https://chatserver.comm100.com
                // enterprise is https://ent.comm100.com/chatserver
                String chatServer=mChatServer.getText().toString();

                Intent intent=new Intent(MainActivity.this,VisitorXWalkActivity.class);

                intent.putExtra("site_id",siteId);
                intent.putExtra("plan_id",planId);
                intent.putExtra("chat_server",chatServer);
                intent.putExtra("debug",true);//是否调试  to debug
                startActivity(intent);


            }
        });

    }
}
