package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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
                DataOutputStream out = new DataOutputStream(s.getOutputStream());

                String linkSearch= in.readLine();
                String[] linkFinder = linkSearch.split(" ");
                String link = linkFinder[1];
                link = "prova"+link;
                

                /*File f = new File ("prova"+link);
                if(!f.getName().endsWith("/")){
                    if(f.exists()){
                        sendFile(out,f);
                    }else{
                        redirect(out, link+"/");
                    }
                }else{
                    File temp = new File (f.getName()+"index.html");
                    if(temp.exists())
                    sendFile(out, f);
                    else
                    sendErr(out, f);
                }*/

                if(link.endsWith("/")){
                    link = link+"index.html";
                    sendFile(out, new File(link),404);
                }else{
                    sendFile(out,new File(link),301);
                    //bisogna aggiungere il redirect in questo 
                }
                        
            //sendFile(out, f);

                out.flush();
                s.close();
            }

            
        } catch (Exception e) {
            
        }
    }

    private static void redirect(DataOutputStream out, String s){
        try{
            out.writeBytes("HTTP/1.1 301 \n");
            out.writeBytes("Location:"+s+"\n");
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }  
       
    }

    private static void sendErr(DataOutputStream out, File f){
        try{     
             out.writeBytes("HTTP/1.1 404 ERROR \n");
             out.writeBytes("Content-Length: "+ f.length()+"\n" );
             out.writeBytes("Content-Type: text/plain; charset=utf-8 \n");
             out.writeBytes("\n");
            
            }catch (Exception e) {
            System.out.println(e.getMessage());
        }  
    }


    private static void sendFile(DataOutputStream out, File f,int val){
        try {

            if (f.exists()) {
                
            
            out.writeBytes("HTTP/1.1 200 OK \n");
            out.writeBytes("Content-Length: "+ f.length()+"\n" );
            out.writeBytes("Server: Java HTTP Server from Pavlov: 1.0"+ "\n");
            out.writeBytes("Date: "+ new Date() +"\n" );
            out.writeBytes("Content-Type: " + getContentType(f)+"; charset=utf-8 \n");
            out.writeBytes("\n");

            InputStream input = new FileInputStream(f);
            byte buf[] = new byte[8192];
            int n;
            while ((n = input.read(buf)) != -1){
                out.write(buf, 0, n);
            }
            input.close();
        }else if(val == 404)
        sendErr(out, f);
        else
        redirect(out, f.getName()+"/");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        

    }

    private static String getContentType(File f){
        String s[] = f.getName().split("\\.");
        String ext = s[s.length-1];
        switch(ext){
            case "html":
            case "htm":
                return "text/html";
            case "png":
                return "image/png";

            case "css":
                return "text/html";
            
            default:
                return"";
        }


    }
}
