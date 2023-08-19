package it.ferhas;

import it.ferhas.forwarder.ForwarderType;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * Main Class, checks if a custom forwarder was set and launch the process
 */
@Slf4j
public class App {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public static void main(String[] args) {
        ForwarderType forwarderType = getForwarderType(args);

        log.info("Started application using forwarder type: '{}'", forwarderType.name().toLowerCase());

        try {
            AssignmentExecutor executor = new AssignmentExecutor();
            executor.execute(forwarderType);

            System.out.println(ANSI_GREEN + "Application ended without errors." + ANSI_RESET);

        } catch (AppException e) {
            log.error(e.getMessage(), e);
            System.err.println("Application execution error");
            System.err.println(e.getMessage());
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            System.err.println("Fatal unexpected error!");
            System.err.println(e.getCause().getMessage());
        }
    }

    private static ForwarderType getForwarderType(String[] args) {
        ForwarderType forwarderType = ForwarderType.CONSOLE;

        if (args.length != 0) {
            String forwarderArg = args[0];

            try {
                forwarderType = ForwarderType.valueOf(forwarderArg.strip().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid provided forwarder type!");
                System.err.println("Available types: " + Arrays.toString(ForwarderType.values()));
                System.err.println("Using default " + forwarderType.name() + " forwarder...");

                log.warn("Invalid forwarder type: '{}', fallback to default: '{}'", forwarderArg, forwarderType.name().toLowerCase());
            }
        }
        return forwarderType;
    }
}
