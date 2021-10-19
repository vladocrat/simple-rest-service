package methods;

import lombok.Getter;

public enum Methods {
    POST ("POST"),
    GET ("GET");

    @Getter
    private final String value;

    Methods(String value) {
        this.value = value;
    }
}
