package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import server.networkServer.ClientConnector;
import shared.data.User;
import shared.exceptions.resourcesLibraryExceptions.LackOfFileException;
import shared.resourcesLibrary.AbstractResourcesLibrary;
import shared.resourcesLibrary.SerializationLibrary;

public class Server {

  private static HashMap<String, Integer> clients = new HashMap<String, Integer>( );
  private static HashMap<Integer, UserThread> threads = new HashMap<Integer, UserThread>( );
  private static List<User> usersAll = new ArrayList<User>( );
  private static CentralBank centralBank = new CentralBank( 100000 );
  private static Bandit bandit = new Bandit( );
  private ClientConnector clientConnector = new ClientConnector( );
  private AbstractResourcesLibrary resource = new SerializationLibrary( "Server" );

  public Server( ) {
    Server.usersAll = this.loadUserList( );
    Server.centralBank = this.loadCentralBank();
    Server.bandit = this.loadBandit( );
    createAdmin( );
    this.clientConnector.listenForClients( );
  }

  public CentralBank loadCentralBank( ) {
    CentralBank centralBank = new CentralBank( );
    try {
      System.out.println( "Reading central bank" );
      List<Object> objects = this.resource.getObjects( "server:centralBank" );
      centralBank = (CentralBank) objects.get( 0 );
    } catch ( LackOfFileException e ) {
      System.out.println( "Creating empty file for central Bank" );
      centralBank = new CentralBank( 100000 );
      this.resource.saveObject( centralBank, "server:centralBank" );
    }
    return centralBank;
  } 
  
  public Bandit loadBandit( ) {
	  Bandit bandit = new Bandit( );
	    try {
	      System.out.println( "Reading bandit" );
	      List<Object> objects = this.resource.getObjects( "server:bandit" );
	      bandit = (Bandit) objects.get( 0 );
	    } catch ( LackOfFileException e ) {
	      System.out.println( "Creating empty file for bandit" );
	      bandit = new Bandit(  );
	      this.resource.saveObject( bandit, "server:bandit" );
	    }
	    return bandit;
	  }
  
  public static CentralBank getCentralBank() {
	return Server.centralBank;
}

public static void setCentralBank(CentralBank centralBank) {
	Server.centralBank = centralBank;
}

public static Bandit getBandit() {
	return Server.bandit;
}

public static void setBandit(Bandit bandit) {
	Server.bandit = bandit;
}

@SuppressWarnings( "unchecked" )
  public List<User> loadUserList( ) {
    List<User> users = new ArrayList<User>( );
    try {
      System.out.println( "Reading users from file" );
      List<Object> objects = this.resource.getObjects( "server:users" );
      users = (List<User>) objects.get( 0 );
    } catch ( LackOfFileException e ) {
      System.out.println( "Creating empty file for users" );
      this.resource.saveObject( Server.usersAll, "server:users" );
    }
    for ( User x : users ) {
      System.out.println( "Read user: " + x.getLogin( ) );
    }
    return users;
  }
  
  public void createAdmin( ) {
	  boolean flagIsAdminInBase = false;
	  for (int i=0; i<Server.usersAll.size(); i++ ) {
		  if ( Server.usersAll.get(i).getIsAdmin() ) {
			  flagIsAdminInBase = true;
		  }
	  }
	  if ( !flagIsAdminInBase ) {
		  Server.usersAll.add( new User ( "admin", "admin", 0, true ) );
		  this.resource.saveObject( Server.getUsersAll( ), "server:users" );
	  }
  }
	  

  public static HashMap<String, Integer> getClientsMap( ) {
    return Server.clients;
  }

  public static void removeClientsMap( String login ) {
    Server.clients.remove( login );
  }

  public static void addClientsMap( String login, Integer id ) {
    Server.clients.put( login, id );
  }

  public static HashMap<Integer, UserThread> getThreadsMap( ) {
    return Server.threads;
  }

  public static void removeThreadsMap( Integer id ) {
    Server.threads.remove( id );
  }

  public static void addThreadsMap( Integer id, UserThread userThread ) {
    Server.threads.put( id, userThread );
  }
  
  public static List<User> getUsersAll( ) {
    return Server.usersAll;
  }

  public static void setUsersAll( ArrayList<User> usersAll ) {
    Server.usersAll = usersAll;
  }
  
  public static void addUserList ( User user ) {
    Server.usersAll.add( user );
  }
  
  public static void removeUserList ( int i ) {
    Server.usersAll.remove( i );
  }

}
