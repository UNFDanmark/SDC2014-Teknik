package dk.sahb.sdcremote;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * Created by sahb on 10/07/14.
 */
public class MainActivity extends Activity {
    static String username;
    static String password;
    static String ip;

    final String ipKey = "ip";
    final String usernameKey = "Username";
    final String passwordKey = "Password";

    final int AUTH_USER_REQUEST = 1;

    SharedPreferences prefs;

    SSHManager sshManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);

        // Load shared preferences
        //prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs = getSharedPreferences("dk.sahb.SDCRemote", Context.MODE_PRIVATE);

        // Load settings from shared prefs
        LoadDataFromPrefs();

        if (ip.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, AUTH_USER_REQUEST);
        }

        // Load main fragment
        showMainFragment();
    }

    private void showMainFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        MainFragment fragment = new MainFragment();
        fragmentTransaction.add(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    public void showSoundsFragment() {
        showFragment(new SoundsFragment());
    }

    public void showOtherFragment() {
        showFragment(new OtherFragment());
    }

    private void showFragment(Fragment fragment) {
        // Create an transaction
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ip.isEmpty() || username.isEmpty() || password.isEmpty()) {
            return;
        }

        // Connect to server
        if (sshManager != null)
            try {
                sshManager.close();
            } catch (Exception e) {}

        sshManager = new SSHManager(username, password, ip, 512);
        sshManager.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Disconnect to server
        if (sshManager != null)
            sshManager.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTH_USER_REQUEST) {
            if (resultCode == RESULT_OK) {
                // Read settings
                String ip = data.getStringExtra(LoginActivity.TAG_IP);
                String username = data.getStringExtra(LoginActivity.TAG_USERNAME);
                String password = data.getStringExtra(LoginActivity.TAG_PASSWORD);

                // Save settings
                SaveDataToPrefs(ip, username, password);

                // Reload settings
                LoadDataFromPrefs();
            }
        }
    }

    void SaveDataToPrefs(String ip, String username, String password) {
        if (prefs == null)
            prefs = getSharedPreferences("dk.sahb.SDCRemote", Context.MODE_PRIVATE);

        // Save ip, username and password to shared prefs
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(ipKey, ip);
        editor.putString(usernameKey, username);
        editor.putString(passwordKey, password);

        editor.apply();
    }

    void LoadDataFromPrefs() {
        // Load ip, username and password from shared prefs
        ip = prefs.getString(ipKey, "");
        username = prefs.getString(usernameKey, "");
        password = prefs.getString(passwordKey, "");
    }

    public void RunCommand(String command) {
        RunProgram("bash " + command);
    }

    public void RunProgram(String program) {
        sshManager.clearOutput();

        // Show status activity
        // Create new fragment and transaction
        StatusFragment newFragment = new StatusFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

        // Bind fragment to observer and delete old observers
        sshManager.deleteObservers();
        sshManager.addObserver(newFragment);

        if (sshManager.sendCommand(program))
            Toast.makeText(this, "Kommandoen blev med succes sendt", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Der opstod en fejl under afsendelsen af kommandoen", Toast.LENGTH_LONG).show();
    }

    public String getSSHOutput() {
        return sshManager.getFilteredOutput();
    }
}