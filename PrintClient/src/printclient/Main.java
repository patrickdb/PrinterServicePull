/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.lang.management.ManagementFactory;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import javax.naming.*;

/**
 *
 * @author Patrick de Beer
 */

public class Main {
    public static void main(String[] args) {
        try {

            String rmi_registry = "rmi://192.168.99.1:1099/";

            // <editor-fold defaultstate="collapsed" desc="Which services are registered in the RMI naming service?">
            Context namingContext = new InitialContext();

            System.out.println("VM machine [" + ManagementFactory.getRuntimeMXBean().getName() + "]");
            System.out.print("RMI registry bindings: \n");

            //Registry registry = LocateRegistry.getRegistry();
            //System.out.println(registry.toString());

            Enumeration<NameClassPair> e = namingContext.list(rmi_registry);
            while (e.hasMoreElements()) {
                System.out.println("Service: " + e.nextElement().getName());
            }
            // </ editor-fold>

            String urlService = rmi_registry + "Printer";
            System.out.println(namingContext.lookup(urlService).toString());

            // <editor-fold defaultstate="collapsed" desc="Retrieve the IPrinter service from the RMI naming service">
            IPrinter printer = (IPrinter) namingContext.lookup(urlService);
            // </editor-fold>
            
            // <editor-fold defaultstate="collapsed" desc="Invoke the remote method on the server. For the debugger  it looks like we are running in the same process">
            int returnValue = printer.printJob("Hello RMI");
            System.out.println("Finished printing with print job " + returnValue);
            System.exit(0);
            // </editor-fold>
        } catch (RemoteException ex) {
            System.out.println(ex.toString());
        } catch (NamingException ex) {
            System.out.println(ex.toString());

        }
    }

}
