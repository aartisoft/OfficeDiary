package com.OfficeDiaryport.admin.teaapps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstActivity extends AppCompatActivity {


    Button login,signupadmin,signupusr;

    SharedPreferences.Editor editor;
    SharedPreferences pref;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        login=(Button)findViewById(R.id.button);
        signupadmin=(Button)findViewById(R.id.button2);
        signupusr=(Button)findViewById(R.id.button3);
        context=this;

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        if (pref.contains("user_name")){
            DataClass.user_id=pref.getString("user_id", "");
            DataClass.user_name=pref.getString("user_name", "");
            DataClass.admin_id=pref.getString("admin_id", "");
            //pass admin letter for check admin or user
            jumpMethod(pref.getString("role",""));
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                //FirstActivity.this.finish();
                startActivity(intent);
            }
        });

        signupadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),RegistrationActivity.class);
                //FirstActivity.this.finish();
                startActivity(intent);
            }
        });

        signupusr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),UserRegistActivity.class);
                //FirstActivity.this.finish();
                startActivity(intent);
            }
        });
    }

    public void jumpMethod(String id){
        if (id.toLowerCase().equals("admin")) {
            Intent it = new Intent(getApplicationContext(), NavigationActivity.class);
            startActivity(it);
        }else {
            Intent it = new Intent(getApplicationContext(), userActivity.class);
            startActivity(it);
        }
        FirstActivity.this.finish();
    }

}
