package org.eclipse.tsp.java.client.api.annotation;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.tsp.java.client.core.tspclient.TspClientResponse;
import org.eclipse.tsp.java.client.shared.query.Query;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;
import org.eclipse.tsp.java.client.shared.response.ResponseStatus;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;

@WireMockTest(httpPort = 8080)
public class AnnotationApiTest {

    private static final String FIXTURE_PATH = "fixtures/tspclient";
    private static final String TSP_EXTENSION_URL = "/tsp/api";

    private AnnotationApi annotationApi = new AnnotationApi("http://localhost:8080");

    @Test
    public void fetchAnnotationCategories() {
        final UUID experimentUuid = UUID.fromString("11111111-1111-1111-1111-111111111111");
        final String outputId = "11111111-1111-1111-1111-111111111111";
        final String targetUrl = String.format("%s/experiments/%s/outputs/%s/annotations", TSP_EXTENSION_URL,
                experimentUuid,
                outputId);
        stubFor(get(targetUrl).willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBodyFile(String.format("%s/fetch-annotation-categories-0.json", FIXTURE_PATH))));

        TspClientResponse<GenericResponse<AnnotationCategoriesModel>> response = this.annotationApi
                .getAnnotationsCategories(experimentUuid, outputId, Optional.empty());

        assertEquals(ResponseStatus.COMPLETED, response.getResponseModel().getStatus());
        assertEquals(1, response.getResponseModel().getModel().getAnnotationCategories().size());
        assertEquals(AnnotationCategoriesModel.class, response.getResponseModel().getModel().getClass());
    }

    @Test
    public void fetchAnnotationModel() {
        final UUID experimentUuid = UUID.fromString("11111111-1111-1111-1111-111111111111");
        final String outputId = "11111111-1111-1111-1111-111111111111";
        final String targetUrl = String.format("%s/experiments/%s/outputs/%s/annotations", TSP_EXTENSION_URL,
                experimentUuid,
                outputId);
        stubFor(post(targetUrl).willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBodyFile(String.format("%s/fetch-annotation-model-0.json", FIXTURE_PATH))));

        Map<String, Object> parameters = new HashMap<>();
        Query query = new Query(parameters);

        TspClientResponse<GenericResponse<AnnotationModel>> response = this.annotationApi.getAnnotations(experimentUuid,
                outputId, query);
        assertEquals(ResponseStatus.RUNNING, response.getResponseModel().getStatus());
        assertEquals(Annotation.class,
                response.getResponseModel().getModel().getAnnotations().get("Annotation category").get(0)
                        .getClass());
        assertEquals(1, response.getResponseModel().getModel().getAnnotations().get("Annotation category").size());
        assertEquals(new BigInteger("1111111111111111111"),
                response.getResponseModel().getModel().getAnnotations().get("Annotation category").get(0)
                        .getTime());
        assertEquals(AnnotationType.CHART,
                response.getResponseModel().getModel().getAnnotations().get("Annotation category").get(0)
                        .getType());

    }

}
