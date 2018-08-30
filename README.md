# 🐨 KaoLaSDK_Data_Android #
sdk对应的服务端接入文档，请移步：  [考拉游戏平台sdk服务端接入文档](https://github.com/KoalaEatChicken/koalagamekit-ios/blob/master/docs/%E8%80%83%E6%8B%89%E6%B8%B8%E6%88%8F%E5%B9%B3%E5%8F%B0sdk%E6%9C%8D%E5%8A%A1%E7%AB%AF%E6%8E%A5%E5%85%A5%E6%96%87%E6%A1%A3%20v2.0.md)


# 考拉游戏平台SDK接入文档【Android端】v2.x
 +>本着简单、快速、稳妥、接入的原则，请贵方技术人员按如下流程接入。
 > (注：从2.0版本起，本项目已迁移至Android Studio,关于eclipse的版本不再维护。)
 > `sdk`可能会做有一些不定期的内部小优化和更新，但是不会修改cp接入的`api`，所以如果需要更新新版本的`sdk`，只需要替换aar库以及相关的资源文件即可~

+ 【必读！】sdk版本更新：若在**2018年7月12日前**已经接入了**旧sdk**，请**删除**游戏项目中的res里的考拉资源文件 以及其他相关的lib，按照本文档指示导入aar和必需导入的资源文件即可。此新版sdk的aar已包含res资源文件，无须再拷贝res文件，望接入方知晓。（SDK接入接口不变）

+ 【必读！】SDK的API接入:所有接口皆为必接，请勿遗漏。

`目录`
- [1、工程配置](#一-关于库lib和其他配置文件的引入)
- [2、生命周期接口](#二-生命周期接口必接)
- [3、初始化接口](#三-初始化接口必接)
- [4、登录接口](#四-登录接口必接)
- [5、切换账号/登出监听器](#五-切换账号登出监听器必接)
- [6、切换账号/登出接口](#六-切换账号登出接口必接)
- [7、角色信息提交接口](#七-角色信息提交接口必接)
- [8、充值接口](#八-充值接口必接)
- [9、退出接口](#九-退出接口必接)

### 一、 关于库，lib和其他配置文件的引入

1、引用kaolasdk.aar库，将其拷贝到libs。

2、配置build.gradle(APP)，参考demo工程接入。

```
android {
 	//。。。yours。。。
    repositories {
        flatDir {
            dirs 'libs'
        }
    }
}
dependencies {
    //。。。yours。。。
    compile(name: 'kaolasdk2.0', ext: 'aar')  //替换为所接入的aar名字,'kaolasdk2.0'--->yours 
}
```

3、将assets里的文件直接拷贝到游戏Android工程的assets目录中

4、Application配置权限

```html
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.CALL_PHONE" />
<uses-permission android:name="android.permission.SEND_SMS" />
<uses-permission android:name="android.permission.READ_SMS" />
<uses-permission android:name="android.permission.WRITE_SMS" />
<uses-permission android:name="android.permission.GET_TASKS" />
<uses-permission android:name="android.permission.REORDER_TASKS" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
<uses-permission android:name="android.permission.READ_LOGS" />
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
```

5、修改Application
1) 若**无自定义**的Application,则可以修改AndroidManifest.xml的Application，如下

```html
<application android:name="com.klsdk.common.KLApplication">
```

2) 若开发者**自定义**application，则自定义的application需继承**com.klsdk.common.KLApplication**

 ```html
<application android:name=”自定义Application">
 ```
6、在Application添加声明：Activity、service和receiver      (请勿漏接)

```html
<activity
android:name="com.klsdk.activity.KLLoginActivity"
android:configChanges="orientation|keyboardHidden|screenSize"
android:screenOrientation="landscape"
android:theme="@style/kl_Transparent" />
<activity
android:name="com.klsdk.activity.KLRegisterActivity"
android:configChanges="orientation|keyboardHidden|screenSize"
android:screenOrientation="landscape"
android:theme="@style/kl_Transparent" />
<activity
android:name="com.klsdk.activity.KLPaymentActivity"
android:configChanges="orientation|keyboardHidden|screenSize"
android:screenOrientation="landscape"
android:theme="@style/kl_Transparent" />
<activity
android:name="com.klsdk.activity.KLpayWebActivity"
android:configChanges="orientation|keyboardHidden|screenSize"
android:screenOrientation="landscape"
android:theme="@style/kl_Transparent" />
<activity
android:name="com.klsdk.activity.KLUserinfoActivity"
android:configChanges="orientation|keyboardHidden|screenSize"
android:screenOrientation="landscape"
android:theme="@style/kl_Transparent" />
<activity
android:name="com.klsdk.activity.KLForgetpasswordActivity"
android:configChanges="orientation|keyboardHidden|screenSize"
android:screenOrientation="landscape"
android:theme="@style/kl_Transparent" />

<service android:name="com.klsdk.push.PushService" />

<receiver android:name="com.klsdk.push.BootReceive" >
    <intent-filter android:priority="1000" >
        <action android:name="android.intent.action.BOOT_COMPLETED" />
        <action android:name="android.intent.action.USER_PRESENT" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</receiver>

```

### 二、 生命周期接口（必接）

代码示例：(请勿漏接)

```java
@Override
Protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    KLSDK.onCreate(this);
}
@Override
Protected void onActivityResult(intrequestCode, intresultCode, Intent data) {
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
Protected void onPause() {
    // TODO Auto-generated method stub
    super.onPause();
    KLSDK.onPause(this);
}
@Override
Protected void onRestart() {
    // TODO Auto-generated method stub
    super.onRestart();
    KLSDK.onRestart(this);
}
@Override
Protected void onResume() {
    // TODO Auto-generated method stub
    super.onResume();
    KLSDK.onResume(this);
}
@Override
Protected void onStop() {
    // TODO Auto-generated method stub
    super.onStop();
    KLSDK.onStop (this);
}
@Override
Protected void onNewIntent(Intent intent) {
    // TODO Auto-generated method stub
    super.onNewIntent(intent);
    KLSDK.onNewIntent(intent);
}
```



### 三、 初始化接口（必接）

代码示例

 ```java
// 初始化		
KLSDK.initInterface(MainActivity.this, appid, appkey,new InitListener() {
    @Override
    Public void Success(String msg) {
    // TODO Auto-generated method stub
    }
    @Override
    Public void fail(String msg) {
    // TODO Auto-generated method stub
    }
});
 ```

**参数说明**

|   **参数**   | **参数名称** | **类型** | **参数说明** | **是否****为空** |
| :----------: | :----------: | :------: | :----------: | :--------------: |
|   Context    |  上下文对象  | Context  |  上下文对象  |        否        |
|    appId     |    游戏id    |   int    |  游戏方的id  |        否        |
|    appKey    |   游戏密钥   |  String  |   加密密钥   |        否        |
| InitListener |   回调接口   |    \     |  初始化回调  |        否        |

**注：Appid, Appkey：在接入游戏的时候，【平台方】 会发给【 游戏方】**

> 接口会返回两种结果
>
> Success的方法体返回的信息是success和updatesuccess，证明初始化成功update，则需要游戏等待，正在更新游戏；
>
> Fail的方法体返回的信息是fail，说明初始化不成功
>

### 四、 登录接口（必接）

**注：必须初始化成功后调用**

代码示例

 ```java
// 登录
KLSDK.login(MainActivity.this, appid, appkey, new ApiListenerInfo() {
    @Override
    Public void onSuccess(Object obj) {
        // TODO Auto-generated method stub
        if (obj != null) {
            LoginMessageinfo data = (LoginMessageinfo) obj;
            String result = data.getResult();
            String msg = data.getMsg();
            String gametoken = data.getGametoken();
            String time = data.getTime();
            String uid = data.getUid();
            String sessid = data.getSessid();
            Log.i("kk", "登录结果:" + result + "|msg" + msg
                  + "|gametoken" + gametoken + "" + "|time"
                  + time + "|uid" + uid + "|sessid" + sessid);
        }
    }
});
 ```



**登录方法参数说明**

|    **参数**     | **参数名称** | **类型** | **参数说明** | **是否**为空 | **实例** |
| :-------------: | :----------: | :------: | :----------: | :----------: | -------- |
|      Appid      |    appid     |   int    |    游戏id    |      否      | 固定     |
|     appkey      |    appkey    |  String  |   游戏密钥   |      否      | 固定     |
| ApiListenerInfo |   回调接口   |    \     |   登录回调   |      否      | \        |

 

**返回结果参数说明**

| **参数**  | **参数名称** | **类型** |                **参数说明**                 | **是否**为空 |    **实例**    |
| :-------: | :----------: | :------: | :-----------------------------------------: | :----------: | :------------: |
|  result   |   登陆结果   |  String  | success/fail（成功返回success其他返回fail） |      否      |  success/fail  |
|    msg    | 登陆额外信息 |  String  |                  额外信息                   |      否      | 提示对应的信息 |
|    uid    |   用户uid    |  String  |                   用户uid                   |      否      |     10010      |
|   tiem    |    时间戳    |  String  |              用在登陆二次验证               |      否      |                |
| gametoken |    token     |  String  |              用在登陆二次验证               |      否      |                |
|  sessid   |    验证id    |  String  |              用在登陆二次验证               |      是      |                |

 

### 五、 切换账号/登出监听器（必接）

**注：必须保证玩家【任何时候】点击【切换账号按钮】，都能正常切换账号**

代码示例

 ```java
KLSDK.setUserListener(new UserApiListenerInfo() {
    @Override
    Public void onLogout(Object obj) {
        super.onLogout(obj);
        // 切换账号，处理自己的逻辑，比如重新登录，进行选服进入游戏
    }
});
 ```



### 六、 切换账号/登出接口（必接）

**说明：用于【用户切换账号等事件】的处理**

> a）若游戏中存在登出或者切换帐号的按钮，则可在点击按钮时进行登出接口调用，在登出回调中进行重新登录等操作；
>
> b）若游戏中不存在登出或者切换帐号的按钮时，建议修改游戏添加登出或切换帐号按钮。
>
> 代码示例
>

 ```java
KLSDK.switchAccount();
 ```



### 七、 角色信息提交接口（必接）
**需要提交的场景有：进入服务器(enterServer)、玩家创建用户角色(createRole)、玩家升级(levelUp)**
代码示例

 ```java
/* 
* @param context 上下文
* @param scene_Id   场景 分别为进入服务器(enterServer)、玩家创建用户角色(createRole)、玩家升级(levelUp)
* @param roleId 角色id
* @param roleName   角色名
* @param roleLevel 角色等级
* @param zoneId 当前登录的游戏区服id
* @param zoneName 当前游戏区服名称
* @param balance 游戏币余额
* @param vip 当前用户vip等级
* @param partyName 当前用户所属帮派
* @param roleCTime 单位为秒，创建角色的时间
* @param roleLevelMTime 单位为秒，角色等级变化时间
*/
//进入服务器
KLSDK.setExtData(this, "enterServer", "11", "kk", "99", "1", "1区","80",
                 "81", "逍遥", "21322220", "54456588");
//创建角色
KLSDK.setExtData(this, "createRole", "11", "kk", "99", "1", "1区","80",
                 "81", "逍遥","21322225", "54456890");
//角色升级
KLSDK.setExtData(this, "levelUp", "11", "kk", "99", "1", "1区",
				"80", "8", "逍遥", "21322333", "54457990");
 ```

**注意：**

> 请根据相应的scene_Id（场景）调用接口；
>
> scene_Id 的值请填写对应的值：
>
> 进入服器=enterServer、玩家创建用户角色=createRole、玩家升级=levelUp；
>
> 若某些参数获取不到，请填默认值""；

**参数说明 **

| 参数          | 参数名称         | 类型    | 参数说明                   | 是否为空 | 示例                                                         |
| ------------- | ---------------- | ------- | -------------------------- | -------- | ------------------------------------------------------------ |
| context       | 上下文           | Context | 上下文                     | 否       | this                                                         |
| scene_Id      | 场景             | String  | 玩家场景                   | 否       | 分别为进入服器(enterServer)、玩家创建用户角色(createRole)、玩家升级(levelUp) |
| roleId        | 角色id           | String  | 角色id                     | 否       | 112                                                          |
| roleName      | 角色名           | String  | 角色名                     | 否       | 盟友                                                         |
| roleLevel     | 角色等级         | String  | 角色等级                   | 否       | 99                                                           |
| zoneId        | 服务器id         | String  | 服务器id                   | 否       | 1                                                            |
| zoneName      | 服务器名称       | String  | 服务器名称                 | 否       | 剑魂                                                         |
| balance       | 游戏币余额       | String  | 游戏币余额                 | 是       | 12                                                           |
| Vip           | Vip等级          | String  | Vip等级                    | 否       | 1                                                            |
| partyName     | 所属帮派         | String  | 所属帮派                   | 是       | 醉江湖                                                       |
| roleCtiem     | 创建角色时间     | String  | 单位为秒，创建角色的时间   | 是       | 21322222                                                     |
| roleLeveMTimE | 角色等级变化时间 | String  | 单位为秒，角色等级变化时间 | 是       | 54456588                                                     |

### 八、 充值接口（必接）

代码示例

 ```java
PaymentInfo paymentInfo = new PaymentInfo();
paymentInfo.setAppid(appid);  //游戏id
paymentInfo.setAppKey(appkey);  //游戏密钥
paymentInfo.setAgent("");  //渠道
paymentInfo.setAmount("1");//金额(元)
paymentInfo.setBillno("");//订单主题
paymentInfo.setExtrainfo("");//额外参数
paymentInfo.setSubject("元宝");//主题
paymentInfo.setIstest("1");//是否测试
paymentInfo.setRoleid("1111");//
paymentInfo.setRolename("hh");//
paymentInfo.setRolelevel("100");//
paymentInfo.setServerid("8888");//
paymentInfo.setUid("");//
KLSDK.payment(this, paymentInfo, new ApiListenerInfo() {
    @Override
    Public void onSuccess(Object obj) {
        // TODO Auto-generated method stub
        super.onSuccess(obj);
        if (obj != null) {
            Log.i("kk", "充值界面关闭" + obj.toString());  //注意：只会返回close
            //回调接口只会返回支付界面关闭状态:close
        }
    }
});
 ```

**参数说明**

|   **参数**   | **参数名称** | **类型** | **参数说明** | **是否**为空 |            **实例**             |
| :----------: | :----------: | :------: | :----------: | :----------: | :-----------------------------: |
|   setAppid   |    游戏id    |   int    |    游戏id    |      否      |              固定               |
|  setAppkey   |   游戏密钥   |  String  |   游戏密钥   |      否      |              固定               |
|   setAgent   |     渠道     |  String  |     渠道     |      是      |               “”                |
|  setBillno   |    订单号    |  String  |    订单号    |      否      |        **查看备注（1）**        |
|  setAmount   |   订单金额   |  String  |   订单金额   |      是      | **（单位：元），查看备注（3）** |
|  setSubject  |   订单主题   |  String  |   订单主题   |      否      |              元宝               |
| setExtraInfo |   额外参数   |  String  | 订单额外信息 |      否      |        **查看备注（3）**        |
|    setUid    |   用户信息   |  String  |   用户信息   |      是      |      “” **查看备注（4）**       |
|  setIsTest   |   是否测试   |  String  |   测试参数   |      否      |      正式"0"      测试"1"       |
|  setRoleid   |    角色id    |  String  |    角色id    |      否      |               111               |
| setRolename  |    角色名    |  String  |    角色名    |      否      |            考拉大王             |
| setRolelevel |   角色等级   |  String  |   角色等级   |      否      |              99               |
| SetServerid  |   服务器id   |  String  |   服务器名   |      否      |           10           |

**备注**

> **(1) setBillNo:游戏订单号（商户订单号(12-32位数字、字母),不要使用特殊字符）**
>
> **(2) setExtraInfo：如果没有透传参数，直接传订单号**
>
> **(3) setAmount: 传整数，不要带小数点，如金额为：60**
>
> **(4) setUid: 传“”就行**

 

### 九、 退出接口（必接）

示例代码，调用后会有对话窗，确定退出后会有回调。

```java
/*
*@param activity 当前Activity
*@param exitListener 退出回调
*@method fail 取消的回调
*@method ExitSuccess 确定退出的回调
*/
KLSDK.exit(this, new ExitListener() {
    @Override
    Public void ExitSuccess(String msg) {
        // TODO Auto-generated method stub
        //退出成功，cp自行处理退出逻辑
        System.exit(0);
    }
    @Override
    Public void fail(String msg) {
        // TODO Auto-generated method stub
         //退出失败，cp自行处理退出逻辑
    }
});

```



