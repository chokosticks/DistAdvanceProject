package dist.project;

import se.sics.kompics.PortType;

public class PingPongPort extends PortType {
	{
		request(Ping.class);
		indication(Pong.class);
	}
}
