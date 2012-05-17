import java.util.ArrayList;

import java.io.IOException;
import java.net.*;

import org.apache.commons.lang3.tuple.ImmutablePair;

import org.oldcode.urt.MasterServer;
import org.oldcode.urt.Util;
import org.oldcode.urt.ServerDetail;
import org.oldcode.urt.MessageResponse;

public class Main {

    public static void main(String[] args) {
        run_master_query();
    }

    public static void server_query() {
        System.out.println("start...");
        Util.test();

        /*
        ip:85.25.109.153 port:27960
        ip:91.121.6.213 port:27960
        ip:188.138.48.106 port:27960
        ip:195.110.8.164 port:27960
        ip:69.60.116.216 port:27960
        */
        ServerDetail sd = new ServerDetail("123.23.123.12", 91223);
        System.out.println("server-detail: "+sd);
        //sm: 64.74.97.153
        //master: 91.121.24.62  27950
        byte[] addr = new byte[4];
        addr[0] = (byte)91;
        addr[1] = (byte)121;
        addr[2] = (byte)24;
        addr[3] = (byte)62;

        String host = "master.urbanterror.info";
        int lport = 27950;


        MessageResponse mr = null;
        try {
            mr = new MessageResponse(addr, 27950, "you are gay");
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }



        System.out.println("msg-resp: "+mr);
        try {
            mr.sendMessage("getservers 68 empty full");
            //mr.sendMessage("getstatus");
            byte[] r = mr.getResponse();
            System.out.println("r.length:" +r.length);
            for (byte b: r) {
                System.out.println(b);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        
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
