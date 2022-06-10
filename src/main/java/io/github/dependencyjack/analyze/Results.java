package io.github.dependencyjack.analyze;

import org.apache.maven.artifact.Artifact;

import java.util.Objects;
import java.util.Set;

/**
 * Stores the results of the project checker
 *
 * @param usedArtifacts   Artifacts that are used. Cant be null
 * @param unusedArtifacts Artifacts that are not used. Cant be null
 */
public record Results(
        Set<Artifact> usedArtifacts,
        Set<Artifact> unusedArtifacts
) {
    public Results {
        Objects.requireNonNull(usedArtifacts);
        Objects.requireNonNull(unusedArtifacts);
    }
}
