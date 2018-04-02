package com.mnysqtp.com.mnyproject.Utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mnysqtp.com.mnyproject.Utils.IOclass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SQLiteclass {
    private final static String filePath = "data/data/com.mnysqtp.com.mnyproject/Tran.db";
    private final static String MusicPath = "data/data/com.mnysqtp.com.mnyproject/m.mp3";
    private final static String comMusicPath = "data/data/com.mnysqtp.com.mnyproject/m1.mp3";
    private static SQLiteDatabase db;

    public static void Init(InputStream IS) throws IOException {
 //       if (!(new File(filePath).exists())) {
            IOclass.copy(IS,filePath);
  //      }
        db = SQLiteDatabase.openOrCreateDatabase(filePath,null);
    }

    public static String[] getRomanByMandarin(String Mandarin,int Max){
        if(Mandarin.isEmpty()){
            return null;
        }
        Cursor cursor = db.query(true,"Tran",new String[] {"*"},"Mandarin=?",new String[] {Mandarin},null,null,"Roman ASC",null);
        cursor.moveToFirst();
        if(cursor.getCount() > Max && Max != 0){
            String[] Re = new String[Max];
            cursor.moveToFirst();
            for (int i = 1; i <= Max; i++){
                Re[i-1]=cursor.getString(2);
                cursor.moveToNext();
            }
            cursor.close();
            return Re;
        }else{
            String[] Re = new String[cursor.getCount()];
            cursor.moveToFirst();
            for (int i = 1; i <= cursor.getCount(); i++){
                Re[i-1]=cursor.getString(2);
                cursor.moveToNext();
            }
            cursor.close();
            return Re;
        }

    }

    public static String[] getMandarinByRoman(String Roman,int Max){
        if(Roman.isEmpty()){
            return null;
        }
        Cursor cursor = db.query("Tran",new String[] {"*"},"Roman LIKE ?",new String[] {Roman+"%"},null,null,"Roman ASC");
        if(cursor.getCount() > Max && Max != 0){
            cursor.moveToFirst();
            String[] Re = new String[Max];
            for (int i = 1; i <= Max; i++){
                Re[i-1] = cursor.getString(0);
                cursor.moveToNext();
            }
            cursor.close();
            return Re;
        }else{
            cursor.moveToFirst();
            String[] Re = new String[cursor.getCount()];
            for (int i = 1; i <= cursor.getCount(); i++){
                Re[i-1] = cursor.getString(0);
                cursor.moveToNext();
            }
            cursor.close();
            return Re;
        }
    }

    public static String[] getStringByRoman(String Roman,int Max){
        if(Roman.isEmpty()){
            return null;
        }
        Cursor cursor = db.query(true,"Tran",new String[] {"*"},"Roman LIKE ?",new String[] {Roman+"%"},null,null,"Roman ASC",null);
        if(cursor.getCount() > Max && Max != 0){
            cursor.moveToFirst();
            String[] Re = new String[Max];
            for (int i = 1; i <= Max; i++){
                Re[i-1] = cursor.getString(2) + " " + cursor.getString(0);
                cursor.moveToNext();
            }
            cursor.close();
            return Re;
        }else{
            cursor.moveToFirst();
            String[] Re = new String[cursor.getCount()];
            for (int i = 1; i <= cursor.getCount(); i++){
                Re[i-1] = cursor.getString(2) + " "  + cursor.getString(0);
                cursor.moveToNext();
            }
            cursor.close();
            return Re;
        }
    }

    public static String[] getStringByMandarin(String Mandarin,int Max){
        if(Mandarin.isEmpty()){
            return null;
        }
        Cursor cursor = db.query(true,"Tran",new String[] {"*"},"Mandarin=?",new String[] {Mandarin},null,null,"Roman ASC",null);
        if(cursor.getCount() > Max && Max != 0){
            cursor.moveToFirst();
            String[] Re = new String[20];
            for (int i = 1; i <= Max; i++){
                Re[i-1] = cursor.getString(2) + " " + cursor.getString(0);
                cursor.moveToNext();
            }
            cursor.close();
            return Re;
        }else{
            cursor.moveToFirst();
            String[] Re = new String[cursor.getCount()];
            for (int i = 1; i <= cursor.getCount(); i++){
                Re[i-1] = cursor.getString(2) + " "  + cursor.getString(0);
                cursor.moveToNext();
            }
            cursor.close();
            return Re;
        }
    }

    public static Cursor getMandarinVocabulary() {
        return db.query("Vocabulary", new String[] {"*"}, null, null, null, null, null);
    }

    public static String[] getMinnanByMandarin(String Mandarin, int Max){
        if(Mandarin.isEmpty()){
            return null;
        }
        Cursor cursor = db.query("Tran",new String[] {"*"},"Mandarin=?",new String[] {Mandarin},null,null,"Roman ASC");
        if(cursor.getCount() > Max && Max != 0){
            cursor.moveToFirst();
            String[] Re = new String[Max];
            for (int i = 1; i <= Max; i++){
                Re[i-1] = cursor.getString(1);
                cursor.moveToNext();
            }
            cursor.close();
            return Re;
        }else{
            cursor.moveToFirst();
            String[] Re = new String[cursor.getCount()];
            for (int i = 1; i <= cursor.getCount(); i++){
                Re[i-1] = cursor.getString(1);
                cursor.moveToNext();
            }
            cursor.close();
            return Re;
        }
    }

    public static String getMusicByRoman(String Roman, int sex) throws IOException{
        Cursor cursor = db.query("Pron",new String[]{"*"},"Roman=?",new String[]{Roman},null,null,null);
        cursor.moveToFirst();
        byte[] content;
        if(sex == 0){
            content = cursor.getBlob(1);
        }else{
            content = cursor.getBlob(2);
        }
        cursor.close();
        FileOutputStream FOS = new FileOutputStream(MusicPath);
        FOS.write(content);
        FOS.flush();
        FOS.close();
        return MusicPath;
    }

    public static String[][] Translate(String Mandarin, StringBuffer Minnan, StringBuffer Luoma){
        String Mandarin3 = getMinnanVocabularyByMandarin(Mandarin);
        String Mandarin2 = Mandarin3.replaceAll("\\s","");
        String[][] Result = new String[2][Mandarin2.length()];
        for(int i = 0; i < Mandarin2.length(); i++) {
            String subMandarin = Mandarin2.substring(i, i + 1);
            String[] Re = getMinnanByMandarin(subMandarin, 1);
            if (Re != null && Re.length == 1) {
                Minnan.append(Re[0]);
                Result[0][i] = Re[0];
            } else {
                Minnan.append(subMandarin);
                Result[0][i] = subMandarin;
            }
            String[] LL = getRomanByMandarin(subMandarin, 1);
            if (LL != null && LL.length == 1) {
                Luoma.append(LL[0]).append(" ");
                Result[1][i] = LL[0];
            } else {
                Result[1][i] = "?";
            }
        }
        return Result;
    }

    public static String getMinnanVocabularyByMandarin(String mandarin) {
        Cursor cursor = getMandarinVocabulary();
        cursor.moveToFirst();
        String re = mandarin;
        do {
            re = re.replaceAll(cursor.getString(cursor.getColumnIndex("Mandarin")), cursor.getString(cursor.getColumnIndex("Minnan")));
        } while (cursor.moveToNext());
        cursor.close();
        return re;
    }

    public static String readSound(String[] Roman, int sex)throws IOException{
        FileOutputStream in = new FileOutputStream(new File(comMusicPath));
        byte[] Result;
        if (Roman.length == 0){
            return null;
        }else if (Roman.length == 1){
            Result = IOclass.read(getMusicByRoman(Roman[0], sex));
            IOclass.write(in, Result);
        }else{
            for (int i = 0; i < Roman.length; i++){
                if(Roman[i].equals("?")){
                    continue;
                }
                Result = IOclass.read(getMusicByRoman(Roman[i], sex));
                byte[] formatResult;
                if(i == Roman.length - 1){
                    formatResult = new byte[Result.length*17/20];
                }else{
                    formatResult = new byte[Result.length*7/10];
                }
                System.arraycopy(Result, Result.length/10, formatResult, 0, formatResult.length);

                IOclass.write(in, formatResult);
            }
        }
        in.flush();
        in.close();
        return comMusicPath;
    }



}
