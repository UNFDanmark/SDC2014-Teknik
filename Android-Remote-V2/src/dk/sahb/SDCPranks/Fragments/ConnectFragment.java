package dk.sahb.SDCPranks.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import dk.sahb.SDCPranks.Activities.MainActivity;
import dk.sahb.SDCPranks.R;

/**
 * Created by sahb on 16/07/14.
 */
public class ConnectFragment extends Fragment implements View.OnClickListener
{
    // Views
    Button btnSelectClients;
    Button btnConnect;
    Button btnDisconnect;
    Button btnReset;

    EditText txtClients;

    // Activity
    MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.connectfragment, container, false);

        // Find views
        btnSelectClients = findView(R.id.btnSelectClients, view);
        btnConnect = findView(R.id.btnConnect, view);
        btnDisconnect = findView(R.id.btnDisconnect, view);
        btnReset = findView(R.id.btnReset, view);
        txtClients = findView(R.id.txtClients, view);

        // Init button listeners
        btnSelectClients.setOnClickListener(this);
        btnConnect.setOnClickListener(this);
        btnDisconnect.setOnClickListener(this);
        btnReset.setOnClickListener(this);

        // Find mainActivity
        mainActivity = (MainActivity) getActivity();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private <T> T findView(int resource, View parentView) {
        return (T) parentView.findViewById(resource);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConnect:
                if (txtClients.getText().toString().isEmpty()) {
                    Toast.makeText(mainActivity.getApplicationContext(), "Indtast venligst hvilke clienter du vil forbinde til", Toast.LENGTH_LONG);
                    return;
                }

                // Try to connect to server
                mainActivity.connect(txtClients.getText().toString());
                break;
            case R.id.btnDisconnect:
                mainActivity.disconnect();
                break;
            case R.id.btnReset:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // Add the buttons
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mainActivity.resetAuth();
                    }
                });
                builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

                builder.setTitle("Vil du nulstille indstillingerne?");

                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.btnSelectClients:
                Toast.makeText(getActivity(), "Endnu ikke implementeret", Toast.LENGTH_LONG).show();
                break;
        }
    }
}