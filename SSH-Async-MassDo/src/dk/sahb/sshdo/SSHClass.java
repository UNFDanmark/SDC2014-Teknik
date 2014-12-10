package dk.sahb.sshdo;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.awt.dnd.InvalidDnDOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Observable;

/**
 * Created by sahb on 14/07/14.
 */
public class SSHClass extends Observable {
    private String username;
    private String password;
    private String ip;
    private int port;

    private String output;

    private ServerThread server;
    private Thread thread;

    public SSHClass(String username, String password, String ip, int port) {
        this.username = username;
        this.password = password;
        this.ip = ip;
        this.port = port;
    }

    public void Connect() {
        server = new ServerThread();

        thread = new Thread(server);
        thread.start();
    }

    public void Disconnect() {
        if (server != null) {
            thread.interrupt();
            server = null;
            thread = null;
        }
    }

    public void SendCommand(String command) {
        if (server != null)
            server.sendCommand(command);
        else
            throw new InvalidDnDOperationException();
    }

    public boolean isConnected() {
        return !(server == null || server.shellStream == null);
    }

    public String getOutput() {
        return output;
    }

    private class ServerThread implements Runnable {

        public PrintStream shellStream;

        public void sendCommand(String command) {
            shellStream.println(command);
            shellStream.flush();
        }

        @Override
        public void run() {
            try {
                JSch jschSSHChannel = new JSch();

                Session sesConnection = jschSSHChannel.getSession(username, ip, port);

                sesConnection.setPassword(password);
                sesConnection.setConfig("StrictHostKeyChecking", "no");
                sesConnection.connect();

                Channel shellChannel = sesConnection.openChannel("shell");// only shell
                shellChannel.setOutputStream(System.out);
                shellChannel.setInputStream(null);
                shellStream = new PrintStream(
                        shellChannel.getOutputStream()); // printStream for convenience

                shellChannel.connect();

                // Read output
                while (!Thread.currentThread().isInterrupted()) {
                    try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }

                shellStream.flush();
                shellChannel.disconnect();
                sesConnection.disconnect();
            }
            catch (JSchException e) {

            }
            catch (IOException e) {

            }
        }
    }

}
