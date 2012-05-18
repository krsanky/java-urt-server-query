import java.util.ArrayList;

import java.io.IOException;
import java.net.*;
import java.io.ByteArrayOutputStream;

import org.apache.commons.lang3.tuple.ImmutablePair;

import org.oldcode.urt.MasterServer;
import org.oldcode.urt.Util;
import org.oldcode.urt.ServerDetail;
import org.oldcode.urt.MessageResponse;

public class Main {

    public static void main(String[] args) {
        //run_master_query();
        server_query();
    }

    public static void server_query() {
        System.out.println("start server getstatus...");

        //um3: 68.232.168.18
        byte[] addr = new byte[4];
        addr[0] = (byte)68;
        addr[1] = (byte)232;
        addr[2] = (byte)168;
        addr[3] = (byte)18;
        int port = 27960;
        //sm: 64.74.97.153 27960
        byte[] sm_addr = new byte[4];
        sm_addr[0] = (byte)64;
        sm_addr[1] = (byte)74;
        sm_addr[2] = (byte)97;
        sm_addr[3] = (byte)153;
        byte[] mst_addr = new byte[4];
        mst_addr[0] = (byte)91;
        mst_addr[1] = (byte)121;
        mst_addr[2] = (byte)24;
        mst_addr[3] = (byte)62;
        int mst_port = 27950;
        //67.202.120.216:27960 ut4_abbey
        byte[] a_addr = new byte[4];
        a_addr[0] = (byte)67;
        a_addr[1] = (byte)202;
        a_addr[2] = (byte)120;
        a_addr[3] = (byte)216;

        //URBANTERROR {Z9} Zombie Mode Server Join 35/40 173.236.38.244:27960 ut_rt
        //URBANTERROR WWW.FALLIN-ANGELS.ORG Join 28/32 209.190.50.170:27960



        byte oob = (byte)0xff;
        String data = "getstatus";
        DatagramPacket dp;
        DatagramSocket ds = null;
        InetAddress address = null;;
        try {
            address = InetAddress.getByAddress(a_addr);
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("address:"+address);
        try {
            ds = new DatagramSocket();
            String out = "xxxx"+data;
            byte [] buff = out.getBytes();
            buff[0] = oob;
            buff[1] = oob;
            buff[2] = oob;
            buff[3] = oob;
            dp = new DatagramPacket(buff, buff.length, address, port);
            ds.send(dp);
        } catch (Exception e) {
            System.out.println("Send method in BowserQuery Failed with: "+e.getMessage());
        }

        //get results
        DatagramPacket dpacket;
        byte[] buffer = new byte[2048];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        while (true) {
            System.out.println("getr .");
            try {
                dpacket = new DatagramPacket(buffer, buffer.length);
                ds.setSoTimeout(1000);
                ds.receive(dpacket);
                baos.write(dpacket.getData(), 0, dpacket.getLength());
            } catch (IOException e) { //we shouldn't use an exception for flow control
                System.out.println("e:"+e);
                break;
            }
        } 
            
        byte[] bytes = baos.toByteArray();
        //for (byte b: bytes) {            System.out.print(b+"-");                    }
        System.out.println(new String(bytes));
        
    }

    public static void run_master_query() {
        System.out.println("start test master query...");
        MasterServer ms = new MasterServer();
        ArrayList<ImmutablePair<String, Integer>> list = ms.getServerList();
        for (ImmutablePair<String, Integer> pair: list) {
            System.out.println("ip:"+pair.left+" port:"+pair.right);
        }

    }

}
