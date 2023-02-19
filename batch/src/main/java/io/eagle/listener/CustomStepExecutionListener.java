package io.eagle.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class CustomStepExecutionListener implements StepExecutionListener {
    private final Logger logger = LoggerFactory.getLogger(CustomStepExecutionListener.class);

    @Override
    public void beforeStep(StepExecution stepExecution) {
        String stepName = stepExecution.getStepName();
        logger.info("stepName = " + stepName + " start");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        String stepName = stepExecution.getStepName();
        ExitStatus exitStatus = stepExecution.getExitStatus();
        logger.info("stepName = " + stepName + " end " + " exitStatus : " + exitStatus);
        return ExitStatus.COMPLETED;
    }
}
