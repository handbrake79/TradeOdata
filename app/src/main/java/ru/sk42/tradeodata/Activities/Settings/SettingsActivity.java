package ru.sk42.tradeodata.Activities.Settings;

import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.sk42.tradeodata.Activities.Document.Adapters.DrawerAdapter;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.UsersList;
import ru.sk42.tradeodata.Model.Catalogs.User;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.Printers;
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
public class SettingsActivity extends AppCompatActivity implements UserListFragment.OnListFragmentInteractionListener {

    static final String TAG = "SETTINGS";
    public static Printers printers;
    public static UsersList usersList;

    Toolbar myToolbar;

    ActionBarDrawerToggle mDrawerToggle;


    @Bind(R.id.settings_listview)
    ListView mDrawerList;

    @Bind(R.id.settings_drawer_layout)
    DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        ButterKnife.bind(this, this);
        int[] colors = {0, 0xFFFF0000, 0}; // red for the example
        mDrawerList.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        mDrawerList.setDividerHeight(2);

        mDrawerList.setAdapter(new DrawerAdapter<String>(this,
                R.layout.list_item_layout, Constants.SETTINGS_ACTIONS));
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
        showConnectionFragment();
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
                //mUser select
                UserListFragment userListFragment = UserListFragment.newInstance(1);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings_frame, userListFragment, userListFragment.getClass().getName())
                        .addToBackStack(userListFragment.getClass().getName())
                        .commit();

                break;
            case 2:
                //printer
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
                break;
        }
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
    public void onListFragmentInteraction(User user) {
        Settings.setCurrentUserStatic(user);
        Toast.makeText(this, "Выбран пользователь " + user.getDescription(), Toast.LENGTH_SHORT).show();

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
