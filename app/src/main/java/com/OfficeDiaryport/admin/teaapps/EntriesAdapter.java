package com.OfficeDiaryport.admin.teaapps;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class EntriesAdapter extends ArrayAdapter<EntriesModel> {

    private ArrayList<EntriesModel> dataSet;
    Context mContext;

    public EntriesAdapter(ArrayList<EntriesModel> dataSet, Context mContext) {
        super(mContext, R.layout.entries_lists,dataSet);
        this.dataSet = dataSet;
        this.mContext = mContext;
    }

    // View lookup cache
    private static class ViewHolder {
        //TextView vName;
        TextView utname;
        TextView quantity;
        TextView times;
        ImageView info;
        //TextView txtdate;
        //TextView textuser;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        final EntriesModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        EntriesAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;
        if (convertView == null) {

            viewHolder = new EntriesAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.entries_lists, parent, false);
            //viewHolder.vName = (TextView) convertView.findViewById(R.id.textvname);
            viewHolder.utname = (TextView) convertView.findViewById(R.id.textutname);
            viewHolder.quantity = (TextView) convertView.findViewById(R.id.textquantity);
            viewHolder.times = (TextView) convertView.findViewById(R.id.texttime);
            viewHolder.info=(ImageView)convertView.findViewById(R.id.imageVinfo);
            //viewHolder.txtdate = (TextView) convertView.findViewById(R.id.textDate);
            //viewHolder.textuser = (TextView) convertView.findViewById(R.id.textvusernm);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (EntriesAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }

        //viewHolder.vName.setText("Vendor : "+dataModel.getVname());
        final String username;
        if (dataModel.getUname().equals(DataClass.user_name)){
            username="You";
        }else {
            username=dataModel.getUname();
        }
        viewHolder.utname.setText(dataModel.getUtname());
        //viewHolder.textuser.setText("Added By :"+username);
        viewHolder.quantity.setText(dataModel.getQuantity());
        viewHolder.times.setText(dataModel.getTime());
        //viewHolder.txtdate.setText("Date :"+dataModel.getDate());

        viewHolder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v," Vendor : "+dataModel.getVname()
                        +"\t\t Date : "+dataModel.getDate()+
                        "\n Added By : "+username , Snackbar.LENGTH_LONG).show();
            }
        });

        // Return the completed view to render on screen
        return convertView;
        //return super.getView(position, convertView, parent);
    }
}
