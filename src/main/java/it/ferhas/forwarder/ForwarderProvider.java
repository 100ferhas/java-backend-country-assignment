package it.ferhas.forwarder;

import it.ferhas.forwarder.impl.ConsoleForwarder;
import it.ferhas.forwarder.impl.FileForwarder;

import java.util.HashMap;
import java.util.Map;

/**
 * Provider class to retrieve the correct Forwarder to use
 */
public class ForwarderProvider {
    private static final Map<ForwarderType, Forwarder> forwarderTypeMapping = new HashMap<>() {{
        put(ForwarderType.CONSOLE, new ConsoleForwarder());
        put(ForwarderType.FILE, new FileForwarder());
    }};

    public static Forwarder getForwarder(ForwarderType forwarderType) {
        return forwarderTypeMapping.getOrDefault(forwarderType, new ConsoleForwarder());
    }
}
