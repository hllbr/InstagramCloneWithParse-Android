package com.hllbr.instagramclonewithparse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {
//Feed ekranına bir adet Sign Out ekranı eklemem gerkiyor istediği zaman uygulamadan çıkış yapabilsin diye.
//Oluşturduğum itemleri(menu) bu alanda bağlamam gerekiyor.
    ListView listView;
    //Post class bağlanırken benden arrayList istenecek gerekli tanımlamaları bu alanda yapıyorum
    ArrayList<String> userNameFromParse;
    ArrayList<String> userCommentFromParse;
    ArrayList<Bitmap> userImageFromParse;
    PostClass postClass;//adaptör


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        setTitle("Feed Screen");
        /*ActionBar actionBar = getSupportActionBar();
        actionBar.hide();*/
        listView = findViewById(R.id.listView);

        userNameFromParse = new ArrayList<String>();
        userCommentFromParse = new ArrayList<String>();
        userImageFromParse = new ArrayList<Bitmap>();

        postClass = new PostClass(userNameFromParse,userCommentFromParse,userImageFromParse,this);

        listView.setAdapter(postClass);
        download();
    }
    //Oluşturduğum itemleri(menu) bu alanda bağlamam gerekiyor.

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_item_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.add_post){
            //Kullanıcı yeni bir post eklemek isterse
            Intent intent = new Intent(FeedActivity.this,UploadActivity.class);
            startActivity(intent);
        }else if(item.getItemId() == R.id.log_out){
            //Kullanıcı Çıkış yapmak isterse
            //ParseUser.logOut(); Bunun yerine callBack ile birlikte bir logout işlemi gerçekleştirebilirim

            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if(e != null){
                        Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                    }else{
                        Intent intent = new Intent(FeedActivity.this,SignUpActivity.class);
                        startActivity(intent);
                    }
                }
            });

        }
        return super.onOptionsItemSelected(item);
    }
    public void download() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e != null) {
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                } else {

                    if (objects.size() > 0) {

                        for (final ParseObject object : objects) {

                            ParseFile parseFile = (ParseFile) object.get("image");

                            parseFile.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {

                                    if (e == null && data != null) {

                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);

                                        userImageFromParse.add(bitmap);
                                        userNameFromParse.add(object.getString("username"));
                                        userCommentFromParse.add(object.getString("commnet"));

                                        postClass.notifyDataSetChanged();


                                    }

                                }
                            });

                        }

                    }

                }

            }
        });



    }
}