package com.OfficeDiaryport.admin.teaapps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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


public class UserEntriesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    ListView listView;
    ImageView imageView;
    private AdView mAdView;

    ArrayList<EntriesModel> dataModels;
    private static EntriesUserAdapter adapter;
    Context context;

    String HttpUrl="http://reichprinz.com/teaAndroid/fetchentries.php";
    ProgressDialog dialog;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserEntriesFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static UserEntriesFragment newInstance(String param1, String param2) {
        UserEntriesFragment fragment = new UserEntriesFragment();
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
        View view= inflater.inflate(R.layout.fragment_user_entries, container, false);
        context=getActivity();

        listView=(ListView)view.findViewById(R.id.listviews);
        imageView=(ImageView)view.findViewById(R.id.imageView2);
        imageView.setVisibility(View.GONE);

        mAdView = (AdView) view.findViewById(R.id.adView);

        showadds();
        serverData();

        return view;
    }

    public void serverData(){

        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading....");
        dialog.show();
        StringRequest request2 = new StringRequest(Request.Method.POST ,HttpUrl, new Response.Listener<String>() {
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
                        String date=jsonObject.getString("fdate");
                        String time=jsonObject.optString("ftime");
                        String quantity=jsonObject.getString("quantity");
                        String utname=jsonObject.getString("utname");
                        String vname=jsonObject.optString("vname");
                        EntriesModel entriesModel=new EntriesModel(vname,utname,quantity,time,date);
                        dataModels.add(entriesModel);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (dataModels.size()>0) {
                    adapter= new EntriesUserAdapter(dataModels,context);
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

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Something went wrong Check your Connection !");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setNeutralButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        serverData();
                    }
                });
                alertDialogBuilder.show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", DataClass.user_id);
                return params;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request2);
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
