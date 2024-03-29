/*
 * Copyright 2015 Seth Traverse
 *
 * This file is part of ViaWare DLNA Server.
 *
 * ViaWare DLNA Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ViaWare DLNA Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ViaWare DLNA Server. If not, see <http://www.gnu.org/licenses/>.
 */

package ca.viaware.dlna.streamserver;

import ca.viaware.api.logging.Log;
import ca.viaware.api.utils.StringUtils;
import ca.viaware.dlna.Globals;
import ca.viaware.dlna.ViaWareDLNA;
import ca.viaware.dlna.library.Library;
import ca.viaware.dlna.library.model.LibraryEntry;
import ca.viaware.dlna.library.model.LibraryFactory;
import ca.viaware.dlna.library.model.LibraryInstanceRunner;
import ca.viaware.dlna.settings.SettingsManager;
import ca.viaware.dlna.util.HttpUtils;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONObject;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

public class StreamServer {

    private HttpServer server;

    public StreamServer() throws IOException {
        JSONObject config = SettingsManager.getServerConfig().getJSONObject("streamServer");
        this.server = HttpServer.create(new InetSocketAddress(config.getString("host"), config.getInt("port")), 8);
    }

    public void start() {

        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (exchange.getRequestMethod().equals("GET") || exchange.getRequestMethod().equals("HEAD")) {
                    streamMedia(exchange);
                } else {
                    Log.error("STREAM-SERVER: Got unknown request %0", exchange.getRequestMethod());
                }
            }
        });

        server.start();
        Log.info("Started stream HTTP server");
    }

    private void streamMedia(HttpExchange exchange) throws IOException {
        final int entryId = Integer.parseInt(StringUtils.cleanNumber(exchange.getRequestURI().getPath()));
        Log.info("STREAM-SERVER: Got request for library item %0", entryId);

        HttpUtils.emptyStream(exchange.getRequestBody());

        LibraryEntry entry = (LibraryEntry) Library.runInstance(new LibraryInstanceRunner() {
            @Override
            public Object run(LibraryFactory factory) {
                return factory.get(entryId);
            }
        });


        if (entry != null) {
            File file = entry.getLocation();
            String mime = Files.probeContentType(file.toPath().toAbsolutePath());
            Log.info("STREAM-SERVER: Entry is %0 %1", file.getAbsolutePath(), mime);

            Headers headers = exchange.getResponseHeaders();
            headers.set("CONTENT-TYPE", mime);
            headers.set("USER-AGENT", Globals.SERVER);
            headers.set("CONTENT-LANGUAGE", "en");

            if (exchange.getRequestMethod().equals("GET")) {
                Log.info("Starting stream upload.");
                exchange.sendResponseHeaders(200, 0);
                InputStream fileIn = new FileInputStream(file);
                OutputStream output = exchange.getResponseBody();
                byte[] buffer = new byte[1024];
                int read;
                while ((read = fileIn.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }
                output.close();
            } else {
                exchange.sendResponseHeaders(200, -1);
                exchange.getResponseBody().close();
                Log.info("Not streaming to HEAD request.");
            }
            Log.info("STREAM-SERVER: Finished stream transaction.");
        } else {
            String html = "";
            html += "<!DOCTYPE html><html>";
            html += "<head><title>ViaWare UPnP - Stream Server</title></head>";
            html += "<body>";
            html += "<h1>ViaWare UPnP Server v" + ViaWareDLNA.VERSION + " - Stream Server</h1><hr>";
            html += "Unable to find the specified stream<br>";
            html += "Copyright 2015 Seth Traverse";
            html += "</body></html>";
            byte[] bytes = html.getBytes("UTF-8");

            Headers headers = exchange.getResponseHeaders();
            headers.set("CONTENT-TYPE", "text/html");
            headers.set("CONTENT-LANGUAGE", "en");
            exchange.sendResponseHeaders(404, bytes.length);

            exchange.getResponseBody().write(bytes);
            exchange.getResponseBody().close();
        }
    }

}
