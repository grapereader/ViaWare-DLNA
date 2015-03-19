package ca.viaware.dlna;

import ca.viaware.api.logging.Log;
import ca.viaware.dlna.library.Library;
import ca.viaware.dlna.library.model.LibraryFactory;
import ca.viaware.dlna.settings.SettingsManager;
import ca.viaware.dlna.upnp.http.UpnpHttpServer;
import ca.viaware.dlna.ssdp.SSDPService;
import ca.viaware.dlna.upnp.device.DeviceManager;
import ca.viaware.dlna.upnp.device.devices.MediaServer;

import java.io.File;
import java.io.IOException;

public class ViaWareDLNA {

    public static final String VERSION = "0.0.1";

    public static void main(String[] args) throws IOException {
        Log.info("Starting ViaWare UPnP Server v" + VERSION);

        SettingsManager.loadSettings();
        LibraryFactory factory = Library.getFactory();
        factory.deleteAll();
        factory.addRootFolder(new File("testfiles/testlib/music"), "Music");
        factory.addRootFolder(new File("testfiles/testlib/tv"), "TV");
        //factory.addRootFolder(new File("G:/UserData/Music/YoutubePlaylists"), "Youtube Playlists");

        DeviceManager.registerDevice(new MediaServer());

        UpnpHttpServer http = new UpnpHttpServer();
        http.start();

        SSDPService ssdp = new SSDPService();
        ssdp.start();
    }

}