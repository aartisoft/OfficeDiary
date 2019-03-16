package com.OfficeDiaryport.admin.teaapps;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DelVenderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ListView listView;
    ImageView imageView;

    private AdView mAdView;

    ArrayList<VendorModel> vendorModels;
    private static CustomAdapter adapter;
    Context context;
    ProgressDialog dialog;
    ArrayList<HashMap> mylist;

    String url="http://reichprinz.com/teaAndroid/fetchvendoradmin.php";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DelVenderFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DelVenderFragment newInstance(String param1, String param2) {
        DelVenderFragment fragment = new DelVenderFragment();
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
        /*vendorModels= new ArrayList<>();
        for (int i = 0 ; i<DataClass.datavendor.size();i++){
            vendorModels.add(DataClass.datavendor.get(i)) ;
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_del_vender, container, false);

        context=getActivity();
        listView=(ListView)view.findViewById(R.id.listview);
        imageView=(ImageView)view.findViewById(R.id.imageView2);
        imageView.setVisibility(View.GONE);

        mAdView = (AdView) view.findViewById(R.id.adView);

        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading....");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                dialog.dismiss();
                //Toast.makeText(context, "data "+string, Toast.LENGTH_SHORT).show();
                vendorModels= new ArrayList<>();
                mylist=new ArrayList<HashMap>();
                try {
                    JSONArray fruitsArray =  new  JSONArray(string);
                    for(int i = 0; i < fruitsArray.length(); ++i) {
                        HashMap<String,String> hashMap=new HashMap<String, String>();
                        JSONObject jsonObject=fruitsArray.getJSONObject(i);

                        String name=jsonObject.getString("name");
                        String id=jsonObject.optString("vendor_id");
                        String mob=jsonObject.optString("mobile");
                        hashMap.put("vendor_id",id);
                        hashMap.put("name",name);
                        hashMap.put("mobile",mob);
                        mylist.add(hashMap);
                        VendorModel vendorModel=new VendorModel(name,mob,id);
                        vendorModels.add(vendorModel);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (vendorModels.size()>0) {
                    adapter = new CustomAdapter(vendorModels, context, getActivity());
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
                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();
                params.put("adminid", DataClass.user_id);

                return params;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);

        showadds();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.fragment_delven);
    }

    public void showadds(){

        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                //.addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB")
                //.addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB")
                .build();
        //MediationTestSuite.setAdRequest(adRequest.build());
        Log.d("onCreate: ", AdRequest.DEVICE_ID_EMULATOR);
        //Toast.makeText(this, AdRequest.DEVICE_ID_EMULATOR, Toast.LENGTH_SHORT).show();
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdClosed() {
                //Toast.makeText(context, "Ad is closed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                //Toast.makeText(context, "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                //Toast.makeText(context, "Ad left application!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });

        mAdView.loadAd(adRequest);
    }
}
