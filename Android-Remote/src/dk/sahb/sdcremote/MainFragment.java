package dk.sahb.sdcremote;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by sahb on 12/07/14.
 */
public class MainFragment extends Fragment {
    Button btnSend;
    Button btnCancel;
    Button btnBeep;
    Button btnOther;
    Button btnReboot;
    Button btnShutdown;

    EditText editText;

    MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mainfragment, container, false);

        // Find views
        btnSend = (Button) view.findViewById(R.id.btnSendMessage);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnBeep = (Button) view.findViewById(R.id.btnBeep);
        btnOther = (Button) view.findViewById(R.id.btnOther);
        btnReboot = (Button) view.findViewById(R.id.btnReboot);
        btnShutdown = (Button) view.findViewById(R.id.btnShutdown);

        editText = findView(view, R.id.editText);

        // Create click handler
        InitButtonListeners();

        return view;
    }

    public <T extends View> T findView(View view, int id) {
        return (T) view.findViewById(id);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Find mainfragment
        activity = (MainActivity) getActivity();
    }

    void InitButtonListeners() {
        // Create click handler
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!EditTextValid())
                {
                    Toast.makeText(getActivity(), "Skriv venligst en besked der skal sendes", Toast.LENGTH_LONG).show();
                    return;
                }

                activity.RunCommand("MessMassDo.sh '" + editText.getText() + "'");
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
            }
        });

        btnBeep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.showSoundsFragment();
            }
        });

        btnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.showOtherFragment();
            }
        });
    }

    boolean EditTextValid() {
        return editText.getText().length() != 0;
    }
}