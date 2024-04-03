package os.balashov.currencyexchangeservice.infrastructure.data.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import os.balashov.currencyexchangeservice.infrastructure.data.entity.AbstractRateEntity;
import os.balashov.currencyexchangeservice.infrastructure.data.entity.CurrencyRateEntity;
import os.balashov.currencyexchangeservice.infrastructure.data.entity.CurrencyRateHistory;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class CurrencyRateMutationRepositoryImpl implements CurrencyRateMutationRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void copyDataFromActualTableToHistory() {
        String sourceTable = getTableName(CurrencyRateEntity.class);
        String destinationTable = getTableName(CurrencyRateHistory.class);
        String columnList = getColumnList(AbstractRateEntity.class);
        String sql = buildCopySql(sourceTable, destinationTable, columnList);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        jdbcTemplate.update(sql, parameters);
    }

    private String buildCopySql(String sourceTable, String destinationTable, String columnList) {
        String[] columns = columnList.split(", ");
        String whereClause = Arrays.stream(columns)
                .map(column -> column + " IS NOT NULL")
                .collect(Collectors.joining(" AND "));

        return "INSERT INTO " +
                destinationTable +
                " (" +
                columnList +
                ") " +
                "SELECT " +
                columnList +
                " FROM " +
                sourceTable +
                " WHERE " +
                whereClause;
    }


    private String getTableName(Class<?> entityClass) {
        Table tableAnnotation = entityClass.getAnnotation(Table.class);
        if (tableAnnotation != null) {
            return tableAnnotation.name();
        } else {
            throw new IllegalArgumentException("Entity class " + entityClass.getName() + " doesn't have a @Table annotation");
        }
    }

    private String getColumnList(Class<?> entityClass) {
        String columnAnnotations = Arrays.stream(entityClass.getDeclaredFields())
                .map(field -> field.getAnnotation(Column.class))
                .filter(Objects::nonNull)
                .map(Column::name)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(", "));
        if (!columnAnnotations.isEmpty()) {
            log.info("Column list for {} entity: {}", entityClass.getSimpleName(), columnAnnotations);
            return columnAnnotations;
        } else {
            throw new IllegalArgumentException("Entity class " + AbstractRateEntity.class.getName() + " doesn't have the @Column annotations");
        }
    }
}
