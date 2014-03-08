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
        //72.5.102.198:27960  6th
        byte[] addr = new byte[4];
        addr[0] = (byte)72;
        addr[1] = (byte)5;
        addr[2] = (byte)102;
        addr[3] = (byte)198;
        int port = 27960;

        ServerDetail sd = new ServerDetail(addr, port);
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
