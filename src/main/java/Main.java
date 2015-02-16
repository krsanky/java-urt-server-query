import org.apache.commons.lang3.tuple.ImmutablePair;
import org.oldcode.urt.MasterServer;
import org.oldcode.urt.ServerDetail;
import org.oldcode.urt.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        run_master_query();
        server_query();
        System.out.println("asd...");
    }

    public static void server_query() {
        // 64.74.97.153   Port: 27960    superman
        // 64.94.238.89   Port: 27960   urtctf
        byte[] addr = new byte[4];
        addr[0] = (byte)64;
        addr[1] = (byte)74;
        addr[2] = (byte)97;
        addr[3] = (byte)153;
        int port = 27960;

        ServerDetail sd = new ServerDetail(addr, port);

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
