package dec.haeyum.scheduler;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail jobDetail(){
        return JobBuilder.newJob(QuartzJob.class)
                .withIdentity("Calendar_Movie-init")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger jobTrigger(){
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail())
                .withIdentity("Calendar_Movie-initTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?")) // 매일 자정 업데이트
                .build();
    }


}
