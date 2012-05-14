import java.util.ArrayList;

import org.apache.commons.lang3.tuple.ImmutablePair;

import org.oldcode.urt.MasterServer;


public class Main {

    public static void main(String[] args) {
        System.out.println("start...");
            
        ArrayList<ImmutablePair<String, Integer>> list = MasterServer.getServerList();

        for (ImmutablePair<String, Integer> pair: list) {
            System.out.println("ip:"+pair.left+" port:"+pair.right);
        }

    }

}
