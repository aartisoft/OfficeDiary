package com.OfficeDiaryport.admin.teaapps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EntriesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ListView listView;
    ImageView imageView;
    private AdView mAdView;

    ArrayList<EntriesModel> dataModels;
    private static EntriesAdapter adapter;
    Context context;

    String HttpUrl="http://reichprinz.com/teaAndroid/fetchentries.php";
    ProgressDialog dialog;

    TextView textqtot,textamt;
    private Calendar calendar;
    Spinner month_spinner;
    int checks = 0;

    String spinitems[]={"January","February","March","April","May","June","July","August","September","October","November","December"};



    private String mParam1;
    private String mParam2;

    public EntriesFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static EntriesFragment newInstance(String param1, String param2) {
        EntriesFragment fragment = new EntriesFragment();
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
        View view=inflater.inflate(R.layout.fragment_entries, container, false);
        context=getActivity();
        calendar = Calendar.getInstance();

        listView=(ListView)view.findViewById(R.id.listviews);
        imageView=(ImageView)view.findViewById(R.id.imageView2);
        imageView.setVisibility(View.GONE);

        mAdView = (AdView) view.findViewById(R.id.adView);

        textqtot=(TextView)view.findViewById(R.id.texttotal);
        textamt=(TextView)view.findViewById(R.id.textView8);
        month_spinner=(Spinner)view.findViewById(R.id.spinner4);


        ArrayAdapter<String> adapter=new ArrayAdapter<String>(context,R.layout.spinner_item2, spinitems);
        month_spinner.setAdapter(adapter);

        serverData();
        month_spinner.setSelection(calendar.get(Calendar.MONTH));

        month_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(checks >= 1) {
                    sortSpin(String.valueOf(position + 1));
                }
                checks++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        showadds();

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
                DataClass.datafio.clear();
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
                        String username=jsonObject.optString("uname");
                        String rate=jsonObject.optString("rate");

                        EntriesModel entriesModel=new EntriesModel(vname,utname,quantity,time,date,username,rate);
                        dataModels.add(entriesModel);
                        DataClass.datafio.add(entriesModel);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (dataModels.size()>0) {
                    adapter = new EntriesAdapter(dataModels, context);
                    listView.setAdapter(adapter);
                    imageView.setVisibility(View.GONE);
                    sortSpin(twodigitNumber(calendar.get(Calendar.MONTH) + 1));
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

    public void sortSpin(String str){
        int countqnt=0,countamt=0;
        int a=DataClass.datafio.size();
        ArrayList<EntriesModel> temp=new ArrayList<EntriesModel>();
        for (int i = 0 ; i<a;i++){
            temp.add(DataClass.datafio.get(i)) ;
        }
        dataModels.clear();
        for (int i = 0; i < a; i++) {
            String array3[]= temp.get(i).getDate().split(":", 0);
            if (array3[1].equals(str)) {
                dataModels.add(temp.get(i));
                int tempqt=Integer.parseInt(temp.get(i).getQuantity());
                int temamt=Integer.parseInt(temp.get(i).getPrize());
                countqnt=countqnt+tempqt;
                countamt=countamt+(temamt*tempqt);
            }
        }
        if (dataModels.size()>0){
            adapter.notifyDataSetChanged();
            listView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
        }else {
            listView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
        }
        textqtot.setText(countqnt+"");
        textamt.setText(countamt+" Rs.");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.fragment_entries);
    }

    public String twodigitNumber(int minute){
        String strminute="";
        if (minute==0)
        {
            strminute="00";
        }else if (minute<10){
            strminute="0"+minute;
        }else {
            strminute=""+minute;
        }
        return strminute;
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
