package com.czj.exception.util;

import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.boot.diagnostics.FailureAnalyzer;

public class FailureAnalyzerDefinition implements FailureAnalyzer {
    @Override
    public FailureAnalysis analyze(Throwable failure) {

        return new FailureAnalysis("hello-failure", "blogMiscellaneous-exception", failure);
    }
}
