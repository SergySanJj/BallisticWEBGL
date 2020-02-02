package com.sergeiyarema.ballistics;

import org.junit.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

interface ResponseChecker {
    void run(HttpResponse<String> response);
}

public class HttpEndpointTests {
    private static int port = ServerConfig.httpPort;
    private static String baseAddress = "http://localhost:" + Integer.toString(port) + "/";

    private HttpClient httpClient;

    @BeforeClass
    public static void startServer() throws Exception {
        HttpServer.createNewServerThread(port).start();
    }

    @Before
    public void setUp() {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
    }


    @Test
    public void checkExistingHtmlPages() {
        ResponseChecker responseChecker = (HttpResponse<String> res) -> {
            statusOkCheck(res);
            bodyNotEmpty(res);
        };
        // index
        checkHtmlEndpoint("", responseChecker);
        // index
        checkHtmlEndpoint(HttpHandler.DEFAULT_FILE, responseChecker);
        // error page
        checkHtmlEndpoint(HttpHandler.FILE_NOT_FOUND, responseChecker);
        // file sent if requested not existing file
        checkHtmlEndpoint(HttpHandler.METHOD_NOT_SUPPORTED, responseChecker);
    }

    @Test
    public void checkNotExistingHtmlPages() {
        ResponseChecker responseChecker = (HttpResponse<String> res) -> {
            statusNotExistingCheck(res);
            bodyNotEmpty(res);
        };
        // Not valid endpoints
        checkHtmlEndpoint("notExistingEndpoint", responseChecker);
        checkHtmlEndpoint("someNotExistingPage", responseChecker);
    }

    @Test
    public void checkExistingMiscFiles() {
        ResponseChecker responseChecker = (HttpResponse<String> res) -> {
            statusOkCheck(res);
            bodyNotEmpty(res);
        };
        checkEndpoint("bundle.js", responseChecker,"text/javascript");
        checkEndpoint("css/styles.css", responseChecker,"text/css");
        checkEndpoint("favicon.ico", responseChecker,"text/plain");
    }

    @Test
    public void checkNotExistingMiscFiles() {
        ResponseChecker responseChecker = (HttpResponse<String> res) -> {
            statusNotExistingCheck(res);
            bodyNotEmpty(res);
        };
        checkEndpoint("notCorrect.notcorrect", responseChecker,"text/html");
    }


    public void checkHtmlEndpoint(String endpoint, ResponseChecker responseChecker){
        checkEndpoint(endpoint,responseChecker,"text/html");
    }

    public void checkEndpoint(String endpoint, ResponseChecker responseChecker, String mimeType) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(baseAddress + endpoint))
                .setHeader("User-Agent", "Test Request")
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            contentTypeCheck(response, mimeType);
            responseChecker.run(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void statusOkCheck(HttpResponse<String> response) {
        Assert.assertEquals(200, response.statusCode());
    }

    public static void statusNotExistingCheck(HttpResponse<String> response) {
        Assert.assertEquals(404, response.statusCode());
    }

    public static void contentTypeCheck(HttpResponse<String> response, String type) {
        Assert.assertEquals(type, response.headers().allValues("content-type").get(0));
    }

    public static void bodyNotEmpty(HttpResponse<String> response) {
        Assert.assertNotNull(response.body());
        Assert.assertNotEquals("", response.body());
    }
}