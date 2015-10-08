
import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.*;
import java.io.*;
import java.util.*;


///////////////////////////// Mutlithreaded Server /////////////////////////////
///////////////////////////// Modified from given PetServer implementation /////

public class ScoresServer
{
   final static int port = 51993;

   public static void main(String[] args )
   {  
      try
      {  
         int i = 1;
         ServerSocket s = new ServerSocket(port);
	 while (true)
         {  
            Socket incoming = s.accept();
//            System.out.println("Spawning " + i);
            Runnable r = new ThreadedHandler(incoming);
            Thread t = new Thread(r);
            t.start();
            i++;
         }
      }
      catch (IOException e)
      {  
         e.printStackTrace();
      }
   }
}

/**
   This class handles the client input for one server socket connection. 
*/
class ThreadedHandler implements Runnable
{ 

   public ThreadedHandler(Socket i)
   { 
      incoming = i; 
   }

   public static Connection getConnection() throws SQLException, IOException
   {
      Properties props = new Properties();
      FileInputStream in = new FileInputStream("database.properties");
      props.load(in);
      in.close();
      String drivers = props.getProperty("jdbc.drivers");
      if (drivers != null)
        System.setProperty("jdbc.drivers", drivers);
      String url = props.getProperty("jdbc.url");
      String username = props.getProperty("jdbc.username");
      String password = props.getProperty("jdbc.password");

      return DriverManager.getConnection( url, username, password);
   }


   void getHiscores( String [] args, PrintWriter out) {
      Connection conn=null;
      try
      {
	conn = getConnection();
        Statement stat = conn.createStatement();
	
	ResultSet result = stat.executeQuery( "SELECT * FROM scores WHERE rank <= " + args[1] + ";");

	while(result.next()) {
       		out.print(result.getString(1)+" | ");
       		out.print(result.getString(2)+" | ");
       		out.print(result.getString(3)+" | ");
       		out.print(result.getString(4)+" | ");
       		out.print(result.getString(5));
		out.println("");
	}

	result.close();

      }
      catch (Exception e) {
	System.out.println(e.toString());
	out.println(e.toString());
      }
      finally
      {
	try {
         if (conn!=null) conn.close();
	}
	catch (Exception e) {
	}
      }

   }

   void insertScore(String[] args, PrintWriter out){
      Connection conn=null;
      try
      {
	conn = getConnection();
        Statement stat = conn.createStatement();
	stat.executeUpdate("UPDATE scores SET rank = rank+1 WHERE rank >= " + args[1] + ";");
	stat.executeUpdate("INSERT INTO scores VALUES('" + args[1] + "','" + args[2] + "','" + args[3] + "'," + args[4] + "," + args[5] + ");");
      }
      catch (Exception e) {
	System.out.println(e.toString());
	out.println(e.toString());
      }
      finally
      {
	try {
         if (conn!=null) conn.close();
	}
	catch (Exception e) {
	}
      }

   }

   void findCount(String[] args, PrintWriter out){
      Connection conn=null;
      try
      {
	conn = getConnection();
        Statement stat = conn.createStatement();
	ResultSet count;
	count = stat.executeQuery( "SELECT COUNT(*) FROM scores WHERE score > " + args[2] + ";");
	if(count.next()){
	    out.println(count.getString(1));
	}


	count.close();

      }
      catch (Exception e) {
	System.out.println(e.toString());
	out.println(e.toString());
      }
      finally
      {
	try {
         if (conn!=null) conn.close();
	}
	catch (Exception e) {
	}
      }

   }


   void findScore(String[] args, PrintWriter out){
      Connection conn=null;
      try
      {
	conn = getConnection();
        Statement stat = conn.createStatement();
	ResultSet count;
	count = stat.executeQuery( "SELECT COUNT(*) FROM scores WHERE score < " + args[2] + ";");
	if(count.next()){
	    out.println(count.getString(1));
	}


	ResultSet result = stat.executeQuery( "SELECT * FROM scores WHERE score < " + args[2] + ";");

	while(result.next()) {
       		out.print(result.getString(1)+"|");
       		out.print(result.getString(2)+"|");
       		out.print(result.getString(3)+"|");
       		out.print(result.getString(4)+"|");
       		out.print(result.getString(5));
		out.println("");
	}

	count.close();
	result.close();

      }
      catch (Exception e) {
	System.out.println(e.toString());
	out.println(e.toString());
      }
      finally
      {
	try {
         if (conn!=null) conn.close();
	}
	catch (Exception e) {
	}
      }


   }

   void handleRequest( InputStream inStream, OutputStream outStream) {
        Scanner in = new Scanner(inStream);         
        PrintWriter out = new PrintWriter(outStream, 
                                      true /* autoFlush */);

	// Get parameters of the call
	String request = in.nextLine();
	String requestSyntax = "Syntax: GET SCORES  or  Syntax: NEWSCORE <score>";

	try {
		// Get arguments.
		// The format is COMMAND|USER|PASSWORD|OTHER|ARGS...
		String [] args = request.split(" ");

		// Print arguments
	/*	for (int i = 0; i < args.length; i++) {
			System.out.println("Arg "+i+": "+args[i]);
		} */

		// Get command and password
		String command = args[0];
		//String user = args[1];
		//String password = args[2];

		// Check user and password. Now it is sent in plain text.
		// You should use Secure Sockets (SSL) for a production environment.
		//
		// Server name and password are not required in our app
		//
		/*if ( !user.equals(ServerUser) || !password.equals(ServerPassword)) {
			System.out.println("Bad user or password");
			out.println("Bad user or password");
			return;
		}*/

		// Do the operation
		if (command.equals("GETSCORES")) {
			getHiscores(args, out);
		} else if (command.equals("NEWSCORE")) {
			insertScore(args, out); 
		} else if (command.equals("FIND")) {
			if(Integer.parseInt(args[2]) == -1){
				findCount(args, out);
			} else {
				findScore(args, out);
			}
		} else {
			out.println("Bad command");
		}
		
	}
	catch (Exception e) {		
		System.out.println(requestSyntax);
		out.println(requestSyntax);

		System.out.println(e.toString());
		out.println(e.toString());
	}
   }

   public void run() {  
      try
      {  
         try
         {
	    InputStream inStream = incoming.getInputStream();
            OutputStream outStream = incoming.getOutputStream();
	    handleRequest(inStream, outStream);

         }
      	 catch (IOException e)
         {  
            e.printStackTrace();
         }
         finally
         {
            incoming.close();
         }
      }
      catch (IOException e)
      {  
         e.printStackTrace();
      }
   }

   private Socket incoming;
}

