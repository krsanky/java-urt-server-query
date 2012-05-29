import java.util.ArrayList;

import java.sql.*;
import java.io.IOException;
import java.net.*;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.HashMap;

import org.apache.commons.lang3.tuple.ImmutablePair;

import org.oldcode.urt.MasterServer;
import org.oldcode.urt.Util;
import org.oldcode.urt.ServerDetail;
import org.oldcode.urt.MessageResponse;
import org.oldcode.urt.Player;

public class Main {

    public static void main(String[] args) {
        //run_master_query();
        server_query();
    }

    public static void server_query() {
        //um3: 68.232.168.18
        byte[] addr = new byte[4];
        addr[0] = (byte)68;
        addr[1] = (byte)232;
        addr[2] = (byte)168;
        addr[3] = (byte)18;
        int port = 27960;
        //sm: 64.74.97.153 27960
        //    64.74.97.153
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

        ServerDetail sd = new ServerDetail(a_addr, port);
        byte[] r = null;
        
        //sd.setResponseFromServer();
        //r = sd.getResponse();        
        //System.out.println(new String(r));
        
        HashMap<String, String> vars = sd.getVars();
        for (Map.Entry<String, String> e: vars.entrySet()) {
            System.out.println("k:"+e.getKey()+" v:"+e.getValue());
        }

        Player[] players = sd.getPlayers();
        for (Player p: players) {
            System.out.println(p);
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
