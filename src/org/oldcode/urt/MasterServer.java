package org.oldcode.urt;

import java.util.Arrays;
import java.util.ArrayList;
import java.net.DatagramSocket; 
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ArrayIndexOutOfBoundsException;

import java.lang.IllegalArgumentException;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

public class MasterServer {

    static String host = "master.urbanterror.info";
    static int port = 27950;
    static byte oob = (byte)0xff;

    public static ArrayList<ImmutablePair<String, Integer>> getServerList() 
    {
        byte[] bytes = getServersResponse();
        if (bytes != null) {
            //System.out.println("bytes.length:"+bytes.length);        
            return parse(bytes);
        }
        return null;
    }

    public static byte[] getServersResponse() {

        DatagramSocket ds = null;
        DatagramPacket dp = null;
        InetAddress ia = null;

        try {
            ds = new DatagramSocket();
            ia = InetAddress.getByName(host);

            String out = "xxxxgetservers 68 empty full";
            byte[] buff = out.getBytes();
            buff[0] = oob;
            buff[1] = oob;
            buff[2] = oob;
            buff[3] = oob;
            dp = new DatagramPacket(buff, buff.length, ia, port);

            ds.send(dp);


            //public String getResponse() {
            DatagramPacket dpacket;
            byte[] buffer = new byte[2048];
            String output = "";
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            while (true) {
                //System.out.println("while (true)...");
                try {
                    dpacket = new DatagramPacket(buffer, buffer.length);
                    // Decrease value speeds things up, increase slows things down.
                    ds.setSoTimeout(800);
                    
                    //this is where we should preempt check for the exc. case:
                    ds.receive(dpacket);

                    String packet = new String(dpacket.getData(), 0, dpacket.getLength());
                    output += packet;
                    baos.write(dpacket.getData(), 0, dpacket.getLength());
                } catch (IOException e1) { //we shouldn't use an exception for flow control
                    //System.out.println("e1:"+e1);
                    break;
                }
            } 
            
            //System.out.println(output);
            //System.out.println(baos);

            byte[] bytes = baos.toByteArray();
            return bytes;
            //parse(bytes);

        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /*
      this comment is taken from a python q3 lib:
      parse the response 
      packet format: all packets starts with the OOB bytes and a
      'getserversResponse' string. after this start the server list starts
      which are each 7 bytes. Start with the char / (first byte), the next
      four bytes are the ip and the last two bytes is the port number
      transmission of servers ends with '/EOF' at the end of the last packet
    */
    public static ArrayList<ImmutablePair<String, Integer>> parse(byte[] bytes) 
    {
        ArrayList<ImmutablePair<String, Integer>> list = new ArrayList<ImmutablePair<String, Integer>>();

        //System.out.println("bytes.length:"+bytes.length);

        //find the firt '/' or (byte)92:
        int next, start = ArrayUtils.indexOf(bytes, (byte)92, 0); //this should always be 22
        
        byte b;
        //the implication is this loop will always enter with i on the indexOf a '/':
        ImmutablePair<String, Integer> ip_port = null;
        for (int i=start; i<bytes.length; i++) {

            //this might be out of bounds at the end
            //hopefully its always the last one
            try {
                b = bytes[i];
            } catch(ArrayIndexOutOfBoundsException aioob) {
                break;
            }

            //find the index of the next '/': (this should always be 7 more
            next = ArrayUtils.indexOf(bytes, (byte)92, i+1); 
            //System.out.println("next:"+next+"--"+(next-i));
          
            try { //hax way to not worry about reaching the end
                ip_port = parse_ip_port(Arrays.copyOfRange(bytes, i+1, next));
            } catch(IllegalArgumentException iae) { 
                ip_port = null;
            }

            if (ip_port != null) {
                //System.out.println("ip:"+ip_port.left+" port:"+ip_port.right);
                list.add(ip_port);
            } else {
                //System.out.println("bad series"); the last one will always fail too
            }

            i = next-1;
            continue;

        }

        if (list.isEmpty()) {
            return null;
        } else {
            return list;
        }

    }
    
    //series is intended to be the 7 bytes between /'s
    public static ImmutablePair<String, Integer> parse_ip_port(byte[] series) {
        ImmutablePair<String, Integer> pair = null;
        //System.out.println("parse_ip_port()..." + series[0] + "::" + series[series.length-1] + " length:"+series.length);

        if (series.length != 6) {
            //System.out.println("ERROR length:"+series.length + " != 6");
            return pair;
        }

        // the &0xff "turns" the signed byte into an unsigned (in essence)
        String ip = 
            (series[0]&0xFF) + "." + 
            (series[1]&0xff) + "." + 
            (series[2]&0xff) + "." + 
            (series[3]&0xff);
        int port = (series[4]*256) + series[5];
        //System.out.println("ip:" + ip + " port:"+ port);
        return new ImmutablePair<String, Integer>(ip, port);
    }


}

