package org.oldcode.urt;

import java.util.Arrays;
import java.util.ArrayList;
import java.io.IOException;
import java.lang.ArrayIndexOutOfBoundsException;

import java.lang.IllegalArgumentException;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

public class MasterServer {

    //private String host = "master.urbanterror.info";
    byte[] addr;// = new byte[4];
    private int port = 27950;

    public MasterServer() {
        // use a static initializer:
        this.addr = new byte[4];
        this.addr[0] = (byte)91;
        this.addr[1] = (byte)121;
        this.addr[2] = (byte)24;
        this.addr[3] = (byte)62;
    }

    public ArrayList<ImmutablePair<String, Integer>> getServerList() 
    {
        byte[] bytes = getServersResponse(); 

        if ((bytes != null) && (bytes.length > 0)) {
            //System.out.println("bytes.length:"+bytes.length);
            return parse(bytes);
        }
        return new ArrayList<ImmutablePair<String, Integer>>();
    }

    public byte[] getServersResponse() {
     
        MessageResponse mr = null;
        try {
            mr = new MessageResponse(this.addr, this.port);
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        try {
            mr.sendMessage("getservers 68 empty full");
            byte[] r = mr.getResponse();
            return r;
        } catch(IOException e) {
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
    public ArrayList<ImmutablePair<String, Integer>> parse(byte[] bytes) 
    {
        ArrayList<ImmutablePair<String, Integer>> list = new ArrayList<ImmutablePair<String, Integer>>();

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
    public ImmutablePair<String, Integer> parse_ip_port(byte[] series) {
        if (series.length != 6) {
            return null;
        }

        // the &0xff "turns" the signed byte into an unsigned (in essence)
        String ip = 
            (series[0]&0xFF) + "." + 
            (series[1]&0xff) + "." + 
            (series[2]&0xff) + "." + 
            (series[3]&0xff);
        int port = (series[4]*256) + series[5];
        return new ImmutablePair<String, Integer>(ip, port);
    }


}

