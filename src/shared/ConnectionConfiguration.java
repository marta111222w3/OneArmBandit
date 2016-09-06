package shared;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ConnectionConfiguration {

  private static final Logger LOGGER = LogManager
    .getLogger( ConnectionConfiguration.class );

  private static final String FILE_NAME = "config.properties";
  private static ConnectionConfiguration instance = null;
  private String address;
  private int port;

  private ConnectionConfiguration( ) {

  }

  public static ConnectionConfiguration getConfiguration( ) {
    if ( instance == null ) {
      instance = buildConfiguration( );
    }
    return instance;
  }

  public static ConnectionConfiguration buildConfiguration( ) {
    Properties props = new Properties( );
    ConnectionConfiguration scc = new ConnectionConfiguration( );
    try {
      InputStream input = new FileInputStream( FILE_NAME );
      props.load( input );
      String number = props.getProperty( "port" );
      scc.port = Integer.parseInt( number );
      scc.address = props.getProperty( "ip" );
      LOGGER.info( "Successfuly loaded values from config.properties file" );
    } catch ( FileNotFoundException e ) {
      LOGGER.fatal( "Cannot find config.properties file!" );
      e.printStackTrace( );
    } catch ( IOException e ) {
      LOGGER.fatal( "Cannot read from config.properties file!" );
      e.printStackTrace( );
    }
    return scc;
  }

  public String getAddress( ) {
    return this.address;
  }

  public int getPort( ) {
    return this.port;
  }

}
