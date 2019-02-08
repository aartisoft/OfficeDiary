package com.OfficeDiaryport.admin.teaapps;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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

public class DelUtilityFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ListView listView;
    ImageView imageView;

    ArrayList<UtilityModel> dataModels;
    private static UtilityAdapter adapter;
    Context context;
    ProgressDialog dialog;

    String url="http://reichprinz.com/teaAndroid/fetchvenduti.php";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DelUtilityFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DelUtilityFragment newInstance(String param1, String param2) {
        DelUtilityFragment fragment = new DelUtilityFragment();
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
        /*dataModels= new ArrayList<>();
        dataModels.clear();
        for (int i = 0 ; i<DataClass.datautility.size();i++){
            dataModels.add(DataClass.datautility.get(i)) ;
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_del_utility, container, false);

        context=getActivity();
        listView=(ListView)view.findViewById(R.id.listview);
        imageView=(ImageView)view.findViewById(R.id.imageView2);
        imageView.setVisibility(View.GONE);

        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading....");
        dialog.show();

        StringRequest request2 = new StringRequest(Request.Method.POST ,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                dialog.dismiss();
                dataModels= new ArrayList<>();

                //Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
                try {
                    JSONArray fruitsArray =  new  JSONArray(string);
                    for(int i = 0; i < fruitsArray.length(); ++i) {
                        //vname,utname,quantity,time,date
                        JSONObject jsonObject=fruitsArray.getJSONObject(i);
                        String utility_id=jsonObject.getString("utility_id");
                        String name=jsonObject.optString("name");
                        String rate=jsonObject.getString("rate");
                        String vendor_id=jsonObject.getString("vendor_id");
                        String vname=jsonObject.optString("vname");
                        String mobile=jsonObject.optString("mobile");
                        UtilityModel utilityModel=new UtilityModel(vname,name,rate,utility_id);
                        dataModels.add(utilityModel);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (dataModels.size()>0) {
                    adapter = new UtilityAdapter(dataModels, context, getActivity());
                    listView.setAdapter(adapter);
                    imageView.setVisibility(View.GONE);
                }else {
                    imageView.setVisibility(View.VISIBLE);
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", DataClass.user_id);
                return params;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request2);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.fragment_deluti);
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
