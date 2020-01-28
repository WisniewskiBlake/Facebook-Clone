package com.example.facebookapp.activity.activity.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.DnsResolver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.facebookapp.R;
import com.example.facebookapp.activity.activity.rest.ApiClient;
import com.example.facebookapp.activity.activity.rest.services.UserInterface;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "GOOGLEACTIVITY";
    private static  final int RC_SIGN_IN = 9001;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private SignInButton signInButton;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInButton = findViewById(R.id.sign_in_button);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        mFirebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Signing your In... Please Wait !");

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    //when the sign is button is clicked we start a new intent
    private void signIn() {
        // Configure Google Sign In
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser!=null){
            Log.d(TAG,"USER IS ALREAYD LOGGED IN");
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    //lec 15 2:10
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // When requestCode matches RCSIGNIN then we say google sign in successful, now we need to authenticate with firebaseAuthWithGoogle
        if (requestCode == RC_SIGN_IN) {
            progressDialog.show();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    //lec 15 2:10
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            final FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LoginActivity.this, new OnSuccessListener<InstanceIdResult>() {
                                @Override
                                public void onSuccess(InstanceIdResult instanceIdResult) {
                                    String userToken = instanceIdResult.getToken();
                                    String uid = user.getUid();
                                    String name = user.getDisplayName();
                                    String email = user.getEmail();
                                    String profileUrl =user.getPhotoUrl().toString();
                                    final String coverUrl = "";
                                    UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
                                    Call<Integer> call = userInterface.singin(new LoginActivity.UserInfo(uid,name,email,profileUrl,coverUrl,userToken));
                                    call.enqueue(new Callback<Integer>() {
                                        @Override
                                        public void onResponse(Call<Integer> call, retrofit2.Response<Integer> response) {
                                            progressDialog.dismiss();
                                            if(response.body() == 1) {
                                                Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                                finish();
                                            }else {
                                                Toast.makeText(LoginActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                                                FirebaseAuth.getInstance().signOut();
                                            }
                                        }
                                        @Override
                                        public void onFailure(Call<Integer> call, Throwable t) {
                                            FirebaseAuth.getInstance().signOut();
                                            progressDialog.dismiss();
                                         //   Toast.makeText(LoginActivity.this,"Login Failed",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }








    public class UserInfo{
        String uid,name,email,profileUrl,coverUrl,userToken;

        public UserInfo(String uid, String name, String email, String profileUrl, String coverUrl, String userToken) {
            this.uid = uid;
            this.name = name;
            this.email = email;
            this.profileUrl = profileUrl;
            this.coverUrl = coverUrl;
            this.userToken = userToken;
        }
    }
}
