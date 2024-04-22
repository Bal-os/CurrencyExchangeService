package os.balashov.currencyexchangeservice.infrastructure.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractRateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "currency_code", nullable = false)
    private String numberCode;

    @Column(name = "currency_sign", nullable = false)
    private String code;

    @Column(name = "currency_name", nullable = false)
    private String name;

    @Column(name = "currency_rate", nullable = false)
    private BigDecimal rate;

    @Column(name = "time", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "rate_date", nullable = false)
    private LocalDate date;

    public AbstractRateEntity(AbstractRateEntity entity) {
        this.id = entity.getId();
        this.numberCode = entity.getNumberCode();
        this.code = entity.getCode();
        this.name = entity.getName();
        this.rate = entity.getRate();
        this.timestamp = entity.getTimestamp();
        this.date = entity.getDate();
    }
}
