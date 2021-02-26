package de.othr.cc;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import de.othr.cc.rest.StudentService;
import jakarta.ws.rs.ext.RuntimeDelegate;
import org.glassfish.jersey.server.ResourceConfig;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class RestServerTest {
    public static void main(String[] args) throws IOException {
        // Create configuration object for webserver instance
        ResourceConfig config = new ResourceConfig();
        // Register REST-resources (i.e. service classes) with the webserver
        config.register(StudentService.class);
        // add further REST-resources like this:  config.register(Xyz.class);

        // Create webserver instance and start it
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        HttpHandler handler = RuntimeDelegate.getInstance().createEndpoint(config, HttpHandler.class);
        // Context is part of the URI directly after  http://domain.tld:port/
        server.createContext("/restapi", handler);
        server.start();

        // Show dialogue in order to prevent premature ending of server(s)
        //JOptionPane.showMessageDialog(null, "Stop server...");
        var scanner = new Scanner(System.in);
        scanner.nextLine();
        server.stop(0);
    }

}
