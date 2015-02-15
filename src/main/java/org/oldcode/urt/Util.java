package org.oldcode.urt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static void test() {
        System.out.println("Util.test()...");
    }

    public static String stripPrintCommands(String input) {
        Pattern r = Pattern.compile("....print\n");
        Matcher m = r.matcher(input);
        return m.replaceAll("");
    }

    public static String stripColors(String input) {
        Pattern r = Pattern.compile("\\^.");
        Matcher m = r.matcher(input);
        return m.replaceAll("");
    }

    // bytes needs to be a 4 element ip address
    /*
    public static InetAddress inetAddressFrom(byte[] bytes) {

        byte[] a_addr = new byte[4];
        a_addr[0] = (byte)67;
        a_addr[1] = (byte)202;
        a_addr[2] = (byte)120;
        a_addr[3] = (byte)216;
        
        return InetAddress.getByAddress(a_addr);
    }*/

}

