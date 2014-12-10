package dk.sahb.SDCPranks.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import dk.sahb.SDCPranks.Activities.MainActivity;
import dk.sahb.SDCPranks.R;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by sahb on 16/07/14.
 */
public class StatusFragment extends Fragment implements Observer {
    // Views
    TextView txtStatus;

    // Actitity
    MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.statusfragment, container, false);

        // Find views
        txtStatus = (TextView) view.findViewById(R.id.txtStatus);

        // Find actitity
        mainActivity = (MainActivity) getActivity();

        // Update txt
        txtStatus.setText(mainActivity.getSSHOutput());

        // Add to observable
        mainActivity.registrerObserver(this);

        return view;
    }

    @Override
    public void update(Observable observable, Object o) {
        final String txt = (String) o;

        txtStatus.post(new Runnable() {
            @Override
            public void run() {
                txtStatus.append(txt);
            }
        });

    }
}