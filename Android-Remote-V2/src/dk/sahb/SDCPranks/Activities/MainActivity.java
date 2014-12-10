package dk.sahb.SDCPranks.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.Toast;
import dk.sahb.SDCPranks.Fragments.ConnectFragment;
import dk.sahb.SDCPranks.Fragments.PranksFragment;
import dk.sahb.SDCPranks.Fragments.StatusFragment;
import dk.sahb.SDCPranks.R;
import dk.sahb.SDCPranks.SSHManager;

import java.util.ArrayList;
import java.util.Observer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends FragmentActivity {
    private SSHManager sshManager;

    private String username;
    private String password;
    private String ip;

    private int port = 512;

    private final String PREFS_IP_KEY = "ip";
    private final String PREFS_USERNAME_KEY = "Username";
    private final String PREFS_PASSWORD_KEY = "Password";
    private final String PREFS_PORT_KEY = "Port";

    private final int AUTH_USER_REQUEST = 1;

    SharedPreferences prefs;

    ArrayList<Observer> observers = new ArrayList<Observer>();

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(
                getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Find shared preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // Load settings from shared prefs
        loadData();

        if (ip.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, AUTH_USER_REQUEST);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Set current page to first
        mViewPager.setCurrentItem(0, true);
    }

    @Override
    protected void onPause() {
        super.onPause();

        disconnect();
    }

    public String getSSHOutput() {
        return sshManager.getOutput();
    }

    public void connect(String clients) {
        // Check if a connection is already open
        if (sshManager != null) {
            Toast.makeText(this, "Der er allerede en åben forbindelse - luk denne først!", Toast.LENGTH_LONG).show();
            return;
        }

        // Check if clients is in a valid format
        String regx = "[^0-9\\- ]";
        Pattern pattern = Pattern.compile(regx,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(clients);
        if (matcher.find()){
            Toast.makeText(this, "Klient numrene er ikke gyldige", Toast.LENGTH_LONG).show();
            return;
        }

        // Add ssh manager and try to connect
        sshManager = new SSHManager(username, password, ip, port);

        // Add observers
        for (Observer observer : observers) {
            sshManager.addObserver(observer);
        }

        // Try to connect
        sshManager.connect();

        // Notify user
        Toast.makeText(this, "Opretter forbindelse til serveren", Toast.LENGTH_LONG);

        // Add thread to notify when server is active
        // Save client to final to make a closure
        final String clientsToConnect = clients;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final int maxTime = 10000;
                final int sleepTime = 1000;

                int currentTime = 0;

                while (!Thread.currentThread().isInterrupted() && !sshManager.isConnected() && currentTime < maxTime) {
                    // SSH manager is not yet connected - try to connect
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finally {
                        currentTime += sleepTime;
                    }
                }

                // Send command to communicate with recievers
                if (sshManager.isConnected()) {
                    // Connect to clients
                    sendCommand("echo starter");
                    sendCommand("java -cp .:ssh.jar dk.sahb.sshdo.Main " + clientsToConnect);
                }

                // Notify changes to page adapter
                mSectionsPagerAdapter.notifyDataSetChanged();

                // Notify user about the process
                Toast.makeText(getApplicationContext(),
                        sshManager.isConnected() ?
                                "Der blev succesfuldt oprettet forbindelse til serveren" :
                                "Der opstod en fejl under oprettelse af forbindelsen til serveren",
                        Toast.LENGTH_LONG).show();

                // Disconnect if not connected
                if (!sshManager.isConnected())
                    disconnect();

            }
        });
        thread.run();
    }

    public void disconnect() {
        // Close ssh manager
        if (sshManager != null) {
            sshManager.sendCommand("exit");

            sshManager.close();
            sshManager = null;

            // Notify changes to page adapter
            mSectionsPagerAdapter.notifyDataSetChanged();
        }
    }

    public void sendCommand(String command) {
        if (sshManager != null &&
                sshManager.isConnected() &&
                sshManager.sendCommand(command)) {
            Toast.makeText(getApplicationContext(), "Kommandoen blev afsendt", Toast.LENGTH_LONG);
        }
        else {
            Toast.makeText(getApplicationContext(), "Der opstod en fejl under afsendelsen af kommandoen", Toast.LENGTH_LONG);
        }
    }

    public void registrerObserver(Observer observer) {
        observers.add(observer);

        if (sshManager != null)
            sshManager.addObserver(observer);
    }

    public void resetAuth() {
        // Set data to null
        username = "";
        password = "";
        ip = "";

        // Save data
        saveData();

        // Restart activity
        recreate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTH_USER_REQUEST) {
            if (resultCode == RESULT_OK) {
                // Read settings
                ip = data.getStringExtra(LoginActivity.TAG_IP);
                username = data.getStringExtra(LoginActivity.TAG_USERNAME);
                password = data.getStringExtra(LoginActivity.TAG_PASSWORD);

                // Save settings
                saveData();
            }
        }
    }

    void saveData() {
        saveData(ip, username, password);
    }

    void saveData(String ip, String username, String password) {
        if (prefs == null)
            prefs = getSharedPreferences("dk.sahb.SDCRemote", Context.MODE_PRIVATE);

        // Save ip, username and password to shared prefs
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(PREFS_IP_KEY, ip);
        editor.putString(PREFS_USERNAME_KEY, username);
        editor.putString(PREFS_PASSWORD_KEY, password);

        editor.apply();
    }

    void loadData() {
        // Load ip, username and password from shared prefs
        ip = prefs.getString(PREFS_IP_KEY, "");
        username = prefs.getString(PREFS_USERNAME_KEY, "");
        password = prefs.getString(PREFS_PASSWORD_KEY, "");
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            Fragment fragment;
            switch (position) {
                case 0:
                    fragment = new ConnectFragment();
                    break;
                case 1:
                    fragment = new PranksFragment();
                    break;
                case 2:
                    fragment = new StatusFragment();
                    break;
                default:
                    return null;
            }

            fragment.setArguments(new Bundle());
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return
                    sshManager == null ||
                    !(sshManager.isConnected()) ?
                            1 : 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Connect";
                case 1:
                    return "Pranks";
                case 2:
                    return "Status";
            }
            return null;
        }
    }
}
