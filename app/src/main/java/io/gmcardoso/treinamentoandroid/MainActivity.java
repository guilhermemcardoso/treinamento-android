package io.gmcardoso.treinamentoandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.gmcardoso.treinamentoandroid.domain.entity.AccessToken;
import io.gmcardoso.treinamentoandroid.domain.entity.GitHubApi;
import io.gmcardoso.treinamentoandroid.domain.entity.GitHubOAuthApi;
import io.gmcardoso.treinamentoandroid.domain.entity.GitHubStatusApi;
import io.gmcardoso.treinamentoandroid.domain.entity.Status;
import io.gmcardoso.treinamentoandroid.domain.entity.User;
import io.gmcardoso.treinamentoandroid.util.AppUtils;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.ivGitHub)
    ImageView mImgGitHub;
    @BindView(R.id.tvGitHub)
    TextView mTxtGitHub;
    @BindView(R.id.tilUsername)
    TextInputLayout mLayoutTxtUsername;
    @BindView(R.id.tilPassword)
    TextInputLayout mLayoutTxtPassword;
    @BindView(R.id.btnOAuth)
    Button mBtnOAuth;

    private static final String TAG = MainActivity.class.getSimpleName();

    private GitHubStatusApi mGitHubStatusApi;
    private GitHubApi mGitHubApi;
    private SharedPreferences mSharedPrefs;
    private GitHubOAuthApi mGitHubOAuthApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mBtnOAuth.setOnClickListener(v -> {
            final String baseUrl = GitHubOAuthApi.BASE_URL + "authorize";
            final String clientId = getString(R.string.oauth_client_id);
            final String redirectUri = getOAuthRedirectUri();
            final Uri uri = Uri.parse(baseUrl + "?client_id=" + clientId + "&redirect_uri=" + redirectUri);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        mGitHubStatusApi = GitHubStatusApi.RETROFIT.create(GitHubStatusApi.class);
        mGitHubApi = GitHubApi.RETROFIT.create(GitHubApi.class);
        mGitHubOAuthApi = GitHubOAuthApi.RETROFIT.create(GitHubOAuthApi.class);
        
        mSharedPrefs = getSharedPreferences(getString(R.string.sp_file), MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        changeGitHubStatus("", Status.Type.NONE.getColorRes());

        mGitHubStatusApi.lastMessage().enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if(response.isSuccessful()) {
                    Status status = response.body();
                    String data = new SimpleDateFormat("dd/MM/yyyy").format(status.created_on);
                    changeGitHubStatus(data + " - " + status.body, status.type.getColorRes());

                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        changeGitHubStatus(errorBody, Status.Type.MAJOR.getColorRes());
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Snackbar.make(mBtnOAuth, t.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

        processOAuthRedirectUri();
    }

    private void changeGitHubStatus(String message, int colorRes) {
        int color = ContextCompat.getColor(MainActivity.this, colorRes);

        mTxtGitHub.setText(message);
        mTxtGitHub.setTextColor(color);
        DrawableCompat.setTint(mImgGitHub.getDrawable(), color);
    }

    private String getOAuthRedirectUri() {
        return getString(R.string.oauth_schema) + "://" + getString(R.string.oauth_host);
    }

    @OnClick(R.id.btnBasicAuth)
    protected void onBasicAuthClick(View v) {
        if(AppUtils.validateRequiredFields(MainActivity.this, mLayoutTxtPassword, mLayoutTxtUsername)) {
            String username = mLayoutTxtUsername.getEditText().getText().toString();
            String password = mLayoutTxtPassword.getEditText().getText().toString();
            final String credential = Credentials.basic(username, password);

            mGitHubApi.basicAuth(credential).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful()) {
                        String login = response.body().login;
                        String credential_key = getString(R.string.sp_credential_key);
                        Snackbar.make(v, login, Snackbar.LENGTH_LONG).show();
                        mSharedPrefs.edit()
                                .putString(credential_key, credential)
                                .apply();

                    } else {
                        try {
                            String errorBody  = response.errorBody().string();
                            Snackbar.make(v, errorBody, Snackbar.LENGTH_LONG).show();
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Snackbar.make(v, t.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

    private void processOAuthRedirectUri() {
        // Os intent-filter's permitem a interação com o ACTION_VIEW
        final Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(this.getOAuthRedirectUri())) {
            String code = uri.getQueryParameter("code");
            if (code != null) {
                //TODO Pegar o access token (Client ID, Client Secret e Code)
                String clientId = getString(R.string.oauth_client_id);
                String clientSecret = getString(R.string.oauth_client_secret);

                mGitHubOAuthApi.accessToken(clientId, clientSecret, code).enqueue(new Callback<AccessToken>() {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        if(response.isSuccessful()) {
                            AccessToken accessToken = response.body();
                            String credential_key = getString(R.string.sp_credential_key);
                            Snackbar.make(mBtnOAuth, accessToken.access_token, Snackbar.LENGTH_LONG).show();
                            mSharedPrefs.edit()
                                    .putString(credential_key, accessToken.getAuthCredential())
                                    .apply();

                        } else {
                            try {
                                String errorBody  = response.errorBody().string();
                                Snackbar.make(mBtnOAuth, errorBody, Snackbar.LENGTH_LONG).show();
                            } catch (IOException e) {
                                Log.e(TAG, e.getMessage(), e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        Snackbar.make(mBtnOAuth, t.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });
            } else if (uri.getQueryParameter("error") != null) {
                //TODO Tratar erro

            }
            // Limpar os dados para evitar chamadas múltiplas
            getIntent().setData(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionCall:
                final String maikoNumber = "+55 16 99387-0941";
                final Uri uri = Uri.parse("tel:" + maikoNumber);
                final Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
