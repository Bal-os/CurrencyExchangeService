package os.balashov.currencyexchangeservice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Currency {
    private String numberCode;
    private String currencyCode;
    private String name;
    private String description;
}
