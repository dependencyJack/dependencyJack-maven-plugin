package io.github.dependencyjack.analyze;

import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtImport;
import spoon.reflect.declaration.CtImportKind;
import spoon.reflect.declaration.CtType;
import spoon.reflect.visitor.ImportScannerImpl;

import java.util.HashSet;
import java.util.Set;

/**
 * Finds dependencies in a model based on imports
 */
public class ImportAnalyzer implements DependencyAnalyzer {

    private CtModel model;

    @Override
    public void setModel(CtModel model) {
        this.model = model;
    }

    @Override
    public Set<String> getDependencies() {
        var imports = getAllImports();
        return convertToImportStrings(imports);
    }

    /**
     * Get a set of all imports in the model
     */
    private Set<CtImport> getAllImports() {
        var imports = new HashSet<CtImport>();

        if (model == null) {
            throw new IllegalArgumentException("Model was not set");
        }

        // A "CtType" in spoon is just a supertype for classes and interfaces
        for (var cl : model.getAllTypes()) {
            var classImports = getImportsForClass(cl);
            imports.addAll(classImports);
        }

        return imports;
    }

    /**
     * Gets a set of imports that belong to a specific CtType
     *
     * @param cl Represents a class or interface in spoon
     * @return the set of imports for this CtType
     */
    private Set<CtImport> getImportsForClass(CtType<?> cl) {
        var importContext = new ImportScannerImpl();
        importContext.computeImports(cl);
        return importContext.getAllImports();
    }

    /**
     * Converts a set of imports to java fully qualified class names
     *
     * @return a list of fully qualified class names
     */
    private Set<String> convertToImportStrings(Set<CtImport> imports) {
        var strings = new HashSet<String>();

        for (var imp : imports) {
            var importedLibName = imp.getReference().toString();
            if (importedLibName.contains(".")) {
                if (imp.getImportKind() == CtImportKind.METHOD
                        || imp.getImportKind() == CtImportKind.ALL_TYPES
                        || imp.getImportKind() == CtImportKind.TYPE) {
                    strings.add(importedLibName);
                }
            }
        }

        return strings;
    }

}
