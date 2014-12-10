package dk.sahb.sdcremote;

import android.util.Log;
import com.jcraft.jsch.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by sahb on 11/07/14.
 */
public class SSHManager extends Observable implements Observer
{
    String strUserName;
    String strPassword;
    String strConnectionIP;
    int intConnectionPort;

    Thread thread = null;
    ServerThread serverThread;
    String output = "";

    public SSHManager(String userName, String password, String connectionIP, int connectionPort) {
        strUserName = userName;
        strPassword = password;
        strConnectionIP = connectionIP;

        intConnectionPort = connectionPort;
    }

    public void connect() {
        serverThread = new ServerThread();
        serverThread.addObserver(this);

        thread = new Thread(serverThread);
        thread.start();
    }

    public boolean sendCommand(String command)
    {
        return serverThread.SendCommand(command);
    }

    public void close()
    {
        if (thread != null) {
            thread.interrupt();

            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public String getFilteredOutput() {
        return getOutput();
/*
        if (output.indexOf("start") == -1)
            return "";
        else {
            return output.substring(output.lastIndexOf("start") + "start".length());
        }*/
    }

    public String getOutput() {
        return output;
    }

    public void clearOutput() {
        output = "";
    }

    class ServerThread extends Observable implements Runnable {

        PrintStream shellStream;

        public boolean SendCommand(String command) {
            if (shellStream == null) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (shellStream == null)
                    return false;
            }

            shellStream.println(command);
            shellStream.flush();

            return true;
        }

        public void run() {
            try {
                JSch jschSSHChannel = new JSch();

                Session sesConnection = jschSSHChannel.getSession(strUserName,
                        strConnectionIP, intConnectionPort);

                sesConnection.setPassword(strPassword);
                sesConnection.setConfig("StrictHostKeyChecking", "no");
                sesConnection.connect(5000);

                Channel shellChannel = sesConnection.openChannel("shell");// only shell
                shellChannel.setOutputStream(System.out, true);
                //shellChannel.setInputStream(null);
                shellStream = new PrintStream(
                        shellChannel.getOutputStream(), true); // printStream for convenience

                shellChannel.connect();

                // Send echo off
                SendCommand("stty -echo");

                // Read output
                InputStream stdout = shellChannel.getInputStream();
                byte[] buffer = new byte[1024];
                while (!Thread.currentThread().isInterrupted()) {
                    while (stdout.available() > 0 && !Thread.currentThread().isInterrupted()) {
                        int i = stdout.read(buffer, 0, 1024);
                        if (i < 0)
                            break;

                        String out = new String(buffer, 0, i);
                        output += out;

                        // Notify observers
                        setChanged();
                        notifyObservers(out);
                    }
                }

                SendCommand("stty echo");

                shellStream.flush();
                shellChannel.disconnect();
                sesConnection.disconnect();
            }
            catch (JSchException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        // Notify observers
        setChanged();
        notifyObservers(o);
    }
}