package com.OfficeDiaryport.admin.teaapps;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class CustomAdapter extends ArrayAdapter<VendorModel> implements View.OnClickListener{

    private ArrayList<VendorModel> dataSet;
    Context mContext;
    ProgressDialog dialog;
    Activity activity;

    public CustomAdapter(ArrayList<VendorModel> data, Context context,Activity activity) {
        super(context, R.layout.vendor_list, data);
        this.dataSet = data;
        this.mContext=context;
        this.activity=activity;
    }

    @Override
    public void onClick(View v) {
        final int position=(Integer) v.getTag();
        Object object= getItem(position);
        final VendorModel dataModel=(VendorModel)object;

        switch (v.getId())
        {
            case R.id.item_info:
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    alertDialogBuilder.setTitle("DELETE");
                    alertDialogBuilder.setMessage("Are you sure you want to delete !");
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setIcon(R.drawable.ic_action_delete);
                    alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            serverData(dataModel.getVendor_id(),position);
                        }
                    });
                    alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(mContext, "Discarded", Toast.LENGTH_SHORT).show();
                        }
                    });

                    alertDialogBuilder.show();
                break;
            case R.id.item_edit:
                LayoutInflater li = LayoutInflater.from(mContext);
                View promptsView = li.inflate(R.layout.promptsvender, null);

                final EditText userInput = (EditText) promptsView.findViewById(R.id.textname);
                final EditText usernumber = (EditText) promptsView.findViewById(R.id.textnumber);
                userInput.setText(dataModel.getName());
                usernumber.setText(dataModel.getPhone());
                userInput.setSelection(userInput.getText().length());

                final AlertDialog.Builder alertDialogBuilder3 = new AlertDialog.Builder(mContext);
                alertDialogBuilder3.setView(promptsView);
                alertDialogBuilder3.setMessage("Edit Vendor?");
                alertDialogBuilder3.setCancelable(false);
                alertDialogBuilder3.setPositiveButton("SAVE",null);
                alertDialogBuilder3.setNegativeButton("CANCEL",null);
                final AlertDialog dialogs = alertDialogBuilder3.create();
                dialogs.show();

                dialogs.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (userInput.getText().toString().trim().length()<=0 ){
                            Toast.makeText(mContext, "User name not be blank", Toast.LENGTH_SHORT).show();
                            userInput.requestFocus();
                        }else if (usernumber.getText().toString().trim().length()<=0 ){
                            Toast.makeText(mContext, "Enter proper Number", Toast.LENGTH_SHORT).show();
                            usernumber.requestFocus();
                        }else if (usernumber.getText().toString().trim().length()>=11){
                            Toast.makeText(mContext, "Enter proper Number", Toast.LENGTH_SHORT).show();
                            usernumber.requestFocus();
                        }
                        else {
                            try  {
                                InputMethodManager imm = (InputMethodManager)activity.getSystemService(INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                            } catch (Exception e) {

                            }
                            updateserverdata(dataModel.getVendor_id(),position,userInput.getText().toString(),usernumber.getText().toString());
                            dialogs.dismiss();
                        }
                    }
                });
                break;
        }
    }

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtType;
        TextView txtVersion;
        ImageView info,edit;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Get the data item for this position
        VendorModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;
        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.vendor_list, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.txtType = (TextView) convertView.findViewById(R.id.type);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);
            viewHolder.edit = (ImageView) convertView.findViewById(R.id.item_edit);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.txtName.setText(dataModel.getName());
        viewHolder.txtType.setText(dataModel.getPhone());
        //viewHolder.info.setImageResource(dataModel.getImgpath());
        viewHolder.info.setOnClickListener(this);
        viewHolder.edit.setOnClickListener(this);

        viewHolder.info.setTag(position);
        viewHolder.edit.setTag(position);
        // Return the completed view to render on screen
        return convertView;
        //return super.getView(position, convertView, parent);
    }


    public void serverData(final String vid ,final int position){
        String HttpUrl="http://reichprinz.com/teaAndroid/delvendor.php";
        dialog = new ProgressDialog(mContext);
        dialog.setMessage("Loading....");
        dialog.show();
        StringRequest request2 = new StringRequest(Request.Method.POST ,HttpUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                dialog.dismiss();
                //dataModels= new ArrayList<>();


                if (string.equals("updated Successfully")) {
                    Toast.makeText(mContext, "Vendor Deleted Successfully", Toast.LENGTH_SHORT).show();
                    dataSet.remove(position);
                    notifyDataSetChanged();
                }else {
                    Toast.makeText(mContext, "Some error occured Try again", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(mContext, "Some error occurred!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                alertDialogBuilder.setMessage("Something went wrong Check your Connection !");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setNeutralButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        serverData(vid,position);
                    }
                });
                alertDialogBuilder.show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("vendor_id", vid);
                return params;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(mContext);
        rQueue.add(request2);
    }

    public void updateserverdata(final String vid ,final int position,final String vname,final String vmobile){
        dialog = new ProgressDialog(mContext);
        dialog.setMessage("Loading....");
        dialog.show();
        String HttpUrl="http://reichprinz.com/teaAndroid/updatevendor.php";
        StringRequest request2 = new StringRequest(Request.Method.POST ,HttpUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                dialog.dismiss();

                if (string.equals("updated Successfully")) {
                    Toast.makeText(mContext, "Data Changed Successfully", Toast.LENGTH_SHORT).show();
                    VendorModel vendorModel=getItem(position);
                    vendorModel.setName(vname);
                    vendorModel.setPhone(vmobile);
                    dataSet.set(position,vendorModel);
                    notifyDataSetChanged();
                }else {
                    Toast.makeText(mContext, "Some error occured Try again", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(mContext, "Some error occurred!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                alertDialogBuilder.setMessage("Something went wrong Check your Connection !");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setNeutralButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateserverdata(vid,position,vname,vmobile);
                    }
                });
                alertDialogBuilder.show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("vendor_id", vid);
                params.put("vname", vname);
                params.put("vmobile", vmobile);
                return params;
            }
        };
        RequestQueue rQueue = Volley.newRequestQueue(mContext);
        rQueue.add(request2);
    }

}
