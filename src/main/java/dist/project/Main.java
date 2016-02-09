package dist.project;

import java.net.InetAddress;
import java.net.UnknownHostException;

import se.sics.kompics.Kompics;
import se.sics.kompics.config.Conversions;
import se.sics.kompics.network.netty.serialization.Serializers;

public class Main {
	
	static {
        // register
        Serializers.register(new NetSerializer(), "netS");
        Serializers.register(new PingPongSerializer(), "ppS");
        // map
        Serializers.register(TAddress.class, "netS");
        Serializers.register(THeader.class, "netS");
        Serializers.register(TAddress.class, "netS");
        Serializers.register(Ping.class, "ppS");
        Serializers.register(Pong.class, "ppS");
        // conversions
        Conversions.register(new TAddressConverter());
	}
	
	public static void main(String[] args){	
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("ponger")) {
                Kompics.createAndStart(PongerParent.class, 2);
                System.out.println("Starting Ponger");
                // no shutdown this time...act like a server and keep running until externally exited
            } else if (args[0].equalsIgnoreCase("pinger")) {
                Kompics.createAndStart(PingerParent.class, 2);
                System.out.println("Starting Pinger");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    System.exit(1);
                }
                Kompics.shutdown();
                System.exit(0);
            }
        } else {
            System.err.println("Invalid number of parameters");
            System.exit(1);
        }
		
//		try {
//			InetAddress ip = InetAddress.getLocalHost();
//			int port = Integer.parseInt(args[0]);
//			TAddress self = new TAddress(ip, port);
//			Kompics.createAndStart(Parent.class, new Parent.Init(self), 2);
//			try {
//				Thread.sleep(10000);
//			} catch (InterruptedException ex) {
//				System.exit(1);
//			}
//			Kompics.shutdown();
//			System.exit(0);
//		} catch (UnknownHostException ex) {
//			System.err.println(ex);
//			System.exit(1);
//		}
	}
}
