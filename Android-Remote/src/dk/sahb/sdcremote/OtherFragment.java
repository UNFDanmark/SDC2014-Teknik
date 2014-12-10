package dk.sahb.sdcremote;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by sahb on 13/07/14.
 */
public class OtherFragment extends Fragment {
    Button btnTest;

    Button btnReboot;
    Button btnShutdown;

    MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.otherfragment, container, false);

        // Find views
        btnTest = (Button) view.findViewById(R.id.btnTest);
        btnReboot = (Button) view.findViewById(R.id.btnReboot);
        btnShutdown = (Button) view.findViewById(R.id.btnShutdown);

        // Init listeners
        InitButtonListeners();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Find mainActivity
        activity = (MainActivity) getActivity();
    }

    void InitButtonListeners() {
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.RunCommand("MassDo.sh 'pwd'");
            }
        });

        btnReboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialog("Genstart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        activity.RunCommand("SudoMassDo.sh 'reboot'");
                    }
                });
            }
        });

        btnShutdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialog("Sluk", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        activity.RunCommand("SudoMassDo.sh 'shutdown -h now'");
                    }
                });
            }
        });
    }

    public void ShowDialog(String title, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Add the buttons
        builder.setPositiveButton("Ok", okListener);
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        builder.setTitle(title);

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}