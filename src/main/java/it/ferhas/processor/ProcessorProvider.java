package it.ferhas.processor;

import lombok.Getter;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;

/**
 * Provider that scans all available processors at application startup and provide them when needed.
 */
public class ProcessorProvider {
    @Getter
    private static final List<Processor> processors = new ArrayList<>();

    static {
        Reflections reflections = new Reflections(ProcessorProvider.class.getPackage().getName());
        Set<Class<?>> processorTypes = reflections.get(SubTypes.of(AbstractProcessor.class).asClass());

        for (Class<?> processorType : processorTypes) {
            try {
                if (!Modifier.isAbstract(processorType.getModifiers())) {
                    processors.add((Processor) processorType.getDeclaredConstructor().newInstance());
                }
            } catch (Exception e) {
                throw new ExceptionInInitializerError("Failed to initialize application processors");
            }
        }
    }
}
