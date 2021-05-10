package com.hllbr.instagramclonewithparse;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PostClass extends ArrayAdapter<String> {//customView ile feedActivity bağlamak için bir adapter yazıyorum
    private final ArrayList<String> username ;//Kullanıcı adlarını tutmak için
    private final ArrayList<String> userComment;//Yorumları tutmak için
    private final ArrayList<Bitmap> userImage ;//Resimleri tutmak için
    private final Activity context;//constructor için bir context ihtiyacım var onuda bu yapıyla sağlamış oluyorum

    //Parse dan alınan verileri feedActivity içerisinde download edeceğim bu class(sınıfı) kullanarak feed içindekli verileri buraya göndererek customView'e tek tek atayacağız
    public PostClass(ArrayList<String> username, ArrayList<String> userComment, ArrayList<Bitmap> userImage, Activity context){
        super(context,R.layout.custom_view,username);
        this.username = username;
        this.userComment = userComment;
        this.userImage = userImage;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.custom_view,null);
        TextView userNameText = customView.findViewById(R.id.custom_view_username_text);
        TextView commentText = customView.findViewById(R.id.comment_text_custom_view);
        ImageView imageView = customView.findViewById(R.id.image_view_customview);
        //Yeni oluşturulan objeleri tanıtabildim.

        userNameText.setText(username.get(position));
        imageView.setImageBitmap(userImage.get(position));
        commentText.setText(userComment.get(position));
        return customView;
    }
}
