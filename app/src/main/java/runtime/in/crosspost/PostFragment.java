package runtime.in.crosspost;

import android.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialRefreshListener;
import com.google.api.client.auth.oauth2.TokenErrorResponse;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plusDomains.PlusDomains;
import com.google.api.services.plusDomains.model.Acl;
import com.google.api.services.plusDomains.model.Activity;
import com.google.api.services.plusDomains.model.PlusDomainsAclentryResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Zulkarnain Shah on 02/11/17.
 */

public class PostFragment extends Fragment implements View.OnClickListener {

    public static PostFragment postFragment;

    CheckBox chbFacebook, chbGoogle, chbTwitter, chbInstagram;
    Button btnPost;
    EditText etMain;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post, container, false);
        chbFacebook = rootView.findViewById(R.id.facebook);
        chbGoogle = rootView.findViewById(R.id.google);
        chbTwitter = rootView.findViewById(R.id.twitter);
        chbInstagram = rootView.findViewById(R.id.instagram);
        btnPost = rootView.findViewById(R.id.btnPost);
        etMain = rootView.findViewById(R.id.etMain);

        MyCheckedChangeListener checkedChangeListener = new MyCheckedChangeListener();
        chbFacebook.setOnCheckedChangeListener(checkedChangeListener);
        chbGoogle.setOnCheckedChangeListener(checkedChangeListener);
        chbTwitter.setOnCheckedChangeListener(checkedChangeListener);
        chbInstagram.setOnCheckedChangeListener(checkedChangeListener);

        btnPost.setOnClickListener(this);
        return rootView;
    }

    public static PostFragment getInstance() {
        if (postFragment == null) {
            postFragment = new PostFragment();
        }
        return postFragment;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnPost) {
            String text = etMain.getText().toString();
            if (text.length() > 0) {
                try {
                    postUpdate(text);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //TODO: Unhandled
            }
        }
    }

    private void postUpdate(final String text) throws IOException {
        // Create a list of ACL entries
        PlusDomainsAclentryResource resource = new PlusDomainsAclentryResource();
        resource.setType("domain"); // Share to domain

        List<PlusDomainsAclentryResource> aclEntries = new ArrayList<>();
        aclEntries.add(resource);

        final Acl acl = new Acl();
        acl.setItems(aclEntries);
        acl.setDomainRestricted(true);  // Required, this does the domain restriction

// Create a new activity object


// Execute the API request, which calls `activities.insert` for the logged in user

        // List the scopes your app requires:
        List<String> SCOPE = Arrays.asList(
                "https://www.googleapis.com/auth/plus.me",
                "https://www.googleapis.com/auth/plus.stream.write");

// The following redirect URI causes Google to return a code to the user's
// browser that they then manually provide to your app to complete the
// OAuth flow.
        final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";

        final String CLIENT_ID = "26772993288-mheh20j4ev3vuqu7622crcv44dpmf9ok.apps.googleusercontent.com";
        final String CLIENT_SECRET = "";

        final GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                new NetHttpTransport(),
                new JacksonFactory(),
                CLIENT_ID, // This comes from your API Console project
                CLIENT_SECRET, // This, as well
                SCOPE)
                .setApprovalPrompt("force")
                // Set the access type to offline so that the token can be refreshed.
                // By default, the library will automatically refresh tokens when it
                // can, but this can be turned off by setting
                // dfp.api.refreshOAuth2Token=false in your ads.properties file.
                .setAccessType("offline")
                .build();

// This command-line server-side flow example requires the user to open the
// authentication URL in their browser to complete the process. In most
// cases, your app will use a browser-based server-side flow and your
// user will not need to copy and paste the authorization code. In this
// type of app, you would be able to skip the next 5 lines.
// You can also look at the client-side and one-time-code flows for other
// options at https://developers.google.com/+/web/signin/
        String url = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
        System.out.println("Please open the following URL in your browser then " +
                "type the authorization code:");
        System.out.println("  " + url);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String code = br.readLine();
// End of command line prompt for the authorization code.

        String googleAccessToken = PreferenceManager.getDefaultSharedPreferences(PostFragment.this.getActivity()).getString("googleAccessToken", null);

        final GoogleTokenResponse tokenResponse = new GoogleTokenResponse();
        tokenResponse.setAccessToken(googleAccessToken);

        GoogleCredential credential = new GoogleCredential().setAccessToken(googleAccessToken);

//        GoogleCredential credential = new GoogleCredential.Builder()
//                .setTransport(new NetHttpTransport())
//                .setJsonFactory(new JacksonFactory())
//                .setClientSecrets(CLIENT_ID, CLIENT_SECRET)
//                .addRefreshListener(new CredentialRefreshListener() {
//                    @Override
//                    public void onTokenResponse(Credential credential, TokenResponse tokenResponse) {
//                        // Handle success.
//                        System.out.println("Credential was refreshed successfully.");
//                    }
//
//                    @Override
//                    public void onTokenErrorResponse(Credential credential,
//                                                     TokenErrorResponse tokenErrorResponse) {
//                        // Handle error.
//                        System.err.println("Credential was not refreshed successfully. "
//                                + "Redirect to error page or login screen.");
//                    }
//                })
//                // You can also add a credential store listener to have credentials
//                // stored automatically.
//                //.addRefreshListener(new CredentialStoreRefreshListener(userId, credentialStore))
//                .build();

// Set authorized credentials.
        credential.setFromTokenResponse(tokenResponse);
// Though not necessary when first created, you can manually refresh the
// token, which is needed after 60 minutes.
        try {
            credential.refreshToken();
        } catch (IOException e) {
            e.printStackTrace();
        }

// Create a new authorized API client
        final PlusDomains plusDomains = new PlusDomains.Builder(new NetHttpTransport(), new JacksonFactory(), credential).build();
        final Activity activity = new Activity()
                .setObject(new Activity.PlusDomainsObject().setOriginalContent(text))
                .setAccess(acl);

        new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... strings) {
                try {
                    plusDomains.activities().insert("me", activity).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }.execute();


//        new AsyncTask<GoogleTokenResponse, String, GoogleTokenResponse>() {
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            @Override
//            protected GoogleTokenResponse doInBackground(GoogleTokenResponse... tokenResponse) {
//                try {
//                    if (tokenResponse.length > 0) {
//                        tokenResponse[0] = flow.newTokenRequest("4/eONARx0kGUa7UZRFYWyIq9iUbTdnWYA6gzZgnxYD9os")
//                                .setRedirectUri(REDIRECT_URI).execute();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                if (tokenResponse.length > 0)
//                    return tokenResponse[0];
//                else
//                    return null;
//            }
//
//            @Override
//            protected void onPostExecute(GoogleTokenResponse tokenResponse) {
//                super.onPostExecute(tokenResponse);
//                if(tokenResponse == null){
//                    return;
//                }
//
//                GoogleCredential credential = new GoogleCredential.Builder()
//                        .setTransport(new NetHttpTransport())
//                        .setJsonFactory(new JacksonFactory())
//                        .setClientSecrets(CLIENT_ID, CLIENT_SECRET)
//                        .addRefreshListener(new CredentialRefreshListener() {
//                            @Override
//                            public void onTokenResponse(Credential credential, TokenResponse tokenResponse) {
//                                // Handle success.
//                                System.out.println("Credential was refreshed successfully.");
//                            }
//
//                            @Override
//                            public void onTokenErrorResponse(Credential credential,
//                                                             TokenErrorResponse tokenErrorResponse) {
//                                // Handle error.
//                                System.err.println("Credential was not refreshed successfully. "
//                                        + "Redirect to error page or login screen.");
//                            }
//                        })
//                        // You can also add a credential store listener to have credentials
//                        // stored automatically.
//                        //.addRefreshListener(new CredentialStoreRefreshListener(userId, credentialStore))
//                        .build();
//
//// Set authorized credentials.
//                credential.setFromTokenResponse(tokenResponse);
//// Though not necessary when first created, you can manually refresh the
//// token, which is needed after 60 minutes.
//                try {
//                    credential.refreshToken();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//// Create a new authorized API client
//                PlusDomains plusDomains = new PlusDomains.Builder(new NetHttpTransport(), new JacksonFactory(), credential).build();
//                try {
//                    Activity activity = new Activity()
//                            .setObject(new Activity.PlusDomainsObject().setOriginalContent(text))
//                            .setAccess(acl);
//                    activity = plusDomains.activities().insert("me", activity).execute();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.execute(tokenResponse);

    }

    class MyCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (compoundButton.getId() == R.id.facebook) {
                if (isChecked)
                    chbFacebook.setTextColor(Color.parseColor(Constants.Colors.FacebookBlue));
                else
                    chbFacebook.setTextColor(Color.BLACK);

            } else if (compoundButton.getId() == R.id.google) {
                if (isChecked)
                    chbGoogle.setTextColor(Color.parseColor(Constants.Colors.GoogleRed));
                else
                    chbGoogle.setTextColor(Color.BLACK);
            } else if (compoundButton.getId() == R.id.twitter) {
                if (isChecked)
                    chbTwitter.setTextColor(Color.parseColor(Constants.Colors.TwitterBlue));
                else
                    chbTwitter.setTextColor(Color.BLACK);
            } else if (compoundButton.getId() == R.id.instagram) {
                if (isChecked)
                    chbInstagram.setTextColor(Color.parseColor(Constants.Colors.InstagramViolet));
                else
                    chbInstagram.setTextColor(Color.BLACK);
            }
        }
    }


}
