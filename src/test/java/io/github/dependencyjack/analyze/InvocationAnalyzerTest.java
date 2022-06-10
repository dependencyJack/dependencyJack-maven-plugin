package io.github.dependencyjack.analyze;

import org.junit.Test;
import spoon.Launcher;
import spoon.reflect.CtModel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class InvocationAnalyzerTest {

    @Test
    public void executeFindInvocation1() throws IOException {

        //setup actual
        Launcher launcher = new Launcher();
        launcher.getEnvironment().setNoClasspath(true);
        launcher.addInputResource("src/test/resources/A2.java");
        launcher.buildModel();

        CtModel model = launcher.getModel();

        DependencyAnalyzer analyzer = new InvocationArgumentAnalyzer();
        analyzer.setModel(model);
        var actualSet = analyzer.getDependencies();

        //setup ecpected
        Set<String> expectedSet = new HashSet<>();
        BufferedReader br = new BufferedReader(new FileReader("src/test/resources/invocationset"));
        String line;
        while ((line = br.readLine()) != null)
        {
            expectedSet.add(line);
        }

        assertEquals(expectedSet, actualSet);

    }
}