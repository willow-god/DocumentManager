package ClientDocumentManager;


import javax.swing.JFrame;

public class ClientTest
{
    public static void main( String args[] )
    {
        Client_TeacherGive application; // declare client application

        // if no command line args
        if ( args.length == 0 )
            application = new Client_TeacherGive( "127.0.0.1" ); // connect to localhost
        else
            application = new Client_TeacherGive( args[ 0 ] ); // use args to connect

        application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        application.runClient(); // run client application
    } // end main
} // end class ClientTest

