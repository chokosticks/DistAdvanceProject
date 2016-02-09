package dist.project;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sics.kompics.ClassMatchedHandler;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Positive;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.Transport;

public class Ponger extends ComponentDefinition{
	
	private static final Logger LOG = LoggerFactory.getLogger(Ponger.class);
	
	//Negative<PingPongPort> ppp = provides(PingPongPort.class);
	
	Positive<Network> net = requires(Network.class);
	
	private long counter = 0;
	private final TAddress self;
	
	public Ponger(){
		this.self = config().getValue("pingpong.self", TAddress.class);
	}
	
//	public Ponger(Init init){
//		this.self = init.self;
//	}
	
    ClassMatchedHandler<Ping, TMessage> pingHandler = new ClassMatchedHandler<Ping, TMessage>() {
        @Override
        public void handle(Ping content, TMessage context) {
            counter++;
            LOG.info("Got Ping #{}!", counter);
            trigger(new TMessage(self, context.getSource(), Transport.TCP, new Pong()), net);
        }
    };
	{
		subscribe(pingHandler, net);
	}
	
//	public static class Init extends se.sics.kompics.Init<Ponger>{
//		public final TAddress self;
//		
//		public Init(TAddress self){
//			this.self = self;
//		}
//	}
}
