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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
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

public class RegistrationActivity extends AppCompatActivity {

    EditText names,contact,editpass,editrepass;
    RadioGroup gender;
    Button submit;
    ProgressDialog dialog;
    Context context;

    String HttpUrl="http://reichprinz.com/teaAndroid/insertuser.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        context=this;
        names=(EditText)findViewById(R.id.editnames);
        contact=(EditText)findViewById(R.id.editcont);
        editpass=(EditText)findViewById(R.id.paswordtext);
        editrepass=(EditText)findViewById(R.id.passwordretyp);
        contact.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

        submit=(Button)findViewById(R.id.button);
        gender=(RadioGroup)findViewById(R.id.radiogroup);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (names.getText().toString().trim().length()<=0 ){
                    //Toast.makeText(RegistrationActivity.this, "Please Enter Name !", Toast.LENGTH_SHORT).show();
                    names.setError("Please Enter Name !");
                    names.requestFocus();
                }else if (contact.getText().toString().trim().length()<=0){
                    //Toast.makeText(RegistrationActivity.this, "Please Enter Mobile Number !", Toast.LENGTH_SHORT).show();
                    contact.setError("Please Enter Mobile Number !");
                    contact.requestFocus();
                }else if (contact.getText().toString().trim().length()!=10){
                    //Toast.makeText(context, "Please Enter Correct Number !", Toast.LENGTH_SHORT).show();
                    contact.setError("Please Enter Correct 10 digit Number !");
                    contact.requestFocus();
                }else if (editpass.getText().toString().trim().length()<=0){
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

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String ServerResponse) {

                                    // Hiding the progress dialog after all task complete.
                                    dialog.dismiss();
                                    if (ServerResponse.equals("Registered Successfully")) {
                                        // Showing response message coming from server.
                                        Toast.makeText(RegistrationActivity.this, "Registered Succesfully", Toast.LENGTH_LONG).show();
                                        Intent intent=new Intent(context,MainActivity.class);
                                        RegistrationActivity.this.finish();
                                        startActivity(intent);
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

                                    // Showing error message if something goes wrong.volleyError.toString()
                                    Toast.makeText(RegistrationActivity.this, "Server Not Available", Toast.LENGTH_LONG).show();
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
                            params.put("roles", "admin");
                            params.put("password", passwd);

                            return params;
                        }
                    };
                    // Creating RequestQueue.
                    RequestQueue requestQueue = Volley.newRequestQueue(RegistrationActivity.this);
                    // Adding the StringRequest object into requestQueue.
                    requestQueue.add(stringRequest);
                }
            }
        });
    }

}
