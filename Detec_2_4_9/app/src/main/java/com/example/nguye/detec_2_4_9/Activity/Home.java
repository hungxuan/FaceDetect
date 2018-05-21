package com.example.nguye.detec_2_4_9.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.nguye.detec_2_4_9.R;

public class Home extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.btnCreate:
                        Intent create = new Intent(Home.this, Create.class);
                        startActivity(create);
                        break;

                    case R.id.btnCheck:
                        Intent check = new Intent(Home.this, Check.class);
                        startActivity(check);
                        break;

                    case R.id.btnAttendance:
                        Intent attendance = new Intent(Home.this, ListStudent.class);
                        startActivity(attendance);
                        break;

                    case R.id.btnList:
                        Intent list = new Intent(Home.this, ListFace.class);
                        startActivity(list);
                        break;
                }
                return true;
            }
        });

    }

}
