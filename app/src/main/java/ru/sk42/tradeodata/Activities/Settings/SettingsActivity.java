package ru.sk42.tradeodata.Activities.Settings;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.sk42.tradeodata.Activities.Document.Adapters.DrawerAdapter;
import ru.sk42.tradeodata.Activities.Documents_List.DocList_Activity;
import ru.sk42.tradeodata.Model.Catalogs.User;
import ru.sk42.tradeodata.Model.Catalogs.VehicleType;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.R;


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
public class SettingsActivity extends AppCompatActivity implements SettingsInterface {

    static final String TAG = "SETTINGS";

    Activity mActivity;

    Toolbar myToolbar;

    ActionBarDrawerToggle mDrawerToggle;

    @Bind(R.id.settings_listview)
    ListView mDrawerList;

    @Bind(R.id.settings_drawer_layout)
    DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings__activity);
        ButterKnife.bind(this, this);

        mActivity = this;

        int[] colors = {0, 0xFFFF0000, 0}; // red for the example
        mDrawerList.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        mDrawerList.setDividerHeight(2);

        mDrawerList.setAdapter(new DrawerAdapter<String>(this,
                R.layout.drawer_list_item_layout, Constants.SETTINGS_ACTIONS));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onListItemSelected(i);
            }
        });

        mDrawerList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(SettingsActivity.this, "sdfsdfsdf", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
//                Toast.makeText(SettingsActivity.this, "sdfsdfsdf", Toast.LENGTH_SHORT).show();

            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                null,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                mDrawerList.bringToFront();
                mDrawerLayout.requestLayout();
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        //mDrawerLayout.openDrawer(Gravity.LEFT);

        myToolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        this.setTitle("");
        setSettingsTitle("Настройки", "");
        if (getIntent().getIntExtra(Constants.REQUEST_SETTINGS_USER_LABEL, 0) == 0) {
            showConnectionFragment();
        } else {
            showUserSelectionFragment();
        }
    }

    public void setSettingsTitle(String t, String st) {
        myToolbar.setSubtitle("");
        myToolbar.setTitle("");
        TextView tv = (TextView) myToolbar.findViewById(R.id.settings__title);
        tv.setText(t);
        tv = (TextView) myToolbar.findViewById(R.id.settings__subtitle);
        tv.setText(st);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method isEmpty
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
            case R.id.action_view_product_list:
                //showProductsListFragment();
                break;
            case R.id.action_scanner_pair:
                //pairScaner();
                break;
            case R.id.action_scanner_set:
                //setScaner();
                break;
            case R.id.action_scanner_connect:
                //connectScaner();
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    private void onListItemSelected(int pos) {
        mDrawerLayout.closeDrawer(Gravity.LEFT);
        switch (pos) {
            case 0:
                //connection settings
                showConnectionFragment();
                break;
            case 1:
                //mVehicleType select
                showUserSelectionFragment();
                break;
            case 2:
                //printer
                PrinterFragment printerFragment = PrinterFragment.newInstance(1);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings_frame, printerFragment, printerFragment.getClass().getName())
                        .addToBackStack(printerFragment.getClass().getName())
                        .commit();
                break;
            case 3:
                ScannerFragment fragment = ScannerFragment.newInstance();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings_frame, fragment, fragment.getClass().getName())
                        //  .addToBackStack(fragment.getClass().getName())
                        .commit();
                //scanner
                break;
            case 4:
                VehicleTypesFragment vehicleTypesFragment = VehicleTypesFragment.newInstance(1);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings_frame, vehicleTypesFragment, vehicleTypesFragment.getClass().getName())
                        .addToBackStack(vehicleTypesFragment.getClass().getName())
                        .commit();
                break;
        }
    }

    private void showUserSelectionFragment() {
        UserSelectionFragment userSelectionFragment = UserSelectionFragment.newInstance(1);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_frame, userSelectionFragment, userSelectionFragment.getClass().getName())
                .addToBackStack(userSelectionFragment.getClass().getName())
                .commit();
    }

    private void showConnectionFragment() {
        ConnectionFragment connectionFragment = ConnectionFragment.newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_frame, connectionFragment, connectionFragment.getClass().getName())
                .addToBackStack(connectionFragment.getClass().getName())
                .commit();

    }

    @Override
    public void onValueSelected(Object object) {
        if (object instanceof User) {
            User user = (User) object;
            Settings.setCurrentUserStatic(user);
            Toast.makeText(this, "Выбран пользователь " + user.getDescription(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, DocList_Activity.class);
            startActivity(intent);
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
