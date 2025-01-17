package org.eclipse.tsp.java.client.api.xy;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.tsp.java.client.api.AbstractTspApi;
import org.eclipse.tsp.java.client.core.restclient.RestClient;
import org.eclipse.tsp.java.client.core.tspclient.TspClientResponse;
import org.eclipse.tsp.java.client.shared.entry.Entry;
import org.eclipse.tsp.java.client.shared.entry.EntryModel;
import org.eclipse.tsp.java.client.shared.query.Query;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;

import com.fasterxml.jackson.core.type.TypeReference;

public class XyApi extends AbstractTspApi {
    private final String XY_API_URL = "%s/experiments/%s/outputs/XY/%s";

    public XyApi(String baseUrl) {
        super(baseUrl);
    }

    public TspClientResponse<GenericResponse<EntryModel<Entry>>> getXyTree(UUID experimentUuid, String outputId,
            Query query) {
        final TspClientResponse<String> tspClientResponse = RestClient.post(
                String.format(this.XY_API_URL.concat("/tree"), this.getBaseUrl(), experimentUuid, outputId),
                Optional.of(query), String.class);

        return TspClientResponse.getGenericResponse(tspClientResponse,
                new TypeReference<GenericResponse<EntryModel<Entry>>>() {
                });
    }

    public TspClientResponse<GenericResponse<XyModel>> getXy(UUID experimentUuid, String outputId,
            Query query) {
        final TspClientResponse<String> tspClientResponse = RestClient.post(
                String.format(this.XY_API_URL.concat("/xy"), this.getBaseUrl(), experimentUuid, outputId),
                Optional.of(query), String.class);

        return TspClientResponse.getGenericResponse(tspClientResponse, new TypeReference<GenericResponse<XyModel>>() {
        });
    }

    public TspClientResponse<GenericResponse<Map<String, String>>> getXyTooltip(UUID experimentUuid, String outputId,
            int xValue, Optional<Integer> yValue, Optional<String> seriesId) {
        Map<String, String> queryParameters = new HashMap<String, String>();
        if (yValue.isPresent()) {
            queryParameters.put("yValue", yValue.get().toString());
        }

        if (seriesId.isPresent()) {
            queryParameters.put("seriesId", seriesId.get().toString());
        }

        final TspClientResponse<String> tspClientResponse = RestClient.get(
                String.format(this.XY_API_URL.concat("/tooltip"), this.getBaseUrl(), experimentUuid, outputId),
                Optional.of(queryParameters), String.class);

        return TspClientResponse.getGenericResponse(tspClientResponse,
                new TypeReference<GenericResponse<Map<String, String>>>() {
                });
    }

}
