package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class App 
{
    public static void main( String[] args )
    {
        try {
            ServerSocket server = new ServerSocket(8080);
            System.out.println("server attivo");
            
            while(true){
                Socket s = server.accept();

                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                PrintWriter out = new PrintWriter(s.getOutputStream());

                String linkSearch= in.readLine();
                System.out.println(linkSearch);
                String[] linkFinder = linkSearch.split(" ");
                String link = "."+linkFinder[1];

                

                do {
                   String str= in.readLine();
                   System.out.println(str);
                   if (str == null || str.equals("")) {
                    break;
                   }
                } while (true);

                sendFile(out, link);

                out.flush();
                s.close();
            }

            
        } catch (Exception e) {
            
        }
    }


    private static void sendFile(PrintWriter out, String file){
        try {
            File f = new File(file);
            Scanner myObj = new Scanner(f);

            out.println("HTTP/1.1 200 OK");
            out.println("Content-Length: "+ f.length());
            out.println("Server: Java HTTP Server from Pavlov: 1.0");
            out.println("Date: "+ new Date());
            out.println("Content-Type: text/html; charset=utf-8");
            out.println();

            while(myObj.hasNextLine()){
                out.println(myObj.nextLine());
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        

    }
}
