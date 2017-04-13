package ru.sk42.tradeodata.Activities.Settings;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.sk42.tradeodata.Activities.Documents_List.DocList_Activity;
import ru.sk42.tradeodata.Model.Catalogs.User;
import ru.sk42.tradeodata.Model.Catalogs.VehicleType;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.R;

import static ru.sk42.tradeodata.R.id.doclist__nav_view;
import static ru.sk42.tradeodata.R.id.settings__nav;


/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: St</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">St
 * API Guide</a> for more information on developing a St UI.
 */
public class SettingsActivity extends AppCompatActivity implements SettingsInterface, NavigationView.OnNavigationItemSelectedListener {

    static final String TAG = "SETTINGS";

    boolean calledFromMenu;

    Toolbar mToolbar;

    @Bind(R.id.settings__drawer)
    DrawerLayout mDrawerLayout;


    private ActionBarDrawerToggle mDrawerToggle;

    @Bind(R.id.settings__nav)
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings__activity);
        ButterKnife.bind(this, this);

        setToolbar();
        setDrawer();
        setTitle("");
        mDrawerLayout.openDrawer(Gravity.LEFT);

        if (getIntent().getIntExtra(Constants.REQUEST_SETTINGS_USER_LABEL, 0) != 0) {
            showUserSelectionFragment(false);
        } else {
            showConnectionFragment();
        }
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    public void setTitle(String t, String st) {
        mToolbar.setSubtitle("");
        mToolbar.setTitle("");
        TextView tv = (TextView) mToolbar.findViewById(R.id.settings__title);
        tv.setText(t);
        tv = (TextView) mToolbar.findViewById(R.id.settings__subtitle);
        tv.setText(st);

    }

    private void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.settings__toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

    }


    private void setDrawer() {

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        navigationView = (NavigationView) findViewById(settings__nav);
        navigationView.setNavigationItemSelectedListener(this);
        mDrawerToggle.syncState();

    }


    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        int id = item.getItemId();
        switch (id) {
            case R.id.settings__connection:
                //connection settings
                showConnectionFragment();
                break;
            case R.id.settings__users:
                //mVehicleType select
                showUserSelectionFragment(true);
                break;
            case R.id.settings__printers:
                //printer
                PrinterFragment printerFragment = PrinterFragment.newInstance(1);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings__frame, printerFragment, printerFragment.getClass().getName())
                        .addToBackStack(printerFragment.getClass().getName())
                        .commit();
                break;
            case R.id.settings__scanner:
                ScannerFragment fragment = ScannerFragment.newInstance();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings__frame, fragment, fragment.getClass().getName())
                        //  .addToBackStack(fragment.getClass().getName())
                        .commit();
                //scanner
                break;
            case R.id.settings__vehicle_types:
                VehicleTypesFragment vehicleTypesFragment = VehicleTypesFragment.newInstance(1);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings__frame, vehicleTypesFragment, vehicleTypesFragment.getClass().getName())
                        .addToBackStack(vehicleTypesFragment.getClass().getName())
                        .commit();
                break;
        }
        return false;
    }

    private void showUserSelectionFragment(boolean mCalledFromMenu) {
        calledFromMenu = mCalledFromMenu;
        UserSelectionFragment userSelectionFragment = UserSelectionFragment.newInstance(mCalledFromMenu);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings__frame, userSelectionFragment, userSelectionFragment.getClass().getName())
                .addToBackStack(userSelectionFragment.getClass().getName())
                .commit();
    }

    private void showConnectionFragment() {
        ConnectionFragment connectionFragment = ConnectionFragment.newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings__frame, connectionFragment, connectionFragment.getClass().getName())
                .addToBackStack(connectionFragment.getClass().getName())
                .commit();

    }

    @Override
    public void onValueSelected(Object object) {
        if (object instanceof User) {
            User user = (User) object;
            Settings.setCurrentUserStatic(user);
            Toast.makeText(this, "Выбран пользователь " + user.getDescription(), Toast.LENGTH_SHORT).show();
            if (!calledFromMenu) {
                Intent intent = new Intent(this, DocList_Activity.class);
                startActivity(intent);
            }
        }
        if (object instanceof VehicleType) {
            VehicleType vehicleType = (VehicleType) object;
            try {
                vehicleType.save();
                showMessage("Изменения вступят в силу после перезапуска программы");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (object instanceof String) {
            Settings.getSettings().setPrinterStatic((String) object);

            Toast.makeText(this, "Выбран принтер " + Settings.getPrinterStatic(), Toast.LENGTH_SHORT).show();
        }

    }

    private void showMessage(String s) {
        Snackbar.make(this.getWindow().getDecorView(), s, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
