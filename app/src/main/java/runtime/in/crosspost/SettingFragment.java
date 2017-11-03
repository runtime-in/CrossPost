package runtime.in.crosspost;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Zulkarnain Shah on 02/11/17.
 */

public class SettingFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener{

    Button btnFacebook, btnGoogle, btnTwitter, btnInstagram;
    public static SettingFragment settingFragment;
    public GoogleApiClient mGoogleApiClient;

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    public static SettingFragment getInstance() {
        if (settingFragment == null) {
            settingFragment = new SettingFragment();
        }
        return settingFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        btnFacebook = rootView.findViewById(R.id.btnFacebook);
        btnGoogle = rootView.findViewById(R.id.btnGoogle);
        btnTwitter = rootView.findViewById(R.id.btnTwitter);
        btnInstagram = rootView.findViewById(R.id.btnInstagram);

        ButtonClickListener buttonClickListener = new ButtonClickListener();
        btnFacebook.setOnClickListener(buttonClickListener);
        btnGoogle.setOnClickListener(buttonClickListener);
        btnTwitter.setOnClickListener(buttonClickListener);
        btnInstagram.setOnClickListener(buttonClickListener);

        //Configure Google sign-in options

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this.getActivity())
                .enableAutoManage((FragmentActivity) this.getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        return rootView;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("Connection failed");
    }

    /**
     * ButtonClickListener class
     **/
    class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnGoogle:
                    googleSignIn();
                    break;
                default:
                    break;
            }
        }
    }

    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Toast.makeText(this.getActivity(),"SUCCESS : "+ acct.getDisplayName(),Toast.LENGTH_LONG).show();
//            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
//            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(this.getActivity(),"FAILED",Toast.LENGTH_LONG).show();
//            updateUI(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage((FragmentActivity) getActivity());
        mGoogleApiClient.disconnect();
    }
}



