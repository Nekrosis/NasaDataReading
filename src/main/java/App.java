import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;

public class App {
    private final String URL = "https://api.nasa.gov/planetary/apod?api_key=YBqWT0YJFV82R6SiG27DMqjIM1SXCjuujjdXvNwX";
    private String image;

    public void nasa(CloseableHttpClient httpClient) {
        try {
            HttpGet get = new HttpGet(URL);
            CloseableHttpResponse response = httpClient.execute(get);
            ObjectMapper mapper = new ObjectMapper();
            NasaService nasaService = mapper.readValue(response.getEntity().getContent(), new TypeReference<NasaService>() {
            });
//TODO: Запись картинки->
            image = nasaService.getUrl();
            String[] fir = image.split("/");
            String name = fir[fir.length - 1];
            HttpGet get1 = new HttpGet(image);
            CloseableHttpResponse httpResponse = httpClient.execute(get1);
            File file = new File("/Users/macbookpro/Downloads", name);
            HttpEntity http = httpResponse.getEntity();
            InputStream inputStream = http.getContent();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] body = inputStream.readAllBytes();
            fos.write(body);
            fos.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
        App app = new App();
        app.nasa(httpClient);
    }
}
