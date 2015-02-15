package org.oldcode.urt;

import java.net.DatagramSocket; 
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.ByteArrayOutputStream;

public class MessageResponse {

    private InetAddress address = null;
    private int port;
    private String message; //not using this
    static byte oob = (byte)0xff;

    private DatagramSocket ds;
    //private DatagramPacket dp;

    public MessageResponse(byte[] ip, int port)
        throws UnknownHostException
    {
        this.address = InetAddress.getByAddress(ip);
        this.port = port;
    }

    public MessageResponse(byte[] ip, int port, String message)
        throws UnknownHostException
    {
        this(ip, port);
        this.message = message;
    }

    @Override
    public String toString() {
        return "<MessageResponse address:"+this.address+" port:"+this.port+" message:"+message+">";
    }

    public void sendMessage(String msg) 
        throws IOException
    {
        this.ds = new DatagramSocket();

        String out = "xxxx" + msg;
        byte[] buff = out.getBytes();
        buff[0] = oob;
        buff[1] = oob;
        buff[2] = oob;
        buff[3] = oob;
        DatagramPacket dp = new DatagramPacket(buff, buff.length, this.address, this.port);

        //System.out.println("sending:" + buff);
        //for (byte b: buff) {
        //    System.out.print((char)b + " ");
        //}
        //System.out.println();
        this.ds.send(dp);
    }

    // assumes a message has been sent
    public byte[] getResponse() {
        DatagramPacket dpacket;
        byte[] buffer = new byte[2048];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        while (true) {
            //System.out.println("getr .");
            try {
                dpacket = new DatagramPacket(buffer, buffer.length);
                // Decrease value speeds things up, increase slows things down.
                this.ds.setSoTimeout(1000);
                    
                //this is where we should preempt check for the exc. case:
                this.ds.receive(dpacket);

                String packet = new String(dpacket.getData(), 0, dpacket.getLength());
                baos.write(dpacket.getData(), 0, dpacket.getLength());
            } catch (IOException e) { //we shouldn't use an exception for flow control
                //System.out.println("e:"+e);
                break;
            }
        } 
            
        //System.out.println(baos);

        byte[] bytes = baos.toByteArray();
        return bytes;
    }

}

