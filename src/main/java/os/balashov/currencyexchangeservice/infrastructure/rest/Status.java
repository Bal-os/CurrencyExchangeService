package os.balashov.currencyexchangeservice.infrastructure.rest;

import lombok.Getter;

@Getter
public enum Status {
    SUCCESS("succes"), ERROR("error");

    private final String status;

    Status(String status) {
        this.status = status;
    }
}
