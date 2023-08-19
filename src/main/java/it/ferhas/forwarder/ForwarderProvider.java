package it.ferhas.forwarder;

import it.ferhas.forwarder.impl.ConsoleForwarder;
import it.ferhas.forwarder.impl.FileForwarder;
import it.ferhas.forwarder.impl.KafkaForwarder;
import it.ferhas.forwarder.impl.RestApiForwarder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Provider class to retrieve the correct Forwarder to use
 */
public class ForwarderProvider {
    private static final Map<ForwarderType, Forwarder> forwarderTypeMapping = new HashMap<>() {{
        put(ForwarderType.CONSOLE, new ConsoleForwarder());
        put(ForwarderType.FILE, new FileForwarder());
        put(ForwarderType.KAFKA, new KafkaForwarder());
        put(ForwarderType.REST, new RestApiForwarder());
    }};

    public static Forwarder getForwarder(ForwarderType forwarderType) {
        return forwarderTypeMapping.getOrDefault(forwarderType, new ConsoleForwarder());
    }
}
