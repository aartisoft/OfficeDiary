package com.OfficeDiaryport.admin.teaapps;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class EntriesUserAdapter extends ArrayAdapter<EntriesModel>{

    private ArrayList<EntriesModel> dataSet;
    Context mContext;

    public EntriesUserAdapter(ArrayList<EntriesModel> dataSet, Context mContext) {
        super(mContext, R.layout.entries_list,dataSet);
        this.dataSet = dataSet;
        this.mContext = mContext;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView vName;
        TextView utname;
        TextView quantity;
        TextView times;
        TextView txtdate;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        EntriesModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        EntriesUserAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;
        if (convertView == null) {

            viewHolder = new EntriesUserAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.entries_list, parent, false);
            viewHolder.vName = (TextView) convertView.findViewById(R.id.textvname);
            viewHolder.utname = (TextView) convertView.findViewById(R.id.textutname);
            viewHolder.quantity = (TextView) convertView.findViewById(R.id.textquantity);
            viewHolder.times = (TextView) convertView.findViewById(R.id.texttime);
            viewHolder.txtdate = (TextView) convertView.findViewById(R.id.textDate);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (EntriesUserAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.vName.setText(dataModel.getVname());
        viewHolder.utname.setText(dataModel.getUtname());
        viewHolder.quantity.setText(dataModel.getQuantity());
        viewHolder.times.setText(dataModel.getTime());
        viewHolder.txtdate.setText(dataModel.getDate());

        // Return the completed view to render on screen
        return convertView;
        //return super.getView(position, convertView, parent);
    }
}
