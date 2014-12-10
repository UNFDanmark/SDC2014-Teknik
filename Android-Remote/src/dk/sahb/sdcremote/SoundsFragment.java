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
 * Created by sahb on 11/07/14.
 */
public class SoundsFragment extends Fragment {
    Button btnInitBeep;
    Button btnStarwars;
    Button btnAxelFoley;
    Button btnFinalCountDown;
    Button btnTetris;
    Button btnAerodynamic;
    Button btnAlarm;
    Button btnFurLise;
    Button btnPhaser;
    Button btnRing;
    Button btnAwesome;
    Button btnGameOfThrones;
    Button btnLetItGo;
    Button btnNyan;
    Button btnPoke;

    Button btnEject;
    Button btnSl;

    EditText txtComputer;

    MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.soundfragment, container, false);

        // Find views
        btnInitBeep = (Button) view.findViewById(R.id.btnInitBeep);
        btnStarwars = (Button) view.findViewById(R.id.btnStarwars);
        btnAxelFoley = (Button) view.findViewById(R.id.btnAxelFoley);
        btnFinalCountDown = (Button) view.findViewById(R.id.btnFinalCountDown);
        btnTetris = (Button) view.findViewById(R.id.btnTetris);
        btnAerodynamic = (Button) view.findViewById(R.id.btnAerodynamic);
        btnAlarm = (Button) view.findViewById(R.id.btnAlarm);
        btnFurLise = (Button) view.findViewById(R.id.btnFurLise);
        btnPhaser = (Button) view.findViewById(R.id.btnPhaser);
        btnRing = (Button) view.findViewById(R.id.btnRing);
        btnAwesome = (Button) view.findViewById(R.id.btnAwesome);
        btnGameOfThrones = (Button) view.findViewById(R.id.btnGameOfThrones);
        btnLetItGo = (Button) view.findViewById(R.id.btnLetItGo);
        btnNyan = (Button) view.findViewById(R.id.btnNyan);
        btnPoke = (Button) view.findViewById(R.id.btnPoke);

        btnEject = (Button) view.findViewById(R.id.btnEject);
        btnSl = (Button) view.findViewById(R.id.btnSl);

        txtComputer = (EditText) view.findViewById(R.id.txtComputer);

        // Bind click handlers
        InitButtonListeners();

        // Find mainfragment
        activity = (MainActivity) getActivity();

        return view;
    }

    void InitButtonListeners() {
        btnInitBeep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.RunCommand("MassDo.sh 'beep'");
            }
        });

        btnStarwars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartSong("StarWars");
            }
        });

        btnAxelFoley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartSong("AxelFoley");
            }
        });

        btnFinalCountDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartSong("FinalCountDown");
            }
        });

        btnTetris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartSong("Tetris");
            }
        });

        btnAerodynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartSong("Aerodynamic");
            }
        });

        btnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartSong("Alarm");
            }
        });

        btnFurLise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartSong("FurLise");
            }
        });

        btnPhaser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartSong("Phaser");
            }
        });

        btnRing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartSong("Ring");
            }
        });

        btnAwesome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartSong("Awesome");
            }
        });

        btnGameOfThrones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartSong("gott");
            }
        });

        btnLetItGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartSong("Letitgo");
            }
        });

        btnNyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartSong("Nyan");
            }
        });

        btnPoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartSong("poke");
            }
        });

        btnSl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ValidateComputer())
                    return;

                activity.RunCommand("Do.sh sdc" + txtComputer.getText() + " \"echo 'SdZ2014' | sudo -H -u sdc -S DISPLAY=:0 gnome-terminal --command /usr/games/sl\"");
            }
        });

        btnEject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ValidateComputer())
                    return;

                activity.RunCommand("Do.sh sdc" + txtComputer.getText() + " 'eject'");
            }
        });
    }

    void StartSong(String song) {
        if (!ValidateComputer())
            return;

        //activity.RunCommand("Do.sh sdc" + txtComputer.getText() + " 'beep'");
        activity.RunCommand("FileDo.sh sdc" + txtComputer.getText() + " Songs/" + song);
    }

    boolean ValidateComputer() {
        int computerName = GetComputerNumber();
        boolean result = computerName > 0 && computerName < 17;

        if (!result) {
            Toast.makeText(getActivity(), "Indtast venligst et gyldigt computer nummer", Toast.LENGTH_LONG).show();
        }

        return result;
    }

    int GetComputerNumber() {
        try {
            return Integer.parseInt(txtComputer.getText().toString());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
}