package dk.sahb.sdcremote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
    Button btnSubmit;
    EditText txtIp;
    EditText txtUsername;
    EditText txtPassword;

    public static final String TAG_USERNAME = "Username";
    public static final String TAG_PASSWORD = "Password";
    public static final String TAG_IP = "IP";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity);

        // Find views
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtIp = (EditText) findViewById(R.id.txtIp);

        btnSubmit = (Button) findViewById(R.id.btnLogin);

        // Set button listener
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                intent.putExtra(TAG_USERNAME, txtUsername.getText().toString());
                intent.putExtra(TAG_PASSWORD, txtPassword.getText().toString());
                intent.putExtra(TAG_IP, txtIp.getText().toString());

                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
