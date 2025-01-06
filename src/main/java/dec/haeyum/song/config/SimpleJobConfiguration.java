package dec.haeyum.song.config;

import dec.haeyum.song.dto.SongDetailCsvDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class SimpleJobConfiguration {

    private final SongDetailCsvReader songDetailCsvReader;
    private final SongDetailCsvScheduleWriter songDetailCsvScheduleWriter;

    @Bean
    public Job SongDetailDataLoadJob(JobRepository jobRepository, Step songDetailDataLoadJobStep) {
        return new JobBuilder("SongDetailDataLoadJob", jobRepository)
                .start(songDetailDataLoadJobStep).build();
    }

    @Bean
    public Step SongDetailDataLoadStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("SongDetailDataLoadStep", jobRepository)
                .<SongDetailCsvDto, SongDetailCsvDto>chunk(100, platformTransactionManager)
                .reader(songDetailCsvReader.csvScheduleReader())
                .writer(songDetailCsvScheduleWriter)
                .build();
    }

}
