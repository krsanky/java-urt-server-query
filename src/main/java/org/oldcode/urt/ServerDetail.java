package org.oldcode.urt;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.ArrayList;

public class ServerDetail {

    private int port;
    private byte[] addr;// = byte[4];
    private byte[] response = null;

    public ServerDetail(byte[] addr, int port) {    
        this.addr = addr;
        this.port = port;
    }

    @Override
    public String toString() {
        return "<ServerDetail addr:"+this.addr+" port:"+this.port+">";
    }

    public byte[] getServerResponse()
        throws UnknownHostException, IOException
    {
        MessageResponse mr = null;
        mr = new MessageResponse(this.addr, this.port);
        mr.sendMessage("getstatus");
        return mr.getResponse();
    }

    public boolean setResponseFromServer() {
        try {
            this.response = getServerResponse();
        } catch(UnknownHostException e) {
            return false;
        } catch(IOException          e) {
            return false;
        }
        return true;
    }

    public HashMap<String, String> getVars() {
        HashMap<String, String> map = new HashMap<String, String>();
        if (response == null) {
            if (! setResponseFromServer()) {
                return map;
            }
        }
        String str = new String(this.response);
        String[] lines = str.split("\\n");
        String raw_vars;
        if (lines.length > 1)
            raw_vars = lines[1];
        else
            return map;

        String[] pieces = raw_vars.split("\\\\");
        for (int i=1; i<pieces.length; i++) {
            //System.out.println("HHH"+pieces[i]+"PPP");
            if ((i+1) < pieces.length) {
                map.put(pieces[i], pieces[i+1]);
            }
        }

        return map;
    }

    public Player[] getPlayers() {
        if (response == null) {
            if (! setResponseFromServer()) {
                return null;
            }
        }
        String str = new String(this.response);
        String[] lines = str.split("\\n");
        
        ArrayList<Player> players = new ArrayList<Player>();
        for (int i=2; i<lines.length; i++) {
            String[] pieces = breakPlayerLine(lines[i]);
            Player p = new Player(pieces[2], pieces[0], pieces[1]); //name score ping
            p.cleanName();
            players.add(p);
        }
        
        Player[] pa = players.toArray(new Player[players.size()]);
        return pa;
    }

    private String[] breakPlayerLine(String line) {
        String[] arr = new String[3];
        
        String score_ping = line.substring(0, line.indexOf('\"'));
        String[] tmp = score_ping.split(" ");
        arr[0] = tmp[0]; // Score
        arr[1] = tmp[1]; // Ping

        arr[2] = line.substring(line.indexOf('\"') + 1, line.lastIndexOf('\"'));
        
        return arr; //score, ping, name
    }

    public byte[] getResponse() {
        return this.response;
    }

}

