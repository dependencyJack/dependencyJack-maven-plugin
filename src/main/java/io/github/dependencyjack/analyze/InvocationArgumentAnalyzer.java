package io.github.dependencyjack.analyze;

import spoon.reflect.CtModel;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InvocationArgumentAnalyzer implements DependencyAnalyzer {

    private CtModel model;


    @Override
    public void setModel(CtModel model) {
        this.model = model;
    }

    @Override
    public Set<String> getDependencies() {
        var methods = getMethods();
        var invocations = getInvocationsForMethods(methods);

        return getInvokedClassArguments(invocations);
    }

    /**
     * Get all methods of the model
     */
    private List<CtMethod<?>> getMethods() {
        var listMethods = new ArrayList<CtMethod<?>>();
        for (CtType<?> s : model.getAllTypes()) {
            listMethods.addAll(s.getMethods());
        }
        return listMethods;
    }

    /**
     * Checks the children of methods to find invocations and returns them
     *
     * @param methods a list of spoon methods
     * @return Children which are of type CtInvocation
     */
    private Set<CtInvocation<?>> getInvocationsForMethods(List<CtMethod<?>> methods) {
        var invocations = new HashSet<CtInvocation<?>>();

        for (var method : methods) {
            method.filterChildren(new TypeFilter<>(CtInvocation.class))
                    .forEach(invocations::add);
        }

        return invocations;
    }

    /**
     * Check the arguments of the given invocations and search for class names and return all found fully qualified class names.
     *
     * @param invocations a list of spoon invocations
     * @return list of fully qualified class names
     */
    private Set<String> getInvokedClassArguments(Set<CtInvocation<?>> invocations) {
        var list = new HashSet<String>();

        for (var invocation : invocations) {
            for (var argument : invocation.getArguments()) {
                if (argument.toString().toLowerCase().endsWith(".class\"")) {
                    var className = argument.toString()
                            .replaceFirst("\"", "")
                            .replaceAll("\"$", "")
                            .replaceAll("\\.class$", "");
                    list.add(className);
                }
            }
        }

        return list;
    }
}
