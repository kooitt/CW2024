package com.example.demo;

import javafx.scene.image.Image;

import java.net.URL;

public class ImageLoader {

    private static final String IMAGE_LOCATION = "/com/example/demo/images/";

    public static Image loadImage(String imageName){
        URL resource = ImageLoader.class.getResource(IMAGE_LOCATION + imageName);
        if (resource != null){
            return new Image(resource.toExternalForm());
        }else{
            System.err.println("Background image not found: " + imageName);
            return null;
        }
    }
}
