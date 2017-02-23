# Dasudian IM Java and Android SDK API 文档

| Date | Version | Note |
|---|---|---|
| 2/13/2017 | 1.1.0 | 1. 修改了连接时发送用户信息的方式<br/> 2. 新的demo使用Android Studio编写，去掉了过去没用的API<br/> 3.更新了API文档，添加了SDK重连机制部分，并添加了javadoc文档<br/> 4. 断线重连后不在调用onConnect<br/>5. 加入组的回调函数添加了一个表示该组的参数，方便在加入组成功后，执行相应的操作|


## 集成前准备

将libs目录下的im-java-sdk-x.x.x.jar复制到你的工程的libs目录下。
在Android Studio的build.gradle中加入下面的内容，然后同步工程。

```
apply plugin: 'com.android.application'

android {
    ...
    // 如果编译时LICENSE重复的错误，则加入下面的内容
    packagingOptions {
        exclude 'META-INF/LICENSE'
    }
}

dependencies {
	...
    compile fileTree(include: ['*.jar'], dir: 'libs')
    compile 'com.squareup.okhttp3:okhttp:3.4.1'
    compile 'com.fasterxml.jackson.core:jackson-core:2.7.3'
    compile 'com.fasterxml.jackson.core:jackson-databind:2.7.3'
}
```

## SDK重连机
在第一次连接服务器时，如果由于网络问题导致连接失败，SDK会自动重连。在连接成功后，如果中途与
服务器的连接断开了，SDK也会自动重连。重连的机制是：如果重连失败，则在2s后再次重连，
如果失败则在4s，8s,16s...64s后再次重连，最多间隔64s。

## 初始化SDK并连接服务器

使用Connection的Builer获得一个Connection实例。

```
public static class Builder {

	......

	/**
	 * 通过builder方式生成Connection实例
	 * @param serverInfo 服务端信息
	 * @param userInfo 用户个人信息
	 */
	public Builder(ServerInfo serverInfo, UserInfo userInfo) {	
	}

	/**
	 * 设置AUC证书
	 * @param aucCertificate AUC证书输入流
	 * @return
	 */
	public Builder setAucCertificate(InputStream aucCertificate) {
	}

	/**
	 * 设置IM证书
	 * @param imCertificate IM证书输入流
	 * @return
	 */
	public Builder setIMCertificate(InputStream imCertificate) {
	}

	/**
	 * 设置连接状态回调函数
	 * @param callback 回调函数
	 * @return
	 */
	public Builder setCallback(ConnectionCallback callback) {
	}

	/**
	 * 设置私有云AUC服务器地址
	 * @param url AUC地址
	 * @return
	 */
	public Builder setAucURL(URL url) {
	}
	
	/**
	 * 设置AUC是否使用系统默认的证书
	 * @param defaultCertificate
	 */
	public Builder setAucDefaultCertificate(boolean aucDefaultCertificate) {
	}
	
	/**
	 * 设置IM是否使用默认的证书
	 * @param imDefaultCertificate
	 * @return
	 */
	public Builder setIMDefaultCertificate(boolean imDefaultCertificate) {
	}

	/**
	 * 连接服务器并返回Connection对象
	 * @return Connection 成功返回Connection对象
	 * @throws ServiceException 连接失败时抛出异常
	 */
	public Connection build() throws ServiceException {
	}
	
	/**
	 * 获取服务端信息
	 * @return
	 */
	public ServerInfo getServerInfo() {
	}

	/**
	 * 获取用户信息
	 * @return
	 */
	public UserInfo getUserInfo() {
	}

	/**
	 * 获取auc证书
	 * @return
	 */
	public InputStream getAucCertificate() {
	}

	/**
	 * 获取im证书
	 * @return
	 */
	public InputStream getImCertificate() {
	}

	/**
	 * 获取auc地址
	 * @return
	 */
	public URL getAucUrl() {
	}

	/**
	 * 获取回调函数
	 * @return
	 */
	public ConnectionCallback getCallback() {
	}

	/**
	 * 查看AUC是否使用默认的证书
	 * @return
	 */
	public boolean isAucDefaultCertificate() {
	}
	
	/**
	 * 查看IM是否使用默认的证书
	 * @return
	 */
	public boolean isIMDefaultCertificate() {
	}
}
```

参数ServerInfo详细说明：

```
public class ServerInfo {
	private URL aucAddress; // 服务器地址，为空则默认使用大数点提供的公有云服务
	private String appId; // 联系大数点客服获取
	private String appSec; // 联系大数点客服获取
	private String userId; // 用户唯一id
	private String clientId; // 设备唯一id
	......
}
````

参数userInfo详细说明：

```
public class UserInfo {
	public final static String MALE = "0";
	public final static String FEMALE = "1";

	private String userid; // 用户唯一id
	private String nickname; // 用户昵称
	private String avatarurl; // 用户头像url
	private String location; // 用户所在地区
	private String sex; // 用户性别,0:男,1:女
	private String signature; // 用户签名
	......
}
```

callback定义说明：

```
public abstract class ConnectionCallback {
	
	/**
	 * 连接服务器成功
	 */
	public void onConnect(){}
	
	/**
	 * 连接服务器失败
	 * @param reason 失败原因
	 */
 	public void onConnectError(String reason){}
	
	/**
	 * 连接中途断开
	 */
	public void onConnectionLost(){}
}
```


## 聊天室

### 加入聊天室

```
/**
 * 加入聊天室，如果这个聊天室不存在，系统会根据roomId和name自动创建该聊天室
 * @param roomId	聊天室id，需要保证唯一
 * @param name		聊天室的别名
 * @param callback	聊天室回调函数
 * @return ChatRoom	加入聊天室成功后返回ChatRoom对象
 * @throws ServiceException 加入聊天室失败或则sdk异常时抛出异常
 */
ChatRoom enterChatRoom(String roomId, String name, ChatRoomCallback callback) throws ServiceException 
```

参数callback详细说明：

```
public interface ChatRoomCallback {
	/**
	 * 加入聊天室服务器返回的结果
	 * @param result 服务器返回的结果
	 */
	void onEnterResult(int result);

	/**
	 * 退出聊天室服务器返回的结果
	 * @param result 退出时，服务器返回的结果
	 */
	void onExitResult(int result);

	/**
	 * 收到聊天室消息时的回调函数
	 * @param message
	 */
	void onChatRoomMessage(Message message);

	/**
	 * 发送消息时，服务器返回的结果回调
	 * @param id	消息唯一id
	 * @param result 发送的结果
	 */
	void onChatRoomMessageDelivery(String id, int result);

	/**
	 * 聊天室系统通知消息
	 * @param message
	 */
	void onSystemNotification(Message message);
}
```

### 发送消息

在加入聊天室成功后会返回一个与该聊天室对应的ChatRoom对象，通过这个ChatRoom对象的方法可以发送聊天室消息。
下面是ChatRoom类的方法说明。

```
/**
 * 发送文本消息
 * @param content 要发送的内容
 * @return Message 发送成功返回Message对象
 * @throws ServiceException  发送失败时抛出异常
 */
Message sendTextMessage(String content) throws ServiceException
```

返回值Message说明：

```
public class Message {
	......

	// 这条消息是谁发送的
	private String senderId;

	// 如果是聊天室消息，消息里面会有组的id
	private String groupId;

	// 如果是聊天室消息，消息里面会有组的名字
	private String groupName;

	// 消息的内容
	private JsonNode content;

	// 消息发送的时间
	private String time;

	// 消息id，每一条消息都有唯一的id。可以通过该id判断某天消息是否发送成功
	private int id;

	......
}
```

### 退出聊天室

```
/**
 * 退出聊天室
 * @throws ServiceException 退出失败时抛出异常
 */
exit() throws ServiceException 
```

## 获取聊天室列表

```
/**
 * 获取所有的聊天室
 * @param callback 	结果回调函数
 * @throws ServiceException	失败时抛出异常
 */
public void getGroupList(GetGroupCallback callback) throws ServiceException
```

参数callback说明：

```
public interface GetGroupCallback {
	void onResult(String result);
	// result 是下面的json格式
	{
	    "d" :"36" //dsdmethod
	    "s" :0000 //status 0000：成功, 1XXX: 失败
	    "gl":[]//group list
	    "i”:　"xx " //msgid
	｝
}
```

## 退出登录

```
logout();
```

