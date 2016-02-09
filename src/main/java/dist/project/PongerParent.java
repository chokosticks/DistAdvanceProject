package dist.project;

import se.sics.kompics.Channel;
import se.sics.kompics.Component;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.netty.NettyInit;
import se.sics.kompics.network.netty.NettyNetwork;

public class PongerParent extends ComponentDefinition{
	
	public PongerParent(Init init){
		Component network = create(NettyNetwork.class, new NettyInit(init.self));
		Component ponger = create(Ponger.class, Init.NONE);
		
		connect(ponger.getNegative(Network.class), network.getPositive(Network.class), Channel.TWO_WAY);
	}
	
	public static class Init extends se.sics.kompics.Init<PongerParent>{
		public final TAddress self;
		
		public Init(TAddress self){
			this.self = self;
		}
	}
}
