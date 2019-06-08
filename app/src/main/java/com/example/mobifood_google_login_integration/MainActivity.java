package com.example.mobifood_google_login_integration;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {



    private static final String TAG = "Mobifood";
    private SignInButton googleSignInButton;
    private GoogleSignInClient googleSignInClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        googleSignInButton = findViewById(R.id.sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("523545811687-ep71uclaq2p8mmrjlc5oku70pah74bhk.apps.googleusercontent.com").requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this,gso);

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent,101);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
        {
            switch (requestCode) {
                case 101:
                    try {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        //onLoggedIn(account);
                        String idToken = account.getIdToken();
                        Toast.makeText(this, idToken, Toast.LENGTH_SHORT).show();
                    } catch (ApiException e) {
                        e.printStackTrace();

                    }
                    break;
            }


        }
    }
    private void onLoggedIn(GoogleSignInAccount googleSignInAccount)
    {
        Intent intent = new Intent(MainActivity.this,SignInResult.class);
        intent.putExtra(SignInResult.GOOGLE_ACCOUNT,googleSignInAccount);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount  alreadyLoggedIn = GoogleSignIn.getLastSignedInAccount(this);
        if(alreadyLoggedIn!=null)
        {
            Toast.makeText(this,"Already Logged In ",Toast.LENGTH_LONG).show();
            onLoggedIn(alreadyLoggedIn);
        }else
        {
            Log.d(TAG,"Not Logged In");
        }
    }
}
