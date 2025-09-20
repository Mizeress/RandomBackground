package com.mizeress.imagegeneration;

import java.awt.Image;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.imageio.ImageIO;

import com.mizeress.config.Config;

// Class to get a random image from an online API
public class APIImageSource implements ImageSource {
    // Implementation to get an image from an online API
    @Override
    public Image getImage() {
        Config config = new Config();
        String apiUrl = config.getProperty("imagePath");
        URI uri = URI.create(apiUrl);
        HttpClient httpClient = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        
        System.out.println("Fetching image from API: " + apiUrl);
        System.out.println("HTTP Request: " + request);

        try {
            HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());

            if (response.statusCode() == 200) {
                byte[] imageData = response.body();
                Image image = ImageIO.read(new java.io.ByteArrayInputStream(imageData));
                
                return image;
            } else {
                System.err.println("Failed to fetch image URL. Status code: " + response.statusCode());
            }


        } catch (Exception e) {
            System.out.println("Error fetching image from API: " + e.getMessage());
        }
        
        // Return null if unable to fetch image
        return null;

    }

}
