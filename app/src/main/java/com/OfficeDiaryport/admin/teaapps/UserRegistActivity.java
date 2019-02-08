package com.OfficeDiaryport.admin.teaapps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
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

public class UserRegistActivity extends AppCompatActivity {

    EditText names,contact,editpass,editrepass;
    RadioGroup gender;
    Button submit;
    ProgressDialog dialog;
    Context context;
    Spinner officespin;
    List<String> spinlist;

    String HttpUrl="http://reichprinz.com/teaAndroid/useregister.php";

    String url="http://reichprinz.com/teaAndroid/fetchadmin.php";

    ArrayList<HashMap> mylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_regist);


        context=this;
        names=(EditText)findViewById(R.id.editnames);
        contact=(EditText)findViewById(R.id.editcont);
        editpass=(EditText)findViewById(R.id.paswordtext);
        editrepass=(EditText)findViewById(R.id.passwordretyp);
        contact.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

        submit=(Button)findViewById(R.id.button);
        gender=(RadioGroup)findViewById(R.id.radiogroup);
        officespin=(Spinner)findViewById(R.id.spinner3);

        datafromServer();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (names.getText().toString().trim().length()<=0 ){
                    //Toast.makeText(context, "Please Enter Name !", Toast.LENGTH_SHORT).show();
                    names.setError("Please Enter name");
                    names.requestFocus();
                }else if (contact.getText().toString().trim().length()<=0){
                    //Toast.makeText(context, "Please Enter Mobile Number !", Toast.LENGTH_SHORT).show();
                    contact.setError("Please Enter Mobile Number !");
                    contact.requestFocus();
                }else if (contact.getText().toString().trim().length()!=10){
                    //Toast.makeText(context, "Please Enter Correct Number !", Toast.LENGTH_SHORT).show();
                    contact.setError("Please Enter Correct 10 digit Number !");
                    contact.requestFocus();
                }else if (officespin.getSelectedItem().equals("No items")){
                    Toast.makeText(context, "No office/admin avail", Toast.LENGTH_SHORT).show();
                }
                else if (editpass.getText().toString().trim().length()<=0){
                    //Toast.makeText(context, "Please Enter password", Toast.LENGTH_SHORT).show();
                    editpass.setError("Please Enter password");
                    editpass.requestFocus();
                }else if (editrepass.getText().toString().trim().length()<=0){
                    //Toast.makeText(context, "Please Enter password Again", Toast.LENGTH_SHORT).show();
                    editrepass.setError("Please Enter password Again");
                    editrepass.requestFocus();
                }else if (!editpass.getText().toString().equals(editrepass.getText().toString())){
                    //Toast.makeText(context, "Password not match", Toast.LENGTH_SHORT).show();
                    editrepass.setError("Password not match");
                    editrepass.requestFocus();
                }
                else {
                    try  {
                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {

                    }

                    dialog = new ProgressDialog(context);
                    dialog.setMessage("Loading....");
                    dialog.show();
                    final String name = names.getText().toString();
                    final String mobi = contact.getText().toString();
                    final String passwd = editrepass.getText().toString();

                    int pos1 = officespin.getSelectedItemPosition();
                    HashMap<String, String> hashmap = mylist.get(pos1);
                    final String admn_id = hashmap.get("iduser");

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String ServerResponse) {

                                    // Hiding the progress dialog after all task complete.
                                    dialog.dismiss();
                                    if (ServerResponse.equals("Registered Successfully")) {
                                        // Showing response message coming from server.
                                        Toast.makeText(context, "User registered succesfully", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(context,MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else if (ServerResponse.equals("user exist")){
                                        Toast.makeText(context, "This mobile number is already Registered!", Toast.LENGTH_SHORT).show();
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
//                                    Log.d("Error", volleyError.getMessage());
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            // Creating Map String Params.
                            Map<String, String> params = new HashMap<String, String>();
                            // Adding All values to Params.
                            params.put("name", name);
                            params.put("mobile", mobi);
                            params.put("roles", "user");
                            params.put("password", passwd);
                            params.put("adminid", admn_id);
                            return params;
                        }
                    };
                    // Creating RequestQueue.
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    // Adding the StringRequest object into requestQueue.
                    requestQueue.add(stringRequest);

                }
            }
        });
    }

    public void datafromServer(){
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading....");
        dialog.show();
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                dialog.dismiss();
                //Toast.makeText(context, "data "+string, Toast.LENGTH_SHORT).show();
                mylist=new ArrayList<HashMap>();
                spinlist=new ArrayList<String>();
                if (string.equals("No Results")){
                    spinlist.add("No items");
                    ArrayAdapter adapter = new ArrayAdapter(context, R.layout.spinner_item, spinlist);
                    officespin.setAdapter(adapter);
                }else {
                    try {
                        JSONArray fruitsArray = new JSONArray(string);
                        for (int i = 0; i < fruitsArray.length(); ++i) {
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            JSONObject jsonObject = fruitsArray.getJSONObject(i);

                            spinlist.add(jsonObject.getString("name"));
                            String name = jsonObject.getString("name");
                            String id = jsonObject.optString("iduser");
                            hashMap.put("name", name);
                            hashMap.put("iduser", id);
                            mylist.add(hashMap);
                        }
                        if (spinlist.size()>0) {
                            ArrayAdapter adapter1 = new ArrayAdapter(context, R.layout.spinner_item, spinlist);
                            officespin.setAdapter(adapter1);
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
        });

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
    }

}
