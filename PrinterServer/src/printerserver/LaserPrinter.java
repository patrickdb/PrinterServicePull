/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.lang.management.ManagementFactory;
import java.net.*;
import java.net.UnknownHostException;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.util.Enumeration;

/**
 *
 * @author Patrick de Beer
 */

// Step 1.
// On the server side a laser printer object is created which implements the interface IPrinter
// UnicastRemoteObject is needed to make it possible to register it in RMI.
public class LaserPrinter extends UnicastRemoteObject implements IPrinter{
    private static int printJobIndex ;
    public LaserPrinter() throws RemoteException {
        printJobIndex = 0;        
    }

    
    public int printJob(String text) throws RemoteException {
        System.out.println("Mock printer : " + text);
        return (++printJobIndex);
    }

// Step 3
// Creating the server and register it to the RMI registry.
    public static void main(String[] args) {
        try {
            System.out.println("VM machine [" + ManagementFactory.getRuntimeMXBean().getName() + "]");

            // <editor-fold defaultstate="collapsed" desc="Creating the laserPrinter object as a server">
            IPrinter printer = new LaserPrinter();
            // </editor-fold>
            
            // <editor-fold defaultstate="collapsed" desc="Instantiate a registry where clients can lookup the (laser)printer service">
            Registry registry = LocateRegistry.createRegistry(1099);
            //Registry registry = LocateRegistry.getRegistry();

            // </editor-fold>
            System.out.println("registry running at: " + registry.toString());

            // The resolve order for binding the registry is the first eth_x which has a regular ip
            // in the binding order in the windows network center, advanced settings
            //printIPAddresses();

            // <editor-fold defaultstate="collapsed" desc="Bind the UnicastRemoteObject/IPrinter LaserPrinter object in the registry.">
            Naming.bind("rmi://192.168.99.1:1099/Printer", printer);

             System.out.println("LaserPrinter server has been started succesful");
             // </editor-fold>
             
        // <editor-fold defaultstate="collapsed" desc="Catch the necessary exceptions">
        } catch (RemoteException ex) {
            System.out.println("Problem with instantiation of server " + ex.toString());
        } catch (MalformedURLException ex) {
            System.out.println("Malformed URL exception  " + ex.toString());
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }

        // </editor-fold>
    }

    // Print IP addresses and network interfaces
    private static void printIPAddresses() {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            System.out.println("Server: IP Address: " + localhost.getHostAddress());
            // Just in case this host has multiple IP addresses....
            InetAddress[] allMyIps = InetAddress.getAllByName(localhost.getCanonicalHostName());
            if (allMyIps != null && allMyIps.length > 1) {
                System.out.println("Server: Full list of IP addresses:");
                for (InetAddress allMyIp : allMyIps) {
                    System.out.println("    " + allMyIp);
                }
            }
        } catch (UnknownHostException ex) {
            System.out.println("Server: Cannot get IP address of local host");
            System.out.println("Server: UnknownHostException: " + ex.getMessage());
        }

        try {
            System.out.println("Server: Full list of network interfaces:");
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                System.out.println("    " + intf.getName() + " " + intf.getDisplayName());
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    System.out.println("        " + enumIpAddr.nextElement().toString());
                }
            }
        } catch (SocketException ex) {
            System.out.println("Server: Cannot retrieve network interface list");
            System.out.println("Server: UnknownHostException: " + ex.getMessage());
        }
    }
}
