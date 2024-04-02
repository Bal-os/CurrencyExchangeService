package os.balashov.currencyexchangeservice.infrastructure.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "currency_rate_history", indexes = {
        @Index(name = "idx_date", columnList = "rate_date", unique = true)
})
public class CurrencyRateHistory extends AbstractRateEntity {
    public CurrencyRateHistory(AbstractRateEntity entity) {
        super(entity);
    }
}
