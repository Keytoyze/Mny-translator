package com.mnysqtp.com.mnyproject.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.mnysqtp.com.mnyproject.Adapter.PagerAdapter;
import com.mnysqtp.com.mnyproject.Fragment.AboutFragment;
import com.mnysqtp.com.mnyproject.Fragment.DictionaryFragment;
import com.mnysqtp.com.mnyproject.Fragment.TranslationFragment;
import com.mnysqtp.com.mnyproject.R;
import com.mnysqtp.com.mnyproject.Utils.SQLiteclass;
import com.mnysqtp.com.mnyproject.Utils.SharedPrefsUtils;
import com.mnysqtp.com.mnyproject.Utils.Util;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements AboutFragment.OnFragmentInteractionListener,
        DictionaryFragment.OnFragmentInteractionListener,
        TranslationFragment.OnFragmentInteractionListener{
    BottomNavigationBar BNB;
    FragmentPagerAdapter adapter;
    ViewPager viewPager;
    long mExitTime = 0;
    boolean show = true;
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
        viewPager = findViewById(R.id.main_viewPager);
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
                setFragmentTitle(position);
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(int position) {}

            @Override
            public void onTabReselected(int position) {}
        });
    }
    public void setFragmentInit() {
        adapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                setFragmentTitle(position);
                BNB.selectTab(position, false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    @Override
    public void onBackPressed() {
        if (show && SharedPrefsUtils.getBoolean("ans", true, this)) {
            show = false;
            new AlertDialog.Builder(this)
                    .setTitle("用户体验调查")
                    .setMessage("在退出之前，我们请求您花费两分钟时间完成一份调查问卷。这对于我们而言至关重要。\n由衷感谢您的使用！")
                    .setPositiveButton("去完成", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                            Util.sendInv(MainActivity.this);
                        }
                    })
                    .setNegativeButton("残忍拒绝", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            MainActivity.this.finish();
                        }
                    })
                    .setCancelable(true)
                    .show();
        } else
        if(System.currentTimeMillis() - mExitTime < 800) {
            this.finish();
        }
        else{
            Toast.makeText(this, "再按返回键退出！", Toast.LENGTH_LONG).show();
            mExitTime = System.currentTimeMillis();
        }
    }

    public void setFragmentTitle(int position) {
        switch (position){
            case 0:setTitle(R.string.app_name);break;
            case 1:setTitle(R.string.title_translation);break;
            case 2:setTitle(R.string.title_discovery);break;
            case 3:setTitle(R.string.title_about);break;
        }
    }
}
