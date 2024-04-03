package os.balashov.currencyexchangeservice.infrastructure.rest.enums;

import lombok.Getter;

@Getter
public enum StatusEnum {
    SUCCESS("succes"), ERROR("error");

    private final String status;

    StatusEnum(String status) {
        this.status = status;
    }
}
