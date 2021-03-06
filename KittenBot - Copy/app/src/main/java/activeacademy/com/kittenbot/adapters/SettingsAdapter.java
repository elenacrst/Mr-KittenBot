package activeacademy.com.kittenbot.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import activeacademy.com.kittenbot.R;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingHolder>

{
    private String[] options;
    private final SettingsAdapterOnClickHandler mClickHandler;

    public interface SettingsAdapterOnClickHandler {
        void onClick(String option);
    }

    public SettingsAdapter(SettingsAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public void setData(String[] data) {
        if(data!=null)
            options = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(options==null)
            return 0;
        return options.length;
    }

    @Override
    public SettingHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.setting_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new SettingHolder(view);
    }
    @Override
    public void onBindViewHolder(SettingHolder holder, int position) {

        holder.settingTV.setText(options[position]);

    }

    class SettingHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        TextView settingTV;

        SettingHolder(View itemView) {
            super(itemView);
            settingTV = (TextView)itemView.findViewById(R.id.settingTV);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            String option = options[position];
            mClickHandler.onClick(option);
        }

    }
}
