package android.example.datastorage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public static final String FILENAME="namafile.txt";
    public static final String PREFNAME="com.example.datastorage.PREF";
    private TextView textBaca;
    private EditText editText;
    //pengecekan permission setelah uses permission di android manifest
    private static final String[] PERMISSIONS={
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static final int REQUEST_CODE=100;
    //notes: kalau permission blm disetujui dari user,
    // maka ada pop up untuk versi2 android terbaru

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textBaca=findViewById(R.id.text_baca);
        editText=findViewById(R.id.edit_text);
    }
    private static boolean hasPermission(Context context, String... permissions){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && permissions!=null){
            for(String permission:permissions){
                if(ActivityCompat.checkSelfPermission(context, permission)!= PackageManager.PERMISSION_GRANTED){
                    return false;

                }
            }
        }
        return true;

    }

    public void simpan(View view) {
        simpanFileES();
    }

    public void baca(View view) {
        bacaFileES();
    }

    public void hapus(View view) {
        deleteFileES();
    }
    //eksternal storage
    public void simpanFileES() {
        if (hasPermission(this, PERMISSIONS)) {
            String isiFile = editText.getText().toString();
            //direktori file
            File path = Environment.getExternalStorageDirectory();
            //buat file
            File file = new File(path.toString()+ "/NAMAFOLDER", FILENAME);
            FileOutputStream outputStream = null;

            try {
                file.createNewFile();
                outputStream = new FileOutputStream(file, false);
                outputStream.write(isiFile.getBytes());
                outputStream.flush();
                outputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_CODE);
        }
    }
    //baca file dari eksternal storage
    public void bacaFileES(){
        File path=Environment.getExternalStorageDirectory();
        File file = new File(path.toString(),FILENAME);
        if(file.exists()){
            //untuk membaca file exists
            StringBuilder text=new StringBuilder();
            try {
                BufferedReader br=new BufferedReader(new FileReader(file));
                String line=br.readLine();
                while(line!=null){
                    text.append(line);
                    line=br.readLine();
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            textBaca.setText(text.toString());
        }else{
            textBaca.setText("");
        }
    }

    //menghapus file dari external storage
    public void deleteFileES(){
        File path=Environment.getExternalStorageDirectory();
        File file = new File(path.toString(),FILENAME);
        if(file.exists()){
            file.delete();
        }
    }

    //share preferencenya
    public void simpanFileSP(){
        //cara nyimpan file di shared preference
        String isiFile=editText.getText().toString();
        SharedPreferences sharedPreferences=getSharedPreferences(PREFNAME, MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(FILENAME,isiFile);
        editor.commit();
    }

    //untuk baca shared preference
    public void bacaFileSP(){
        SharedPreferences sharedPreferences=getSharedPreferences(PREFNAME, MODE_PRIVATE);
        if (sharedPreferences.contains(FILENAME)
        ) {
            String mytext=sharedPreferences.getString(FILENAME,"");
            textBaca.setText(mytext);
        }else{
            textBaca.setText("");
        }
    }
    //tambahkan untuk baca dan delete file
    public void deleteFileSP(){
        SharedPreferences sharedPreferences=getSharedPreferences(PREFNAME, MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}