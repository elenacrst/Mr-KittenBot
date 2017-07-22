package activeacademy.com.kittenbot.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import activeacademy.com.kittenbot.R;
import activeacademy.com.kittenbot.SettingsActivity;
import activeacademy.com.kittenbot.entities.Message;

/**
 * Created by Elena on 7/2/2017.
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageHolder> {
    List<Message> messages;

    public MessagesAdapter() {
    }

    public void setData(List<Message> data) {
        if (data != null)
            messages = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (messages == null)
            return 0;
        return messages.size();
    }

    @Override
    public MessagesAdapter.MessageHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.chat_bubble;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MessagesAdapter.MessageHolder(view);
    }


    @Override
    public void onBindViewHolder(MessageHolder holder, int position) {

        if (messages.get(position).isMine()) {
            holder.leftRL.setVisibility(View.INVISIBLE);
            holder.rightTV.setText(messages.get(position).getBody());
        } else {

            holder.rightRL.setVisibility(View.INVISIBLE);
            holder.leftTV.setText(messages.get(position).getBody());
        }
    }

    public class MessageHolder extends RecyclerView.ViewHolder

    {
        public RelativeLayout leftRL, rightRL;
        public TextView leftTV, rightTV;

        public MessageHolder(View itemView) {
            super(itemView);
            leftRL = (RelativeLayout) itemView.findViewById(R.id.leftRL);
            leftTV = (TextView) itemView.findViewById(R.id.leftTV);
            rightRL = (RelativeLayout) itemView.findViewById(R.id.rightRL);
            rightTV = (TextView) itemView.findViewById(R.id.rightTV);

           switch (SettingsActivity.appColor) {
                case "chatBlue":
                    rightRL.setBackgroundResource(R.drawable.bubble_blue);
                    break;
                case "chatGreen":
                    rightRL.setBackgroundResource(R.drawable.bubble_green);
                    break;
                case "chatOrange":
                    rightRL.setBackgroundResource(R.drawable.bubble_orange);
                    break;
                case "chatPurple":
                    rightRL.setBackgroundResource(R.drawable.bubble_purple);
                    break;
                case "chatRed":
                    rightRL.setBackgroundResource(R.drawable.bubble_red);
                    break;

            }

        }
    }
}
