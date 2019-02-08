package com.OfficeDiaryport.admin.teaapps;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.List;
import java.util.Map;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class AddUtilityFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Spinner spinner;
    List<String> venderlist;

    EditText edname,edrate;
    Button submit;

    Context context;
    String url="http://reichprinz.com/teaAndroid/fetchvendoradmin.php";
    String url2="http://reichprinz.com/teaAndroid/addutilities.php";
    ProgressDialog dialog;
    ArrayList<HashMap> mylist;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AddUtilityFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AddUtilityFragment newInstance(String param1, String param2) {
        AddUtilityFragment fragment = new AddUtilityFragment();
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
        View view= inflater.inflate(R.layout.fragment_add_utility, container, false);
        context=getActivity();

        spinner=(Spinner)view.findViewById(R.id.spinner3);
        edname=(EditText)view.findViewById(R.id.textutility);
        edrate=(EditText)view.findViewById(R.id.textprice);
        edrate.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
        submit=(Button)view.findViewById(R.id.button2);

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
                if (string.equals("No Results")){
                    venderlist.add("No items");
                    ArrayAdapter adapter = new ArrayAdapter(context, R.layout.spinner_item, venderlist);
                    spinner.setAdapter(adapter);
                }else {
                    try {
                        JSONArray fruitsArray = new JSONArray(string);
                        for (int i = 0; i < fruitsArray.length(); ++i) {
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            JSONObject jsonObject = fruitsArray.getJSONObject(i);

                            venderlist.add(jsonObject.getString("name"));
                            String name = jsonObject.getString("name");
                            String id = jsonObject.optString("vendor_id");
                            hashMap.put("name", name);
                            hashMap.put("vendor_id", id);
                            mylist.add(hashMap);
                        }
                        if (venderlist.size() > 0) {
                            ArrayAdapter adapter = new ArrayAdapter(context, R.layout.spinner_item, venderlist);
                            spinner.setAdapter(adapter);
                        } else {
                            venderlist.add("No items");
                            ArrayAdapter adapter = new ArrayAdapter(context, R.layout.spinner_item, venderlist);
                            spinner.setAdapter(adapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
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
                params.put("adminid", DataClass.user_id);

                return params;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner.getSelectedItem().equals("No items")) {
                    Toast.makeText(context, "Please First Add vendors", Toast.LENGTH_SHORT).show();
                }else if (edname.getText().toString().trim().length()<=0 ){
                    Toast.makeText(context, "Please Enter Utility Name !", Toast.LENGTH_SHORT).show();
                    edname.requestFocus();
                }else if (edrate.getText().toString().trim().length()<=0){
                    Toast.makeText(context, "Please Enter Price !", Toast.LENGTH_SHORT).show();
                    edrate.requestFocus();
                }else {
                    try  {
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {

                    }
                    dialog.show();
                    final String stname = edname.getText().toString();
                    final String strate = edrate.getText().toString();

                    int pos2 = spinner.getSelectedItemPosition();
                    HashMap<String, String> hashmap = mylist.get(pos2);
                    final String vnd_id = hashmap.get("vendor_id");

                    StringRequest stringRequest3 = new StringRequest(Request.Method.POST, url2,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String ServerResponse) {
                                    dialog.dismiss();
                                    // Showing response message coming from server.
                                    if (ServerResponse.equals("Registered Successfully")) {
                                        edname.setText("");
                                        edrate.setText("");
                                        Toast.makeText(context, "Utility Added Successfully", Toast.LENGTH_LONG).show();

                                        //for jumping fragment
                                        Fragment fragment=new EntriesFragment();
                                        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                                        FragmentTransaction frgtras=fragmentManager.beginTransaction();
                                        frgtras.replace(R.id.flContent,fragment);
                                        frgtras.commit();
                                    }else {
                                        Toast.makeText(context, "Something went wrong ! Try again", Toast.LENGTH_LONG).show();
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
                            params.put("name", stname);
                            params.put("rate", strate);
                            params.put("vendor_id", vnd_id);
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

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.fragment_adduti);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            /*throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");*/
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
