package dist.project;




import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sics.kompics.ClassMatchedHandler;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Positive;
import se.sics.kompics.Start;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.Transport;
import se.sics.kompics.timer.*;
import se.sics.kompics.timer.Timeout;


public class Pinger extends ComponentDefinition {
	
	private static final Logger LOG = LoggerFactory.getLogger(Pinger.class);
	
	//Positive<PingPongPort> ppp = requires(PingPongPort.class);
	Positive<Timer> timer = requires(Timer.class);
	Positive<Network> net = requires(Network.class);
	
	
	private long counter = 0;
	private UUID timerId;
	private final TAddress self;
	private final TAddress ponger;
	
	public Pinger(){
		this.self = config().getValue("pingpong.self", TAddress.class);
		this.ponger = config().getValue("pingpong.pinger.pongeraddr", TAddress.class);
	}
	
	Handler<Start> startHandler = new Handler<Start>(){
		public void handle(Start event){
			long period = config().getValue("pingpong.pinger.timeout", Long.class);
			SchedulePeriodicTimeout spt = new SchedulePeriodicTimeout(0,period);
			PingTimeout timeout = new PingTimeout(spt);
			spt.setTimeoutEvent(timeout);
			trigger(spt, timer);
			timerId = timeout.getTimeoutId();
		}
	};
	
    ClassMatchedHandler<Pong, TMessage> pongHandler = new ClassMatchedHandler<Pong, TMessage>() {

        @Override
        public void handle(Pong content, TMessage context) {
            counter++;
            LOG.info("Got Pong #{}!", counter);
        }
    };
	
    Handler<PingTimeout> timeoutHandler = new Handler<PingTimeout>() {
        @Override
        public void handle(PingTimeout event) {
            trigger(new TMessage(self, ponger, Transport.TCP, new Ping()), net);
        }
    };
	
	{
		subscribe(startHandler, control);
		subscribe(pongHandler, net);
		subscribe(timeoutHandler, timer);
	}
	
	@Override
	public void tearDown() {
    	trigger(new CancelPeriodicTimeout(timerId), timer);
	}
	
	public static class PingTimeout extends Timeout {
		public PingTimeout(SchedulePeriodicTimeout spt) {
			super(spt);
		}
	}
	
//	public static class Init extends se.sics.kompics.Init<Pinger> {
//		public final TAddress self;
//		public final TAddress ponger;
//		
//		public Init(TAddress self, TAddress ponger) {
//			this.self = self;
//			this.ponger = ponger;
//		}
//	}
}



