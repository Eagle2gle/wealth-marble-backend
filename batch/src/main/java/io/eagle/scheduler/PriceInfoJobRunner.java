package io.eagle.scheduler;

import lombok.RequiredArgsConstructor;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class PriceInfoJobRunner extends JobRunner{
    private final Scheduler scheduler;

    @Override
    protected void doRun(ApplicationArguments args) {
        JobDetail jobDetail = buildJobDetail(PriceInfoSchJob.class, "priceInfoJob", "batch", new HashMap());
        Trigger trigger = buildJobTrigger("0 0 0 * * ?");

        try{
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
