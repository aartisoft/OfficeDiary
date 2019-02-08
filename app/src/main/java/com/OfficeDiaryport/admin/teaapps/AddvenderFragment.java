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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class AddvenderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    EditText name,number;
    Context context;
    Button submit;

    String url="http://reichprinz.com/teaAndroid/addvendor.php";
    ProgressDialog dialog;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AddvenderFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AddvenderFragment newInstance(String param1, String param2) {
        AddvenderFragment fragment = new AddvenderFragment();
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
        View view= inflater.inflate(R.layout.fragment_addvender, container, false);
        context=getActivity();

        name=(EditText)view.findViewById(R.id.textname);
        number=(EditText)view.findViewById(R.id.textnumber);
        number.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

        submit=(Button)view.findViewById(R.id.button2);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().trim().length()<=0 ){
                    //Toast.makeText(context, "Please Enter vendor name!", Toast.LENGTH_SHORT).show();
                    name.setError("Please Enter vendor name!");
                    name.requestFocus();
                }else if (number.getText().toString().trim().length()<=0){
                    //Toast.makeText(context, "Please Enter Mobile number", Toast.LENGTH_SHORT).show();
                    number.setError("Please Enter Mobile number");
                    number.requestFocus();
                }else if (number.getText().toString().trim().length()!=10){
                    //Toast.makeText(context, "Please Enter Correct Number !", Toast.LENGTH_SHORT).show();
                    number.setError("Please Enter Correct 10 digit Number !");
                    number.requestFocus();
                }else {
                    try  {
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {

                    }
                    dialog = new ProgressDialog(context);
                    dialog.setMessage("Loading....");
                    dialog.show();
                    final String stname = name.getText().toString();
                    final String stnumber = number.getText().toString();

                    StringRequest stringRequest3 = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String ServerResponse) {
                                    dialog.dismiss();
                                    // Showing response message coming from server.
                                    if (ServerResponse.equals("Registered Successfully")) {
                                        name.setText("");
                                        number.setText("");
                                        Toast.makeText(context, "Vendor Added sucessfully", Toast.LENGTH_LONG).show();

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
                            params.put("mobile", stnumber);
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


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.fragment_addven);
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
