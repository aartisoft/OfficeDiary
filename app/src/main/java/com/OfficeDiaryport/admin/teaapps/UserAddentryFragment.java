package com.OfficeDiaryport.admin.teaapps;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserAddentryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    Spinner spinner,spinvender;
    EditText editTextquantity;
    TextView texttime;
    Button button,addbutton;
    List<String> spinlist,venderlist;
    private int  mHour, mMinute;
    String format,timestring;

    Context context;

    String url="http://reichprinz.com/teaAndroid/fetchvendoradmin.php";

    String HttpUrl="http://reichprinz.com/teaAndroid/entrydata.php";
    ProgressDialog dialog;

    ArrayList<HashMap> mylist,utilmaplist;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public UserAddentryFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static UserAddentryFragment newInstance(String param1, String param2) {
        UserAddentryFragment fragment = new UserAddentryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user_addentry, container, false);


        context=getActivity();

        spinner=(Spinner)view.findViewById(R.id.spinner);
        spinvender=(Spinner)view.findViewById(R.id.spinner2);
        button=(Button)view.findViewById(R.id.button);
        addbutton=(Button)view.findViewById(R.id.button2);
        editTextquantity=(EditText)view.findViewById(R.id.editText);

        datafromServer();

        texttime=(TextView)view.findViewById(R.id.textvtime);
        String now = new SimpleDateFormat("hh:mm aa").format(new java.util.Date().getTime());
        timestring=now;
        texttime.setText("Time : "+now);
        final String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        if (hourOfDay == 0) {
                            hourOfDay += 12;
                            format = "AM";
                        }
                        else if (hourOfDay == 12) {
                            format = "PM";
                        }
                        else if (hourOfDay > 12) {
                            hourOfDay -= 12;
                            format = "PM";
                        }
                        else {
                            format = "AM";
                        }
                        String strminute="";
                        if (minute==0)
                        {
                            strminute="00";
                        }else if (minute<10){
                            strminute="0"+minute;
                        }else {
                            strminute=""+minute;
                        }
                        texttime.setText("Time : "+hourOfDay + ":" + strminute+" "+format);
                        timestring=hourOfDay + ":" + minute+" "+format;
                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinvender.getSelectedItem().equals("No items")){
                    Toast.makeText(context, "No Vender Added yet", Toast.LENGTH_SHORT).show();
                }else if (spinner.getSelectedItem().equals("No items")){
                    Toast.makeText(context, "No Utility avail for this vendor", Toast.LENGTH_SHORT).show();
                }else if (editTextquantity.getText().toString().trim().length()<=0 ){
                    Toast.makeText(context, "Please Enter Quantity !", Toast.LENGTH_SHORT).show();
                    editTextquantity.requestFocus();
                }else {
                    dialog.show();
                    int pos1 = spinvender.getSelectedItemPosition();
                    int pos2 = spinner.getSelectedItemPosition();
                    HashMap<String, String> hashmap = mylist.get(pos1);
                    final String vnd_id = hashmap.get("vendor_id");
                    HashMap<String, String> hashmap2 = utilmaplist.get(pos2);
                    final String util_id = hashmap2.get("utility_id");
                    //Toast.makeText(context, vnd_id + " > " + util_id, Toast.LENGTH_SHORT).show();

                    StringRequest stringRequest3 = new StringRequest(Request.Method.POST, HttpUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String ServerResponse) {
                                    dialog.dismiss();
                                    // Showing response message coming from server.
                                    if (ServerResponse.equals("1")){
                                        Toast.makeText(context, "Data Addes Successfully", Toast.LENGTH_LONG).show();
                                        userActivity.setfragment();
                                    }else {
                                        Toast.makeText(context, "Try again some problem occured!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {

                                    // Hiding the progress dialog after all task complete.
                                    dialog.dismiss();
                                    // Showing error message if something goes wrong.
                                    Toast.makeText(context, volleyError.toString(), Toast.LENGTH_LONG).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            // Creating Map String Params.
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("date", date);
                            params.put("time", timestring);
                            params.put("utility_id", util_id);
                            params.put("quantity", editTextquantity.getText().toString());
                            params.put("vendor_id", vnd_id);
                            params.put("user_id", DataClass.user_id);
                            return params;
                        }
                    };
                    // Creating RequestQueue.
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    // Adding the StringRequest object into requestQueue.
                    requestQueue.add(stringRequest3);
                }
            }
        });


        spinvender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinvender.getSelectedItem().equals("No items")){
                }else {
                    HashMap<String, String> hashmap = mylist.get(position);
                    String vid = hashmap.get("vendor_id");
                    dialog.show();
                    fetchutil(vid);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return view;
    }

    public void datafromServer(){
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading....");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                dialog.dismiss();
                //Toast.makeText(context, "data "+string, Toast.LENGTH_SHORT).show();
                mylist=new ArrayList<HashMap>();
                venderlist=new ArrayList<String>();
                try {
                    JSONArray fruitsArray =  new  JSONArray(string);
                    for(int i = 0; i < fruitsArray.length(); ++i) {
                        HashMap<String,String> hashMap=new HashMap<String, String>();
                        JSONObject jsonObject=fruitsArray.getJSONObject(i);

                        venderlist.add(jsonObject.getString("name"));
                        String name=jsonObject.getString("name");
                        String id=jsonObject.optString("vendor_id");
                        hashMap.put("name",name);
                        hashMap.put("vendor_id",id);
                        mylist.add(hashMap);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                vendorspinner();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "Some error occurred!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Something went wrong Check your Connection !");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setNeutralButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        datafromServer();
                    }
                });
                alertDialogBuilder.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();
                params.put("adminid", DataClass.admin_id);

                return params;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
    }


    public void vendorspinner(){
        if (venderlist.size()>0) {
            ArrayAdapter adapter1 = new ArrayAdapter(context, R.layout.spinner_item, venderlist);
            spinvender.setAdapter(adapter1);
        }else {
            venderlist.add("No items");
            ArrayAdapter adapter1 = new ArrayAdapter(context, R.layout.spinner_item, venderlist);
            spinvender.setAdapter(adapter1);
        }
    }

    public void fetchutil(final String vndid){
        String url1="http://reichprinz.com/teaAndroid/fetchutility.php";
        StringRequest request2 = new StringRequest(Request.Method.POST ,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                dialog.dismiss();
                utilmaplist=new ArrayList<HashMap>();
                spinlist=new ArrayList<String>();
                try {
                    JSONArray fruitsArray =  new  JSONArray(string);
                    for(int i = 0; i < fruitsArray.length(); ++i) {
                        HashMap<String,String> hashMap=new HashMap<String, String>();
                        JSONObject jsonObject=fruitsArray.getJSONObject(i);

                        spinlist.add(jsonObject.getString("name"));
                        //Toast.makeText(context, ""+jsonObject.getString("rate"), Toast.LENGTH_SHORT).show();
                        String name=jsonObject.getString("name");
                        String id=jsonObject.optString("utility_id");
                        String rate=jsonObject.getString("rate");
                        hashMap.put("name",name);
                        hashMap.put("utility_id",id);
                        hashMap.put("rate",rate);
                        utilmaplist.add(hashMap);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                spinneritems();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "Some error occurred!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();
                // Adding All values to Params.
                params.put("vendorid", vndid);
                return params;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request2);
    }

    public void spinneritems(){
        if (spinlist.size()>0) {
            ArrayAdapter adapter = new ArrayAdapter(context, R.layout.spinner_item, spinlist);
            spinner.setAdapter(adapter);
        }else {
            spinlist.add("No items");
            ArrayAdapter adapter = new ArrayAdapter(context, R.layout.spinner_item, spinlist);
            spinner.setAdapter(adapter);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
