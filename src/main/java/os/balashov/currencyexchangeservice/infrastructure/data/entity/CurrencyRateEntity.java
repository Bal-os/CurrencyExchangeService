package os.balashov.currencyexchangeservice.infrastructure.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "currency_rate_cache")
public class CurrencyRateEntity extends AbstractRateEntity {
    public CurrencyRateEntity(AbstractRateEntity entity) {
        super(entity);
    }
}
