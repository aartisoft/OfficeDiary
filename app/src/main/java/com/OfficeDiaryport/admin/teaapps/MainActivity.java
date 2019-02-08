package com.OfficeDiaryport.admin.teaapps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    Button login;
    //Button register;
    EditText username,pass;
    CheckBox showp;

    ProgressDialog dialog;

    String url="http://reichprinz.com/teaAndroid/checkuser.php";

    Context context;
    SharedPreferences.Editor editor;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        context=this;

        login=(Button)findViewById(R.id.button3);
        //register=(Button)findViewById(R.id.buttonregister);

        username=(EditText) findViewById(R.id.textuname);
        pass=(EditText) findViewById(R.id.textpass);
        showp=(CheckBox)findViewById(R.id.checkBox);

        showp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // show password
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    pass.setSelection(pass.getText().length());
                } else {
                    // hide password
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    pass.setSelection(pass.getText().length());
                }
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().trim().length()<=0 ){
                    Toast.makeText(context, "Please Enter Mobile Number !", Toast.LENGTH_SHORT).show();
                    username.requestFocus();
                }else if (pass.getText().toString().trim().length()<=0){
                    Toast.makeText(context, "Please Enter Password !", Toast.LENGTH_SHORT).show();
                    pass.requestFocus();
                }else {
                    try  {
                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {

                    }
                    serverdata(username.getText().toString(),pass.getText().toString());
                }
            }
        });

        /*register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(getApplicationContext(),RegistrationActivity.class);
                startActivity(it);
            }
        });*/

    }

    public void serverdata(final String mobil, final String passwd){
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading....");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                dialog.dismiss();
                if (string.equals("not")){
                    Toast.makeText(context, "Username and password Not match", Toast.LENGTH_SHORT).show();
                    username.requestFocus();
                }else {
                    try {
                        JSONArray fruitsArray = new JSONArray(string);
                        for (int i = 0; i < fruitsArray.length(); ++i) {
                            JSONObject jsonObject = fruitsArray.getJSONObject(i);
                            String name = jsonObject.getString("name");
                            String mobile = jsonObject.optString("mobile");
                            String iduser = jsonObject.getString("iduser");
                            String roles_id = jsonObject.optString("roles_id");
                            String admin_id = jsonObject.optString("admin_id");
                            String role = jsonObject.optString("role");
                            editor.putString("user_name", name); // Storing string
                            editor.putString("user_id", iduser);
                            editor.putString("mobile", mobile);
                            editor.putString("role_id", roles_id);
                            editor.putString("role", role);
                            editor.putString("admin_id", admin_id);
                            editor.commit();
                            DataClass.user_id=iduser;
                            DataClass.admin_id=admin_id;
                            DataClass.user_name=name;
                            jumpMethod(role);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "Some error occurred!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Something went wrong Check your Connection !");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setNeutralButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        serverdata(mobil,passwd);
                    }
                });
                alertDialogBuilder.show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();
                // Adding All values to Params.
                params.put("mobile", mobil);
                params.put("password", passwd);

                return params;
            }
        };
        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
        rQueue.add(request);
    }

    public void jumpMethod(String id){
        if (id.toLowerCase().equals("admin")) {
            Intent it = new Intent(getApplicationContext(), NavigationActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(it);
        }else {
            Intent it = new Intent(getApplicationContext(), userActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(it);
        }
        //MainActivity.this.finish();
    }

}
