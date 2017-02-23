package dasudian.com.im.demo.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import dasudian.com.im.demo.DsdMessage;
import dasudian.com.im.demo.R;


public class GroupChatAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	public List<DsdMessage> msgList;
	
	public GroupChatAdapter(Context context, int resource) {
		this.inflater = LayoutInflater.from(context);
		this.msgList = new LinkedList<DsdMessage>();
	}

	@Override
	public int getCount() {
		return msgList.size();
	}

	@Override
	public DsdMessage getItem(int position) {
		return msgList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		return getItem(position).getType().getMethod();
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DsdMessage message = getItem(position);
		ViewHolder holder;
		if (convertView == null) {
			convertView = createViewByMessage(message, position);
			holder = getHolder(message, convertView);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		setHolder(message, holder);

		return convertView;
	}

	private View createViewByMessage(DsdMessage message, int position) {
		switch (message.getType()) {
		case TEXT:
			return inflater.inflate(R.layout.group_chat_text_item, null);
		case IMAGE:
			return inflater.inflate(R.layout.group_chat_image_item, null);
		default:
			return null;
		}
	}

	/**
	 * 根据消息类型返回对应的holder
	 * 
	 * @param message
	 * @param convertView
	 * @return
	 */
	private ViewHolder getHolder(DsdMessage message, View convertView) {
		ViewHolder holder = new ViewHolder();

		switch (message.getType()) {
		case TEXT:
			holder.from = (TextView) convertView.findViewById(R.id.tv_from);
			holder.tv_content = (TextView) convertView
					.findViewById(R.id.tv_content);
			break;
		case IMAGE:
			holder.from = (TextView) convertView.findViewById(R.id.tv_from);
			holder.iv_image = (ImageView) convertView
					.findViewById(R.id.iv_image);
			break;
		default:
			break;
		}

		return holder;
	}

	/**
	 * 根据消息类型设置holder
	 * 
	 * @param holder
	 */
	private void setHolder(final DsdMessage message, final ViewHolder holder) {
		holder.from.setText(message.getFrom()+":");
		switch (message.getType()) {
		case TEXT:
			holder.tv_content.setText(message.getContent());
			break;
		case IMAGE:
			break;
		default:
			break;
		}
	}

	class ViewHolder {
		TextView from;
		TextView tv_content;
		ImageView iv_image;
	}
}
