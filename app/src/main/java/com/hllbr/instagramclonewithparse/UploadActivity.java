package com.hllbr.instagramclonewithparse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.acl.Permission;

public class UploadActivity extends AppCompatActivity {
    ImageView imageView;
    EditText commentText ;
    Button addPostButton ;
    Bitmap choosenImage ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        //ActionBar Operation
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //identification phase
        imageView = findViewById(R.id.imageView);
        commentText = findViewById(R.id.commentText);
        addPostButton = findViewById(R.id.AddPostButton);

    }
    public void SelectImage(View view){
        //MediaStore Operations
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //not allowes operations
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
        }else{
            //Allowed operations
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,1);
        }
    }//Bu yapı sonuçlandığında yapılacak işlemler =

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

       // What to do as a result of the permit process
        if(requestCode  ==2 ){
            if(grantResults.length >0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //What to do when startactivityforresult starts

        //Kullanıcı Media içerisinde resmi seçti bununla neler yapılacak
        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            //burada kullanıcının datasının Medianın telefonda kayıtlı olduğu yolu uri olarak alabilirim.

            //SDK Kontrolü =
            try {
            Uri imageData = data.getData();
            if(Build.VERSION.SDK_INT != 28){
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),imageData);
                choosenImage = ImageDecoder.decodeBitmap(source);
                imageView.setImageBitmap(choosenImage);
            }else{
                choosenImage= MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageData);
                imageView.setImageBitmap(choosenImage);
            }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void AddPostButton(View view){
        //POST Upload and intent operations


        //Parse Database resimlerin vb... kaydedilmesi ....

        String commentTextArea = commentText.getText().toString();

        //Image eklerken bu imageleri parse dosyası(parse file) bir objeye dönüştürmeme gerekiyor.
        //Veriyi(Image) byte dönüştürmem gerekiyor bu alana ekleme yapabilmek için

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        choosenImage.compress(Bitmap.CompressFormat.PNG,50,outputStream);//ziplemek gibi düşünülebilir.Bu işlem
        byte [] bytes = outputStream.toByteArray();
        ParseFile file = new ParseFile("image.png",bytes);

        ParseObject object = new ParseObject("Posts");
        object.put("image",file);
        object.put("commnet",commentTextArea);
        object.put("username", ParseUser.getCurrentUser().getUsername());
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Post Upload operation successful",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(),FeedActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
}