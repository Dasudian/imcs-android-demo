package dasudian.com.im.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.dasudian.im.sdk.ChatRoom;
import com.dasudian.im.sdk.ChatRoomCallback;
import com.dasudian.im.sdk.Message;
import com.dasudian.im.sdk.MessageType;
import com.dasudian.im.sdk.ServiceException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import dasudian.com.im.demo.chat.adapter.GroupChatAdapter;
import dasudian.com.im.demo.util.Util;

public class GroupChatActivity extends Activity {
	private static final String TAG = "GroupChatActivity";
	private String user = "我";
	private static String groupId;
	private static ListView lv_chat;
	private EditText et_msg;

	private GroupChatAdapter adapter;
	private ChatRoom chatRoom;
	private List<Message> messageList = new LinkedList<Message>();
	private ChatRoomCallback callback = new MyChatRoomCallback();

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				try {
					Message message = (Message) msg.obj;
					JSONObject jsonObject = new JSONObject(message.getContent().toString());
					String content = jsonObject.getString("b");
					DsdMessage dm = new DsdMessage(MessageType.TEXT, content, message.getSenderId(), groupId);
					adapter.msgList.add(dm);
				} catch (JSONException e) {
					e.printStackTrace();
					return;
				}
				adapter.notifyDataSetChanged();
				break;
			case 1:
                Message message = (Message) msg.obj;
                adapter.msgList.add(new DsdMessage(MessageType.TEXT, message.getContent().toString(),
                        message.getSenderId(), message.getGroupId()));
				adapter.notifyDataSetChanged();
				lv_chat.setSelection(adapter.msgList.size());
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groupchat);

		Intent intent = getIntent();
		groupId = intent.getStringExtra("groupId");

		adapter = new GroupChatAdapter(this, R.layout.group_chat_text_item);
		initMessage();
		lv_chat = (ListView) findViewById(R.id.lv_chat);
		lv_chat.setAdapter(adapter);
		registerForContextMenu(lv_chat);
		et_msg = (EditText) findViewById(R.id.et_msg);

		// 加入聊天室
		try {
			chatRoom = MyApplication.client.enterChatRoom(groupId, groupId, callback);
		} catch (ServiceException e) {
			e.printStackTrace();
			Util.showToast(this, "加入聊天室失败，请检查网络");
		}
	}

	// 聊天室回调函数
	class MyChatRoomCallback implements ChatRoomCallback {

		@Override
		public void onChatRoomMessage(final Message message) {
			Log.i(TAG, "onChatRoomMessage:" + message.getContent());
			android.os.Message handerMessage = handler.obtainMessage();
			handerMessage.what = 0;
			handerMessage.obj = message;
			handler.sendMessage(handerMessage);
		}

		@Override
		public void onChatRoomMessageDelivery(String messageId, int result) {
			Log.i(TAG, "onChatRoomMessageDelivery:" + messageId + "," + result);
		}

		@Override
		public void onEnterResult(ChatRoom chatRoom, final int i) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Util.showToast(GroupChatActivity.this, "加入聊天室:" + i);
				}
			});
			Log.i(TAG, "onEnterResult: " + i);
		}

		@Override
		public void onExitResult(int result) {
			Log.i(TAG, "onExitResult:" + result);
		}

		@Override
		synchronized public void onSystemNotification(Message message) {
			Log.i(TAG, "onSystemNotification:" + message.getContent());
			android.os.Message msg = handler.obtainMessage();
			msg.what = 1;
			msg.obj = message;
			handler.sendMessage(msg);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 退出聊天室
		if (chatRoom != null) {
			try {
				chatRoom.exit();
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
	}

	// 发送消息
	public void sendMsg(View view) {
		final String msg = et_msg.getText().toString();
		if (chatRoom == null) {
			try {
				chatRoom = MyApplication.client.enterChatRoom(groupId, user, callback);
			} catch (ServiceException e) {
				e.printStackTrace();
				Util.showToast(this, "enterChatRoom failed，请检查网络");
				return;
			}
		}

		if (!TextUtils.isEmpty(msg) && chatRoom != null) {
			try {
				Message message = chatRoom.sendTextMessage(msg);
				// 添加到已发送链表
				messageList.add(message);

				adapter.msgList.add(new DsdMessage(MessageType.TEXT, msg, user, null));
				adapter.notifyDataSetChanged();
				lv_chat.setSelection(adapter.msgList.size() - 1);
				et_msg.setText("");
			} catch (ServiceException e) {
				e.printStackTrace();
				// 注意：断线重连后，需要重新加入聊天室，才能再次发送，否则会发送失败
				chatRoom = null;
				Util.showToast(this, "发送消息失败，请检查网络");
			}
		}
	}

	/**
	 * 显示当前的组id
	 */
	private void initMessage() {
		DsdMessage message = new DsdMessage(MessageType.TEXT, groupId, user, groupId);
		adapter.msgList.add(message);
	}
}
