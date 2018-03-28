package com.mnysqtp.com.mnyproject.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mnysqtp.com.mnyproject.Utils.InputManager;
import com.mnysqtp.com.mnyproject.Utils.Mediaclass;
import com.mnysqtp.com.mnyproject.R;
import com.mnysqtp.com.mnyproject.Utils.SQLiteclass;

import java.io.IOException;

public class SearchActivity extends AppCompatActivity {
    Activity acti = this;
    boolean S = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle("词典搜索");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        final EditText et = findViewById(R.id.id_SearchTrue);
        final ListView rv = findViewById(R.id.id_Listview);
        final ScrollView rl = findViewById(R.id.id_SV);
        final TextView Mandarin = findViewById(R.id.id_Mandarin);
        final TextView MNY = findViewById(R.id.id_MNY);
        final TextView LUOMA = findViewById(R.id.id_Roman);
        final Button tongyin = findViewById(R.id.id_samePro);
        final ImageButton ib_male = findViewById(R.id.id_male);
        final ImageButton ib_female = findViewById(R.id.id_female);
        final Context t = this;
        InputManager.ShowInput(acti, et);
        tongyin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText(LUOMA.getText());
                S = true;
                rl.setVisibility(View.INVISIBLE);
                InputManager.HideInput(acti, et);
            }
        });
        ib_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String name = SQLiteclass.getMusicByRoman(LUOMA.getText().toString(),0);
                    Mediaclass.Play(name, ib_male);
                }catch (IOException e){
                    Toast.makeText(t,e.toString(),Toast.LENGTH_LONG).show();
                    System.out.println(e.toString());
                }
            }
        });
        ib_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String name = SQLiteclass.getMusicByRoman(LUOMA.getText().toString(),1);
                    Mediaclass.Play(name, ib_female);
                }catch (IOException e){
                    Toast.makeText(t,e.toString(),Toast.LENGTH_LONG).show();
                    System.out.println(e.toString());
                }
            }
        });
        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl.setVisibility(View.INVISIBLE);
                rv.setVisibility(View.VISIBLE);
                S = true;
            }
        });
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String[] content;
                content = SQLiteclass.getStringByRoman(et.getText().toString(),20);
                if (et.getText().toString().isEmpty()){
                    content = null;
                }else {
                    if (content.length == 0) {
                        content = SQLiteclass.getStringByMandarin(et.getText().toString(), 20);
                        if (content.length == 0) {
                            content = new String[]{"整句翻译：" + et.getText().toString()};
                        }
                    }
                }
                if (content != null){
                    rv.setVisibility(View.VISIBLE);
                    rv.setAdapter(new ArrayAdapter(t,android.R.layout.simple_list_item_1,content));
                }else{
                    rv.setVisibility(View.INVISIBLE);
                }
            }
        });
        Intent I = getIntent();
        String word = I.getStringExtra("CONTENT");
        et.setText(word);
        rv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String con = (String)parent.getAdapter().getItem(position);
                if (con.startsWith("整句翻译")){
                    InputManager.HideInput(acti, et);
                    String[] Translation = con.split("：");
                    Intent Re = new Intent();
                    Re.putExtra("TRANSLATION",Translation[1]);
                    setResult(1437,Re);
                    acti.finish();
                }else{
                    String[] c = con.split("\\s+");
                    if (c.length == 2){
                        try{
                            String [] MN = SQLiteclass.getMinnanByMandarin(c[1],1);
                        if (MN != null && MN.length >= 1){
                            Mandarin.setText(c[1]);
                            //et.setText(c[1]);
                            LUOMA.setText(c[0]);
                            MNY.setText(MN[0]);
                            InputManager.HideInput(acti, et);
                            S = false;
                            rv.setVisibility(View.INVISIBLE);
                            rl.setVisibility(View.VISIBLE);
                            return;
                        }
                        }catch (Exception e){
                            Toast.makeText(t,e.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                    Toast.makeText(t,"诶，获取失败了。。。",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (S){
            super.onBackPressed();
        }else{
            findViewById(R.id.id_Listview).setVisibility(View.VISIBLE);
            findViewById(R.id.id_SV).setVisibility(View.INVISIBLE);
            S = true;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(S){
                    this.finish();
                }else{
                    findViewById(R.id.id_Listview).setVisibility(View.VISIBLE);
                    findViewById(R.id.id_SV).setVisibility(View.INVISIBLE);
                    InputManager.HideInput(acti,(EditText)findViewById(R.id.id_SearchTrue));
                    S = true;
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
