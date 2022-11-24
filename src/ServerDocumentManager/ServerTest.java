package ServerDocumentManager;

import javax.swing.JFrame;

public class ServerTest
{
    public static void main( String args[] )
    {
        Server_TeacherGive application = new Server_TeacherGive(); // create server
        application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        application.runServer(); // run server application
    } // end main
} // end class ServerTest
