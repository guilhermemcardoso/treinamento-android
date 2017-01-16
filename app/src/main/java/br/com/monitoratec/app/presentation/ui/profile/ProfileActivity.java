package br.com.monitoratec.app.presentation.ui.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;
import javax.inject.Named;

import br.com.monitoratec.app.R;
import br.com.monitoratec.app.domain.entity.User;
import br.com.monitoratec.app.presentation.base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, ProfileContract.View {

    @Inject
    ProfileContract.Presenter mPresenter;

    @Inject @Named("secret")
    SharedPreferences mSharedPrefs;

    ImageView mProfileImage;

    @BindView(R.id.txtUsername)
    TextView mUsername;

    @BindView(R.id.txtZenMessage)
    TextView mZenMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        mProfileImage = (ImageView) hView.findViewById(R.id.iv_my_img_profile);

        ButterKnife.bind(this);
        super.getDaggerUiComponent().inject(this);

        mPresenter.setView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String credentialKey = getString(R.string.sp_credential_key);
        String authorization = mSharedPrefs.getString(credentialKey, "");
        mPresenter.getUser(authorization);
        mPresenter.loadZenMessage();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_repos) {

        } else if (id == R.id.nav_logout) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
        return false;
    }

    @Override
    public void onLoadZenMessageComplete(String zenMessage) {
        mZenMessage.setText(zenMessage);
    }

    @Override
    public void onGetUserComplete(User user) {
        Picasso.with(this).load(user.avatarUrl).into(mProfileImage);
        mUsername.setText(user.login);
    }

    @Override
    public void onError(String message) {
        Snackbar.make(mUsername, message, Snackbar.LENGTH_LONG).show();
    }
}
