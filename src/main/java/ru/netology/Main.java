package ru.netology;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileOutputStream;
import java.io.IOException;


public class Main {

    public static String url = "https://api.nasa.gov/planetary/apod?api_key=ZJMYkb3480VVPPSGdmXQz6wsCuXtMcZbiyOUvcpa";
    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(request);

        FileInfo f = mapper.readValue(response.getEntity().getContent(), new TypeReference<>() {
                }
        );

        System.out.println(f);
        String pictureUrl = f.getUrl();
        int lastIndex = pictureUrl.lastIndexOf("/");
        String name = pictureUrl.substring(lastIndex + 1);
        System.out.println(name);

        HttpGet requestTwo = new HttpGet(pictureUrl);
        CloseableHttpResponse responseTwo = httpClient.execute(requestTwo);

        byte[] a = responseTwo.getEntity().getContent().readAllBytes();

        try (FileOutputStream fos = new FileOutputStream("D:\\Java Project\\HTTP\\2\\" + name)) {
            fos.write(a, 0, a.length);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
