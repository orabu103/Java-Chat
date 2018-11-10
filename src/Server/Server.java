/**
 * This class represent Server
 * @author Tzvi Mints And Or Abuhazira
 */
package Server;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Server {
	private static final int PORT = 9999; // Init the PORT for the server
	private static String whoIsHere = "";
	private static List<ClientThread> clients; // list of all clients 
	private static ServerSocket myServer;
	private static List<String> infoAboutClients; 

	/**
	 * This Method is Starting the Server, Init ServerSocket.
	 */
	public void startServer()
	{
		clients = new ArrayList<ClientThread>();
		try {
			myServer = new ServerSocket(PORT);
			acceptClients(myServer);
		}
		catch(Exception e)
		{
			System.err.println("Couldn't init the Server with "+PORT+" port");
		}
	}


	private void acceptClients(ServerSocket myServer) {
		while(true) {
			try {
				Socket skt = myServer.accept();
				
				// get nickname of newUser
			      String username = (new Scanner ( skt.getInputStream() )).nextLine();
			      username = username.replace(" ", "_");
			      System.out.println("New Client: \"" + username + "\"" + "\n"
			    		           + "Host:" + skt.getInetAddress().getHostAddress() + "\n"
			    		           + "***********************************" );

			    
				whoIsHere = skt.getInetAddress()+":"+skt.getPort() + "\n";
				ClientThread client = new ClientThread(this, skt);
				Thread thread = new Thread(client);
				thread.start();
				clients.add(client);
			} catch (Exception e){
				System.err.println("Couldn't make a Thread");
			}	
		}
	}

	/* ************************** Setters And Getters ************************** */
	public List<ClientThread> getClients()
	{
		return clients;
	}
	public Server(int PORT)
	{
	}
	/* ************************** Main ************************** */
	public static void main(String[] args) {
		JFrame frame = new JFrame("Start \"T&O\" Server");
		frame.setBounds(100,100,500,200);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
		ImageIcon icon = new ImageIcon("./img/icon.png"); // Set Icon to Chat
		frame.setIconImage(icon.getImage());

		// Adding Start Button
		JLabel start = new JLabel(new ImageIcon("./img/start.png"));
		start.setVisible(true);
		frame.add(start);
		start.setBounds(150, 40, 201, 49);

		// On Click "Start Server"
		start.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// Close the last frame	
				frame.setVisible(false);
				frame.dispose();

				Server server = new Server(PORT);
				server.startServer();
			}
		});
	}
}