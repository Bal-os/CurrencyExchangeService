package os.balashov.currencyexchangeservice.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Currency {
    private String numberCode;
    private String currencyCode;
    private String name;
    private String description;
}
