package ru.sk42.tradeodata.Activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.sk42.tradeodata.Model.Catalogs.HelperLists.UsersList;
import ru.sk42.tradeodata.Model.Printers;
import ru.sk42.tradeodata.Model.SettingsOld;
import ru.sk42.tradeodata.R;
import ru.sk42.tradeodata.RetroRequests.RetroConstants;
import ru.sk42.tradeodata.RetroRequests.ServiceGenerator;
import ru.sk42.tradeodata.RetroRequests.UsersRequest;



/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: SettingsOld</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">SettingsOld
 * API Guide</a> for more information on developing a SettingsOld UI.
 */
public class SettingsActivity extends PreferenceActivity {

    static final String TAG = "SETTINGS";
    public static Printers printers;
    public static UsersList usersList;
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * MyHelper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (usersList == null)
            usersList = new UsersList();
        if (printers == null)
            printers = new Printers();

        //loadFromNetwork();
    }

    @Override
    protected void onStop() {
        super.onStop();

        SettingsOld.readSettings();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                // NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);

    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || UserPreferenceFragment.class.getName().equals(fragmentName)
                || PrinterPreferenceFragment.class.getName().equals(fragmentName)
                || ConnectionPreferenceFragment.class.getName().equals(fragmentName);
    }



    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class UserPreferenceFragment extends PreferenceFragment {

        void setUsersPreferenceList(){
            final CharSequence[] cs = UsersList.getValuesAsCharSequence();
            Preference p = findPreference("currentUserKey");
            ListPreference lp = (ListPreference) p;
            lp.setEntries(cs);
            lp.setEntryValues(cs);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_user);
            setHasOptionsMenu(true);


            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.

            CharSequence[] charSequences = UsersList.getValuesAsCharSequence();
            if(charSequences.length == 0) {
                UsersRequest request = ServiceGenerator.createService(UsersRequest.class);
                Call<UsersList> call = request.call(RetroConstants.getMap(RetroConstants.FILTERS.USERS));
                call.enqueue(new Callback<UsersList>() {
                    @Override
                    public void onResponse(Call<UsersList> call, Response<UsersList> response) {
                        UsersList list = response.body();
                        list.save();
                        setUsersPreferenceList();
                    }

                    @Override
                    public void onFailure(Call<UsersList> call, Throwable t) {
                        Log.d(TAG, "onFailure: " +  t.toString());
                    }

                });
            }
            else {
                setUsersPreferenceList();
            }

            bindPreferenceSummaryToValue(findPreference("currentUserKey"));

        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    public static class ConnectionPreferenceFragment extends PreferenceFragment {
        public void testConnection() {
            Thread thread1 = new Thread();
            thread1.start();
            try {
                thread1.join();
            } catch (InterruptedException e) {
                Log.e(this.getClass().getName(), e.getMessage());
            }

        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_connection);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("ServerAddress"));
            bindPreferenceSummaryToValue(findPreference("ServerUser"));
            bindPreferenceSummaryToValue(findPreference("InfoBaseName"));

        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    public static class PrinterPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_printer);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            Preference p = findPreference("Printer");
            ListPreference lp = (ListPreference) p;

            lp.setEntryValues(printers.getCharSequence());
            lp.setEntries(printers.getCharSequence());
            bindPreferenceSummaryToValue(findPreference("Printer"));
        }

        @Override
        public void onStart() {
            super.onStart();

        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }


}
