package io.github.dependencyjack.analyze;

import spoon.reflect.CtModel;

import java.util.Set;

/**
 * Used to find dependencies in a given spoon model
 */
public interface DependencyAnalyzer {
    /**
     * Set the model to be analyzed by the dependency analyzer
     */
    void setModel(CtModel model);

    /**
     * Fetch a set of all dependencies found by the analyzer in the provided model
     *
     * @return A set of dependencies as fully qualified class names
     */
    Set<String> getDependencies();
}
