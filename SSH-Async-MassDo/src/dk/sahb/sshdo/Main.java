package dk.sahb.sshdo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.sun.tools.example.debug.gui.Environment;

public class Main {

    public static void main(String[] args) {
        // Find args
        ArrayList<Integer> computers = new ArrayList<Integer>();
        for (int i = 0; i < args.length; i++) {
        	// Parse arg
        	if (args[i].contains("-")) {
        		int start = Integer.parseInt(args[i].split("-")[0]),
        			end = Integer.parseInt(args[i].split("-")[1]);
        		
        		for (int j = start; j <= end; j++) {
        			computers.add(j);
        		}
        	}
        	else {
        		computers.add(Integer.parseInt(args[i]));
        	}
        }
        
        // Add ssh classes and start connect process
        ArrayList<SSHClass> sshClasses = new ArrayList<SSHClass>();
        for (int i = 0; i < computers.size(); i++) {
        	SSHClass sshclient = new SSHClass("sdcadmin", "SdZ2014", 
					"sdc" + computers.get(i), 22);
        	
        	sshclient.Connect();
        	
        	sshClasses.add(sshclient);
        }
        
        // Wait for connect
        for (int i = 0; i < sshClasses.size(); i++) {
        	
        	while (!sshClasses.get(i).isConnected()){
        		try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		System.out.println("Venter p� forbindelse");
        	}
        }
        
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        System.out.println("Klar");
        
        Scanner s = new Scanner(System.in);
        while (true) {
        	String command = s.nextLine();
        	
        	if (command.equals("exit"))
        		break;
        	
        	for (int i = 0; i < sshClasses.size(); i++)
            {
                sshClasses.get(i).SendCommand(command);
            }
        }
        
        for (int i = 0; i < sshClasses.size(); i++)
        {
            sshClasses.get(i).Disconnect();
        }
    }
}
