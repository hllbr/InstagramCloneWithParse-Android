package com.hllbr.instagramclonewithparse;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {
//First Activity //
    //User create,User Control,Login operations....
    TextView problemText;
    EditText nameText,passwordText;
    CheckBox checkBox;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        problemText = findViewById(R.id.problemText);
        nameText = findViewById(R.id.userNameText);
        passwordText = findViewById(R.id.UserPasswordName);
        checkBox = findViewById(R.id.checkBox);
        //setTitle("Creative Thinking");
        //Actionbar operation
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //User control operation
        ParseUser parseUser = ParseUser.getCurrentUser();//Güncel kullanıcıyı al şeklinde bir tanımlama eğer güncel bir kullanıcı varsa bunu al getir.

        //Bu veriyi kontrol ediyorum bu veri boş değilse .Gerçekten giriş yapmış bir kullanıcı varsa bu ekranla değil Feed ekranıyla bu kullanıcıyı karşılamam gerekiyor
        if(parseUser != null){
            //Bu kontrolü gerçekleştirmek için bir kontrol sınıfı(class) oluşturabilirm fakat en kısa yolu bu daha güvenilir yöntemlerde mevcut

            Intent intent = new Intent(getApplicationContext(),FeedActivity.class);
            startActivity(intent);
        }else{
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        passwordText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }else{
                        passwordText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                }
            });
        }
    }
    public void signIn(View view){
        //Login operations
        ParseUser.logInInBackground(nameText.getText().toString(), passwordText.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null){
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();

                }else{
                   //bu sefer bize bir kullanıcı veriyor alt satırdaki yöntemin aksine kullanıcıya merhaba diyebilirim.
                    Toast.makeText(getApplicationContext(),"Welcome "+user.getUsername(),Toast.LENGTH_LONG).show();
                    //INTENT
                    Intent intent = new Intent(getApplicationContext(),FeedActivity.class);
                    //finish();
                    startActivity(intent);
                }
            }
        });
    }
    public void SignUp(View view){
        //SignUp operations/Kullanıcı kayıt işlemleri =
        ParseUser user = new ParseUser();
        user.setUsername(nameText.getText().toString());
        user.setPassword(passwordText.getText().toString());
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"User Created!",Toast.LENGTH_LONG).show();
                    //Nereye gideceksem oraya intent yapmam gerekiyor.
                    Intent intent = new Intent(getApplicationContext(),FeedActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}