package org.effective.mobile.core.config;

import jakarta.validation.constraints.NotNull;
import org.effective.mobile.core.config.access.AccessCacheType;
import org.effective.mobile.core.config.access.AccessDatabaseType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
        @NotNull AccessDatabaseType databaseAccessDatabaseType,
        @NotNull AccessCacheType cacheType
        ) {
}
