package com.kksofts.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.klsdk.common.ApiListenerInfo;
import com.klsdk.common.ExitListener;
import com.klsdk.common.InitListener;
import com.klsdk.common.KLSDK;
import com.klsdk.common.UserApiListenerInfo;
import com.klsdk.model.LoginMessageinfo;
import com.klsdk.model.PaymentInfo;


public class MainActivity extends Activity implements View.OnClickListener{

    public Button mBtninit;
    public Button mBtnlogin;
    public Button mBtninfo;
    public Button mBtnpay;
    public Button mBtnexit;
    public Button mBtwwtichAc;
    public Button mBtlogoutAc;
    private int appid = 100000;
    private String appkey = "123456";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        KLSDK.onCreate(this);
        initView();

        //登出/切换账号的回调.
        //【注意:】考拉不会自己拉起登录界面.
        KLSDK.setUserListener(new UserApiListenerInfo() {

            @Override
            public void onLogout(Object obj) {
                // TODO Auto-generated method stub
                //obj:switchAccount
                // 登出/切换账号，处理自己的逻辑，比如重新登录，进行选服进入游戏
                Toast.makeText(MainActivity.this, obj.toString() + "登出",
                        Toast.LENGTH_LONG).show();
            }

        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()){
            case R.id.initbt:
                // 初始化
                kaolainit();
                break;
            case R.id.loginbt:
                // 登录
                kaolaLogin();
                break;
            case R.id.info:
                // 提交额外信息
                kaolaSetInfo();
                break;
            case R.id.paybt:
                // 支付
                kaolaPay();
                break;
            case R.id.switchAc:
                // 切换账号
                KLSDK.switchAccount();
                break;
            case R.id.exitbt:
                // 退出游戏.
                kaolaexit();
                break;
        }
    }

    private void kaolainit(){
        KLSDK.initInterface(MainActivity.this, appid, appkey,
                new InitListener() {

                    @Override
                    public void fail(String msg) {
                        // TODO Auto-generated method stub
                        //在此处理初始化失败
                        Toast.makeText(MainActivity.this, "初始化失败", Toast.LENGTH_SHORT).show();
                        Log.i("kl",msg);
                    }

                    @Override
                    public void Success(String msg) {
                        // TODO Auto-generated method stub
                        Toast.makeText(MainActivity.this, "初始化成功", Toast.LENGTH_SHORT).show();
                        Log.i("kl","初始化成功"+msg);
                    }
                });
    }


    private void kaolaPay(){
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setAppid(appid);
        paymentInfo.setAppKey(appkey);
        paymentInfo.setAgent("");
        paymentInfo.setAmount("1");
        paymentInfo.setBillno("");
        paymentInfo.setExtrainfo("");
        paymentInfo.setSubject("元宝");
        paymentInfo.setIstest("1");
        paymentInfo.setRoleid("1111");
        paymentInfo.setRolename("wz");
        paymentInfo.setRolelevel("100");
        paymentInfo.setServerid("8888");

        paymentInfo.setUid("");
        KLSDK.payment(this, paymentInfo, new ApiListenerInfo() {

            @Override
            public void onSuccess(Object obj) {
                // TODO Auto-generated method stub
                super.onSuccess(obj);
                if (obj != null) {
                    // LoginMessageInfo kaolaLogin=(LoginMessageInfo) obj;
                    Log.i("kl", "充值界面关闭" + obj.toString());
                }

            }

        });
    }



    private void kaolaLogin(){
        KLSDK.login(MainActivity.this, appid, appkey, new ApiListenerInfo() {

            @Override
            public void onSuccess(Object obj) {
                // TODO Auto-generated method stub
                if (obj != null) {
                    LoginMessageinfo data = (LoginMessageinfo) obj;
                    String result = data.getResult();
                    String msg = data.getMsg();
                    String gametoken = data.getGametoken();
                    String time = data.getTime();
                    String uid = data.getUid();
                    String sessid = data.getSessid();
                    Log.i("kl", "登录结果:" + result + "|msg" + msg
                            + "|gametoken" + gametoken + "" + "|time"
                            + time + "|uid" + uid + "|sessid" + sessid);
                    Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void kaolaSetInfo(){
        /**
         * 额外信息
         *
         * @param context
         *            上下文
         * @param scene_Id
         *            场景 分别为进入服务器(enterServer)、玩家创建用户角色(createRole)、玩家升级(levelUp)
         * @param roleId
         *            角色id
         * @param roleName
         *            角色名
         * @param roleLevel
         *            角色等级
         * @param zoneId
         *            当前登录的游戏区服id
         * @param zoneName
         *            当前游戏区服名称
         * @param balance
         *            游戏币余额
         * @param Vip
         *            当前用户vip等级
         * @param partyName
         *            当前用户所属帮派
         * @param roleCTime
         *            单位为秒，创建角色的时间
         * @param roleLevelMTime
         *            单位为秒，角色等级变化时间
         */
        KLSDK.setExtData(this, "enterServer", "11", "王者", "99", "1", "1区",
                "80", "8", "逍遥", "21322222", "54456588");
    }

    private void kaolaexit(){
        KLSDK.exit(this, new ExitListener() {

            @Override
            public void fail(String msg) {
                // TODO Auto-generated method stub

            }

            @Override
            public void ExitSuccess(String msg) {
                // TODO Auto-generated method stub
                System.exit(0);
                MainActivity.this.finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        KLSDK.onActivityResult(MainActivity.this, requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        KLSDK.onDestroy(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        KLSDK.onPause(this);
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        KLSDK.onRestart(this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        KLSDK.onResume(this);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        KLSDK.onStop(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        KLSDK.onNewIntent(intent);
    }


    private void initView() {
        // TODO Auto-generated method stub
        mBtninit = (Button) findViewById(R.id.initbt);
        mBtnlogin = (Button) findViewById(R.id.loginbt);
        mBtninfo = (Button) findViewById(R.id.info);
        mBtnpay = (Button) findViewById(R.id.paybt);
        mBtnexit = (Button) findViewById(R.id.exitbt);
        mBtwwtichAc = (Button) findViewById(R.id.switchAc);

        mBtninit.setOnClickListener(this);
        mBtnlogin.setOnClickListener(this);
        mBtninfo.setOnClickListener(this);
        mBtnpay.setOnClickListener(this);
        mBtnexit.setOnClickListener(this);
        mBtwwtichAc.setOnClickListener(this);
    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            kaolaexit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
