package dasudian.com.im.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dasudian.im.sdk.GetGroupCallback;
import com.dasudian.im.sdk.ServiceException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dasudian.com.im.demo.chat.adapter.GroupAdapter;
import dasudian.com.im.demo.dao.ClientInfo;
import dasudian.com.im.demo.util.Util;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";

	private List<Group> groupList = new ArrayList<Group>();
	private static GroupAdapter adapter;
	private ListView lv_list;

	private static Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				adapter.notifyDataSetChanged();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		adapter = new GroupAdapter(this, R.layout.group_item, groupList);
		lv_list = (ListView) findViewById(R.id.lv_list);
		lv_list.setAdapter(adapter);
		lv_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				final Group group = groupList.get(position);
				Intent intent = new Intent(MainActivity.this,
						GroupChatActivity.class);
				intent.putExtra("groupId", group.getGroupId());
				startActivity(intent);
			}
		});
//		initGroup();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		MyApplication.logout();
	}

	private void initGroup() {
		for (int i = 0; i < 100; i++) {
			String groupId = i + "";
			groupList.add(new Group(groupId, groupId));
		}
		adapter.notifyDataSetChanged();
	}

	// 获取组列表按钮被点击
	public void getGroupList(View view) {
		if (MyApplication.client == null) {
			Log.e(TAG, "client is null");
			return;
		}
		Util.showProgressDialog(this);
		try {
			MyApplication.client.getGroupList(new GetGroupCallback() {
				@Override
				public void onResult(String result) {
					Util.disMissProgressDialog();
					try {
						JSONObject jsonObject = new JSONObject(result);
						JSONArray jsonArray = jsonObject.getJSONArray("gl");
						groupList.clear();
						for (int i = 0; i < jsonArray.length(); i++) {
							String groupId = jsonArray.get(i).toString();
							groupList.add(new Group(groupId, groupId));
						}
						Message msg = handler.obtainMessage();
						msg.what = 0;
						handler.sendMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (ServiceException e) {
			e.printStackTrace();
			Util.disMissProgressDialog();
		}
	}
}
