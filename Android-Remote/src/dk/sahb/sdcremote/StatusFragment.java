package dk.sahb.sdcremote;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by sahb on 13/07/14.
 */
public class StatusFragment extends Fragment implements Observer {
    TextView textView;

    MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statusfragment, container, false);

        textView = (TextView) view.findViewById(R.id.textView);
        updateTextView();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Find mainfragment
        activity = (MainActivity) getActivity();
    }

    @Override
    public void update(Observable observable, Object o) {
        updateTextView();
    }

    private void updateTextView() {
        if (textView != null)
            textView.post(new Runnable() {
                @Override
                public void run() {
                    textView.setText(activity.getSSHOutput().replace("\r", ""));
                }
            });
    }
}