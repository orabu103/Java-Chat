/**
 * This class represent Server
 * @author Tzvi Mints And Or Abuhazira
 */
package Client;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.*;

public class ServerThread implements Runnable {
	private Socket skt;
	private String username;
	private final LinkedList<String> msgLL;
	private boolean hasMsg = false;
	private ClientGUI gui;


	/* ************************** Setters and Getters ************************** */

	public ServerThread(Socket skt, String username) {
		this.skt = skt;
		this.username = username;
		msgLL = new LinkedList<String>();
	}
	/* ************************** Methods ************************** */

	public void addNextMessage(String message){
		synchronized (msgLL){
			hasMsg = true;
			msgLL.push(message);
		}
	}
	@Override
	public void run() {
		try {
		gui = new ClientGUI(username,""+skt.getLocalAddress(),""+skt.getRemoteSocketAddress(), skt);
			PrintWriter out = new PrintWriter(skt.getOutputStream(),false);
			InputStream in = skt.getInputStream();
			Scanner serverin = new Scanner(in); 
			while(skt.isConnected()){
				if(hasMsg)
				{
					String msgtodeliever = "";
					synchronized(msgLL){
						msgtodeliever = msgLL.pop();
						hasMsg = !msgLL.isEmpty();
					}
				}
				String msgtodeliever = serverin.nextLine();
				gui.setNewMsg(msgtodeliever);
			}
		}	  
		catch(Exception e)
		{
			System.err.println("Error! From ServerThread");
		}
	}
}