package org.effective.mobile.core.config.access;

import org.effective.mobile.core.repository.*;
import org.effective.mobile.core.service.task.ChainTaskFilterParams;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcConfig {
    @Bean
    public TaskRepository taskRepository(
            JdbcTemplate jdbcTemplate,
            ChainTaskFilterParams chainTaskFilterParams
    ) {
        return new TaskRepositoryJdbc(jdbcTemplate, chainTaskFilterParams);
    }

    @Bean
    public UserRepository userRepository(JdbcTemplate jdbcTemplate) {
        return new UserRepositoryJdbc(jdbcTemplate);
    }

    @Bean
    public CommentRepository commentRepository(JdbcTemplate jdbcTemplate) {
        return new CommentRepositoryJdbc(jdbcTemplate);
    }
}
