package com.mnysqtp.com.mnyproject.Activity;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.mnysqtp.com.mnyproject.Fragment.AboutFragment;
import com.mnysqtp.com.mnyproject.Fragment.BlankFragment;
import com.mnysqtp.com.mnyproject.Fragment.DictionaryFragment;
import com.mnysqtp.com.mnyproject.Fragment.DiscoveryFragment;
import com.mnysqtp.com.mnyproject.Fragment.TranslationFragment;
import com.mnysqtp.com.mnyproject.R;
import com.mnysqtp.com.mnyproject.Utils.SQLiteclass;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements AboutFragment.OnFragmentInteractionListener,
        DictionaryFragment.OnFragmentInteractionListener,
        TranslationFragment.OnFragmentInteractionListener,
        BlankFragment.OnFragmentInteractionListener{
    BottomNavigationBar BNB;
    FragmentTransaction ft;
    DictionaryFragment m1;
    TranslationFragment m2;
    DiscoveryFragment m3;
    AboutFragment m4;
    Fragment formerFragment;
    long mExitTime = 0;
    @Override
    public void onFragmentInteraction(Uri Uri){
        if(Uri.toString().equals("SEARCH")){
            Intent I=new Intent(this,SearchActivity.class);
            I.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            I.putExtra("CONTENT","");
            startActivityForResult(I,1437);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == 1437){
            if(findViewById(R.id.id_translation_edit) != null){
                BNB.selectTab(1);
                EditText et = findViewById(R.id.id_translation_edit);
                et.setText(data.getStringExtra("TRANSLATION"));
//                InputManager.HideInput(this,et);
                BottomNavigationBar BNB = findViewById(R.id.BottomNavigationB);
                ImageButton IB = findViewById(R.id.id_search_imageButton);
                IB.performClick();

            }else{
                Toast.makeText(this, "自动跳转至翻译界面失败！请手动在翻译界面输入。", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBottomNavigation();
        setFragmentInit();
        Intent I = getIntent();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);
        }
        setDatabaseInit();
    }
        //EditText et = findViewById(R.id.Translation);
        //et.setText("wefef");
        /*String [] re = SQLiteclass.getMandarinByRoman("goa");
        for (int i = 1; i <= re.length; i++){
            System.out.println(re[i-1]);
        }*/
    public void setDatabaseInit(){
        try{
            InputStream is=getAssets().open("Tran.sdb");
            SQLiteclass.Init(is);
            is.close();
        }catch (Exception e){
            Toast.makeText(this,"写出数据库失败，app无法正常使用！",Toast.LENGTH_LONG).show();
        }

    }
    public void setBottomNavigation(){
        BNB=findViewById(R.id.BottomNavigationB);
        BNB.setMode(BottomNavigationBar.MODE_FIXED);
        // BottomNavigationBar.BACKGROUND_STYLE_DEFAULT;
        // BottomNavigationBar.BACKGROUND_STYLE_RIPPLE
        // BottomNavigationBar.BACKGROUND_STYLE_STATIC
        BNB.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .setActiveColor(R.color.colorBlue)
                .setInActiveColor(R.color.colorPrimaryDark);
        BottomNavigationItem DictionaryItem = new BottomNavigationItem(R.drawable.ic_dictionary_black_24dp , R.string.title_home);
        BottomNavigationItem TranslationItem = new BottomNavigationItem(R.drawable.ic_translate_black_24dp , R.string.title_translation );
        BottomNavigationItem DiscoveryItem = new BottomNavigationItem(R.drawable.ic_discovery_black_24dp , R.string.title_discovery );
        BottomNavigationItem AboutItem = new BottomNavigationItem(R.drawable.ic_about_black_24dp , R.string.title_about );
        BNB.addItem(DictionaryItem).addItem(TranslationItem).addItem(DiscoveryItem).addItem(AboutItem);
        BNB.initialise();
        BNB.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position){
                    case 0:setFragment(m1);setTitle(R.string.app_name);break;
                    case 1:setFragment(m2);setTitle(R.string.title_translation);break;
                    case 2:setFragment(m3);setTitle(R.string.title_discovery);break;
                    case 3:setFragment(m4);setTitle(R.string.title_about);break;
                }
            }

            @Override
            public void onTabUnselected(int position) {}

            @Override
            public void onTabReselected(int position) {}
        });
    }
    public void setFragmentInit(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        m1 = new DictionaryFragment();
        m2 = new TranslationFragment();
        m3 = new DiscoveryFragment();
        m4 = new AboutFragment();
        transaction.add(R.id.id_MainFragment,m1);
        transaction.add(R.id.id_MainFragment,m2);
        transaction.add(R.id.id_MainFragment,m3);
        transaction.add(R.id.id_MainFragment,m4);
        transaction.hide(m2);
        transaction.hide(m3);
        transaction.hide(m4);
        transaction.commit();
        formerFragment = m1;
    }

    public void setFragment(Fragment F){
        if(F != formerFragment){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.hide(formerFragment);
            transaction.show(F);
            transaction.commit();
            formerFragment = F;
        }
    }

    public void Translate(){

    }

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - mExitTime < 800) {
            this.finish();
        }
        else{
            Toast.makeText(this, "再按返回键退出！", Toast.LENGTH_LONG).show();
            mExitTime = System.currentTimeMillis();
        }
    }}
