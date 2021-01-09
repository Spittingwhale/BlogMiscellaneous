package com.czj.exception.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.boot.diagnostics.FailureAnalysisReporter;

public class FailureAnalysisReporterDefinition implements FailureAnalysisReporter {
    private static final Log logger = LogFactory.getLog(FailureAnalysisReporterDefinition.class);

    @Override
    public void report(FailureAnalysis analysis) {
        logger.error("***********************************************************");
        logger.info(analysis.toString());
    }
}
