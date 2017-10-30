/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmiprinter;

import java.rmi.*;

/**
 *
 * @author
 */

// Step 2
// The server shall implement the IPrinter interface
// This interface needs to extend Remote, to make it RMI implementable
public interface IPrinter extends Remote{
    
    // Don't forget to indicate that RemoteException should be thrown by each method
    int printJob(String text) throws RemoteException;
}
