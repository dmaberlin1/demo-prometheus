package com.dmadev.demoPrometheus.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

import okhttp3.Request;
import okhttp3.Response;
import org.hawkular.agent.prometheus.PrometheusMetricsProcessor;
import org.hawkular.agent.prometheus.text.TextPrometheusMetricsProcessor;
import org.hawkular.agent.prometheus.types.Gauge;
import org.hawkular.agent.prometheus.types.Metric;
import org.hawkular.agent.prometheus.types.MetricFamily;
import org.hawkular.agent.prometheus.walkers.CollectorPrometheusMetricsWalker;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class PrometheusClient {
        OkHttpClient client = new OkHttpClient();
    private final static String url = "http://localhost:8080/actuator/prometheus?includedNames=metrics_query_row_count_gauge";

    public double getMetricRequest() throws IOException {
        Request request =new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        InputStream inputStream = new ByteArrayInputStream(responseBody.getBytes());
        CollectorPrometheusMetricsWalker walker = new CollectorPrometheusMetricsWalker();
        PrometheusMetricsProcessor<MetricFamily> processor = new TextPrometheusMetricsProcessor(inputStream, walker);
        processor.walk();
        List<MetricFamily> metricList = walker.getAllMetricFamilies();
        MetricFamily metricFamily = metricList.get(0);
        List<Metric> metrics = metricFamily.getMetrics();
        Gauge metric = (Gauge) metrics.get(0);
        double value = metric.getValue();


        return value;
    }


    //eof
}
