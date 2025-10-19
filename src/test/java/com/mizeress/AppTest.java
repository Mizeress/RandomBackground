package com.mizeress;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

import com.mizeress.backgroundchange.BackgroundChanger;
import com.mizeress.backgroundchange.WindowsBackgroundChanger;
import com.mizeress.config.Config;
import com.mizeress.imagegeneration.APIImageSource;
import com.mizeress.imagegeneration.DirectoryImageSource;
import com.mizeress.imagegeneration.ImageSource;

/**
 * Tests for background change application
 */
public class AppTest 
{
    // TODO Fix these tests to use mocking to be more independent and unitary

    @Test
    public void configReadWrite()
    {
        Config config = new Config();
        String testKey = "testKey";
        String testValue = "testValue";
        config.setProperty(testKey, testValue);
        assertTrue( config.getProperty(testKey).equals(testValue) );
    }

    @Test
    public void ImageSourceResolve() {
        Config config = new Config();
        config.setProperty("imageSource", "directory");
        ImageSource imgSrc = ImageSource.create();
        assertTrue( imgSrc instanceof DirectoryImageSource );

        config.setProperty("imageSource", "API");
        imgSrc = ImageSource.create();
        assertTrue( imgSrc instanceof APIImageSource );
    }

    @Test
    public void getImageFromDirectory()
    {
        Config config = new Config();
        config.setProperty("imageSource", "directory");
        config.setProperty("imagePath", "C:\\Users\\Mizeress\\Pictures\\Wallpapers");
        ImageSource imgSrc = new DirectoryImageSource();
        assertTrue( imgSrc.getImage() != null );
    }

    @Test
    public void getImageFromAPI()
    {
        String apiURL = "https://picsum.photos/1920/1080";
        Config config = new Config();
        config.setProperty("imageSource", "API");
        config.setProperty("imagePath", apiURL);
        APIImageSource imgSrc = new APIImageSource();
        assertTrue( imgSrc.getImage() != null );
    }

    @Test
    public void changeBackgroundWindows()
    {
        BackgroundChanger bkgChanger = new WindowsBackgroundChanger();
        java.awt.Image image = new java.awt.image.BufferedImage(1920, 1080, java.awt.image.BufferedImage.TYPE_INT_RGB);
        bkgChanger.changeBackground(image);
        assertTrue( true ); // If no exception, test passed
    }

    /**
     * Test resolving BackgroundChanger based on OS
     * Currently only windows is supported
     */
    @Test
    public void resolveBackgroundChanger()
    {
        BackgroundChanger bkgChanger = BackgroundChanger.create();
        assertTrue( bkgChanger instanceof WindowsBackgroundChanger );
    }
}
