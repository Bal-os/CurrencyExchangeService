package os.balashov.currencyexchangeservice.application.dto;

import lombok.Getter;

@Getter
public enum DataSource {
    EXTERNAL("provider"),
    CACHE("repository");
    private final String name;

    DataSource(String name) {
        this.name = name;
    }
}
