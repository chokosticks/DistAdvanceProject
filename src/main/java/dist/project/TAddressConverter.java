package dist.project;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import se.sics.kompics.config.Conversions;
import se.sics.kompics.config.Converter;


public class TAddressConverter implements Converter<TAddress>{
	
    @Override
    public TAddress convert(Object o) {
        if (o instanceof Map) {
            try {
                Map m = (Map) o;
                String hostname = Conversions.convert(m.get("host"), String.class);
                int port = Conversions.convert(m.get("port"), Integer.class);
                InetAddress ip = InetAddress.getByName(hostname);
                return new TAddress(ip, port);
            } catch (UnknownHostException ex) {
                return null;
            }
        }
        if (o instanceof String) {
            try {
                String[] ipPort = ((String) o).split(":");
                InetAddress ip = InetAddress.getByName(ipPort[0]);
                int port = Integer.parseInt(ipPort[1]);
            } catch (UnknownHostException ex) {
                return null;
            }
        }
        return null;
    }

    @Override
    public Class<TAddress> type() {
        return TAddress.class;
    }
}
