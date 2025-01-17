package org.eclipse.tsp.java.client.api;

import lombok.Getter;

public abstract class AbstractTspApi {
    @Getter
    private String baseUrl;

    public AbstractTspApi(String baseUrl) {
        this.baseUrl = String.format("%s/tsp/api", baseUrl);
    }
}
