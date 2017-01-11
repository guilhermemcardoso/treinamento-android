package io.gmcardoso.treinamentoandroid;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;

import io.gmcardoso.treinamentoandroid.domain.entity.GitHubApi;
import io.gmcardoso.treinamentoandroid.domain.entity.GitHubOAuthApi;
import io.gmcardoso.treinamentoandroid.domain.entity.GitHubStatusApi;
import io.gmcardoso.treinamentoandroid.domain.entity.Status;
import io.gmcardoso.treinamentoandroid.domain.entity.User;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    // declaracao dos componentes utilizados na tela de login
    // por padrao, o Google recomenda usar a letra "m" antes do nome da variavel, relacionado a "member" da classe.
    private ImageView mImgGitHub;
    private TextView mTxtGitHub;
    private TextInputLayout mLayoutTxtUsername;
    private TextInputLayout mLayoutTxtPassword;
    private Button mBtnLogin;
    private Button mBtnOAuth;

    private static final String TAG = MainActivity.class.getSimpleName();

    private GitHubStatusApi mGitHubStatusApi;
    private GitHubApi mGitHubApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Binding dos componentes da tela de login
        mImgGitHub = (ImageView) findViewById(R.id.ivGitHub);
        mTxtGitHub = (TextView) findViewById(R.id.tvGitHub);
        mLayoutTxtUsername = (TextInputLayout) findViewById(R.id.tilUsername);
        mLayoutTxtPassword = (TextInputLayout) findViewById(R.id.tilPassword);
        mBtnLogin = (Button) findViewById(R.id.btnLogin);
        mBtnOAuth = (Button) findViewById(R.id.btnOAuth);
        // fim do binding

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateLoginFields()) {
                    String username;
                    String password;

                    username = mLayoutTxtUsername.getEditText().getText().toString();
                    password = mLayoutTxtPassword.getEditText().getText().toString();

                    loginGitHub(username, password);
                } else {

                }
            }
        });

        mBtnOAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateLoginFields()) {
                    String username;
                    String password;

                    username = mLayoutTxtUsername.getEditText().getText().toString();
                    password = mLayoutTxtPassword.getEditText().getText().toString();

                    oAuthGitHub(username, password);
                } else {

                }
            }
        });

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .create();

        final Retrofit rfStatus = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(GitHubStatusApi.BASE_URL)
                .build();

        final Retrofit rfGitHub = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(GitHubApi.BASE_URL)
                .build();

        mGitHubStatusApi = rfStatus.create(GitHubStatusApi.class);
        mGitHubApi = rfGitHub.create(GitHubApi.class);
    }

    private boolean validateLoginFields() {

        boolean flag = true;

        if(mLayoutTxtPassword.getEditText().getText().toString().isEmpty()) {

            mLayoutTxtPassword.setError(getString(R.string.msg_errorPasswordEmpty));
            mLayoutTxtPassword.setErrorEnabled(true);
            flag = false;
        } else {
            mLayoutTxtPassword.setErrorEnabled(false);
        }

        if(mLayoutTxtUsername.getEditText().getText().toString().isEmpty()) {

            mLayoutTxtUsername.setError(getString(R.string.msg_errorUsernameEmpty));
            mLayoutTxtUsername.setErrorEnabled(true);
            flag = false;
        } else {
            mLayoutTxtUsername.setErrorEnabled(false);
        }

        return flag;
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
                changeGitHubStatus(t.getMessage(), Status.Type.MAJOR.getColorRes());
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

    private void loginGitHub(String username, String password) {

        String hash = Credentials.basic(username, password);

        mGitHubApi.login(hash).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    User user = response.body();
                    Toast.makeText(MainActivity.this, user.login, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "NAO FOI :(", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MainActivity.this, "NAO FOI MESMO :(", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void oAuthGitHub(String username, String password) {

        final String baseUrl = GitHubOAuthApi.BASE_URL + "authorize";
        final String clientId = getString(R.string.oauth_client_id);
        final String redirectUri = this.getOAuthRedirectUri();
        final Uri uri = Uri.parse(baseUrl + "?client_id=" + clientId + "&redirect_uri=" + redirectUri);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private String getOAuthRedirectUri() {
        return getString(R.string.oauth_schema) + "://" + getString(R.string.oauth_host);
    }

    private void processOAuthRedirectUri() {
        // Os intent-filter's permitem a interação com o ACTION_VIEW
        final Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(this.getOAuthRedirectUri())) {
            String code = uri.getQueryParameter("code");
            if (code != null) {
                //TODO Pegar o access token (Client ID, Client Secret e Code)
            } else if (uri.getQueryParameter("error") != null) {
                //TODO Tratar erro
            }
            // Limpar os dados para evitar chamadas múltiplas
            getIntent().setData(null);
        }
    }
}
