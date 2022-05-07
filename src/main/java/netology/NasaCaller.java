package netology;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class NasaCaller {

    public static final String REMOTE_SERVICE_URI = "https://jsonplaceholder.typicode.com/posts";
    public static final ObjectMapper mapper = new ObjectMapper();

    private static String getNameFromUrl(String url){
        String[] urlParts =  url.split("/");
        return urlParts[urlParts.length-1];
    }

    public static void main(String[] args) throws IOException {
        System.out.println(System.getProperty("file.encoding"));

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();
        HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=VbFDChI0DM2LGeJR69KtmNWTWlgIXyy0G7xfUU7A");
        CloseableHttpResponse response = httpClient.execute(request);

        Arrays.stream(response.getAllHeaders()).forEach(System.out::println);
        NasaData today = mapper.readValue(response.getEntity().getContent(), new TypeReference<>() {});

        System.out.println();



        URL url = new URL(today.getUrl());
        InputStream in = new BufferedInputStream(url.openStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int n = 0;
        while (-1!=(n=in.read(buf)))
        {
            out.write(buf, 0, n);
        }
        out.close();
        in.close();
        byte[] attache = out.toByteArray();
        FileOutputStream fos = new FileOutputStream(getNameFromUrl(today.getUrl()));
        fos.write(attache);
        fos.close();

    }

}
