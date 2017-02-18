package com.hackathon.cardless;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.hackathon.cardless.Constants;
import com.hackathon.cardless.MainActivity;
import com.hackathon.cardless.R;
import com.hackathon.cardless.SimpleAlertDialog;
import com.microsoft.aad.adal.AuthenticationCallback;
import com.microsoft.aad.adal.AuthenticationContext;
import com.microsoft.aad.adal.AuthenticationResult;
import com.microsoft.aad.adal.AuthenticationSettings;
import com.microsoft.aad.adal.PromptBehavior;
import com.microsoft.aad.adal.UserIdentifier;

import static com.hackathon.cardless.Constants.ADDITIONAL_SCOPES;
import static com.hackathon.cardless.Constants.AUTHORITY_URL;
import static com.hackathon.cardless.Constants.CLIENT_ID;
import static com.hackathon.cardless.Constants.CURRENT_RESULT;
import static com.hackathon.cardless.Constants.EMAIL_SIGNIN_POLICY;
import static com.hackathon.cardless.Constants.REDIRECT_URL;
import static com.hackathon.cardless.Constants.SCOPES;
import static com.hackathon.cardless.Constants.TOKEN;


public class LoginActivity extends AppCompatActivity {

    private ProgressDialog mLoginProgressDialog;
    private AuthenticationContext mAuthContext;
    private AuthenticationResult sResult;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Show a loading dialog when waiting for the authentication token to return
        mLoginProgressDialog = new ProgressDialog(this);
        mLoginProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mLoginProgressDialog.setMessage("Login in progress...");
        mLoginProgressDialog.show();

        try{
            mAuthContext = new AuthenticationContext(LoginActivity.this, AUTHORITY_URL, true);
            String policy = EMAIL_SIGNIN_POLICY;
            AuthenticationSettings.INSTANCE.setSkipBroker(true);

            /*
            * This code is where the access token is obtained
            * Below chunk of code cannot be omitted
            */
            mAuthContext.acquireToken(LoginActivity.this, SCOPES,
                    ADDITIONAL_SCOPES, policy, CLIENT_ID,
                    REDIRECT_URL, getUserInfo(), PromptBehavior.Auto,
                    "nux=1&" + Constants.EXTRA_QP,
                    new AuthenticationCallback<AuthenticationResult>() {

                        @Override
                        public void onError(Exception exc) {
                            if (mLoginProgressDialog.isShowing()) {
                                mLoginProgressDialog.dismiss();
                            }
                            SimpleAlertDialog.showAlertDialog(LoginActivity.this,
                                    "Failed to get token", exc.getMessage());
                        }

                        @Override
                        public void onSuccess(AuthenticationResult result) {
                            if (mLoginProgressDialog.isShowing()) {
                                mLoginProgressDialog.dismiss();
                            }

                            if (result != null && !result.getToken().isEmpty()) {
                                setLocalToken(result);
                                sResult = result;
                                TOKEN = result.getToken();

                                Log.d("Token", result.getToken());
                                openMainActivity();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Token was not returned", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (Exception e) {
            SimpleAlertDialog.showAlertDialog(LoginActivity.this, "Exception caught",
                    e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAuthContext.onActivityResult(requestCode, resultCode, data);
    }

    //this method gives back a username to put into the login field
    public static UserIdentifier getUserInfo() {
        return new UserIdentifier("", UserIdentifier.UserIdentifierType.OptionalDisplayableId);
    }

    public static void setLocalToken(AuthenticationResult newToken) {
        CURRENT_RESULT = newToken;
    }

    private void openMainActivity(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
