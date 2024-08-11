package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Component
public class StatsWebClient implements StatsClient {
    private final WebClient webClient;

    public StatsWebClient(@Value("${stat.server.url}") String uri) {
        this.webClient = WebClient.create(uri);
    }

    @Override
    public void saveHit(String app, String uri, String ip, String timestamp) {
        log.info("Stats Webclient saveHit start");

        webClient.post()
                .uri("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new EndpointHit(app, uri, ip, timestamp))
                .retrieve()
                .toBodilessEntity()
                .block();

        log.info("Stats Webclient saveHit finish");
    }

    @Override
    public List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique) {
        StringBuilder uri = new StringBuilder("/stats?start=" + start + "&end=" + end);
        if (unique) {
            uri.append("&unique=true");
        }

        for (String u : uris) {
            uri.append("&uri=").append(u);
        }

        return webClient.get()
                .uri(uri.toString())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ViewStats>>() {
                })
                .block();
    }
}
