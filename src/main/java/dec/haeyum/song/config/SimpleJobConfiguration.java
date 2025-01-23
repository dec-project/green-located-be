package dec.haeyum.song.config;

import dec.haeyum.song.dto.CalendarSongCsvDto;
import dec.haeyum.song.dto.SongDetailCsvDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.transform.FlatFileFormatException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class SimpleJobConfiguration {

    private final SongDetailCsvReader songDetailCsvReader;
    private final SongDetailCsvScheduleWriter songDetailCsvScheduleWriter;

    private final CalendarSongCsvReader calendarSongCsvReader;
    private final CalendarSongCsvScheduleWriter calendarSongCsvScheduleWriter;

    @Bean
    public Job sequentialJob(JobRepository jobRepository, Flow jobFlow) {
        // Flow를 Job으로 연결
        return new JobBuilder("SequentialJob", jobRepository)
                .start(jobFlow) // Flow 실행
                .end()          // Job 종료
                .build();
    }

    @Bean
    public Flow jobFlow(@Qualifier("SongDetailDataLoadStep") Step songDetailDataLoadStep, @Qualifier("CalendarSongDataLoadStep") Step calendarSongDataLoadStep) {
        // Flow 내부에서 Step 순서 정의
        return new FlowBuilder<SimpleFlow>("JobFlow")
                .start(songDetailDataLoadStep)      // 첫 번째 Step
                .next(calendarSongDataLoadStep)    // 두 번째 Step
                .build();
    }
/*
    @Bean
    public Job SongDetailDataLoadJob(JobRepository jobRepository, @Qualifier("SongDetailDataLoadStep") Step songDetailDataLoadStep) {
        return new JobBuilder("SongDetailDataLoadJob", jobRepository)
                .start(songDetailDataLoadStep).build();
    }


 */
    @Bean
    public Step SongDetailDataLoadStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("SongDetailDataLoadStep", jobRepository)
                .<SongDetailCsvDto, SongDetailCsvDto>chunk(100, platformTransactionManager)
                .reader(songDetailCsvReader.csvScheduleReader())
                .writer(songDetailCsvScheduleWriter)
                .build();
    }
/*
    @Bean
    public Job CalendarSongDataLoadJob(JobRepository jobRepository, @Qualifier("CalendarSongDataLoadStep") Step calendarSongDataLoadStep) {
        return new JobBuilder("CalendarSongDataLoadJob", jobRepository)
                .start(calendarSongDataLoadStep).build();
    }


 */
    @Bean
    public Step CalendarSongDataLoadStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("CalendarSongDataLoadStep", jobRepository)
                .<CalendarSongCsvDto, CalendarSongCsvDto>chunk(100, platformTransactionManager)
                .reader(calendarSongCsvReader.csvScheduleReader())
                .writer(calendarSongCsvScheduleWriter)
                .build();
    }

}
