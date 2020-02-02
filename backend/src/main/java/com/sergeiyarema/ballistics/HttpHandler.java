package com.sergeiyarema.ballistics;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpHandler implements Runnable {
    private static final File WEB_ROOT = new File("../frontend/dist/");
    public static final String DEFAULT_FILE = "index.html";
    public static final String FILE_NOT_FOUND = "404.html";
    public static final String METHOD_NOT_SUPPORTED = "not_supported.html";

    private static final boolean verbose = true;

    private Socket connect;

    HttpHandler(Socket c) {
        connect = c;
    }

    private Logger logger = Logger.getLogger("http server");

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        BufferedOutputStream dataOut = null;
        String fileRequested = null;

        try {
            in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            out = new PrintWriter(connect.getOutputStream());
            dataOut = new BufferedOutputStream(connect.getOutputStream());

            String input = in.readLine();

            StringTokenizer parse = new StringTokenizer(input);
            String HTTPmethod = parse.nextToken().toUpperCase();
            fileRequested = parse.nextToken().toLowerCase();

            if (!HTTPmethod.equals("GET") && !HTTPmethod.equals("HEAD")) {
                if (verbose) {
                    logger.log(Level.WARNING, String.format("501 Not Implemented : %s method.", HTTPmethod));
                }

                File file = new File(WEB_ROOT, METHOD_NOT_SUPPORTED);
                int fileLength = (int) file.length();
                String contentMimeType = "text/html";
                byte[] fileData = readFileData(file, fileLength);

                applyHeaders(out, fileLength, contentMimeType, "HTTP/1.1 501 Not Implemented");
                dataOut.write(fileData, 0, fileLength);
                dataOut.flush();

            } else {
                if (fileRequested.endsWith("/")) {
                    fileRequested += DEFAULT_FILE;
                }

                File file = new File(WEB_ROOT, fileRequested);
                int fileLength = (int) file.length();
                String content = getContentType(fileRequested);

                if (HTTPmethod.equals("GET")) { // GET method so we return content
                    byte[] fileData = readFileData(file, fileLength);

                    applyHeaders(out, fileLength, content, "HTTP/1.1 200 OK");
                    dataOut.write(fileData, 0, fileLength);
                    dataOut.flush();
                }
            }

        } catch (FileNotFoundException fnfe) {
            try {
                fileNotFound(out, dataOut, fileRequested);
            } catch (IOException ioe) {
                logger.log(Level.SEVERE, "Error with file not found exception : " + ioe.getMessage());
            }

        } catch (IOException ioe) {
            logger.log(Level.SEVERE, "Server error : " + ioe);
        } finally {
            try {
                in.close();
                out.close();
                dataOut.close();
                connect.close();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error closing stream : " + e.getMessage());
            }

            if (verbose) {
                logger.log(Level.INFO, "Connection closed.\n");
            }
        }

    }

    private static void applyHeaders(PrintWriter out, int fileLength, String contentMimeType, String status) {
        out.println(status);
        out.println("Server: Java HTTP Server cr: Sergei Yarema --v=1.0");
        out.println("Date: " + new Date());
        out.println("Content-type: " + contentMimeType);
        out.println("Content-length: " + fileLength);

        out.println();
        out.flush();
    }

    private byte[] readFileData(File file, int fileLength) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];

        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null)
                fileIn.close();
        }

        return fileData;
    }

    // MIME Types
    private String getContentType(String fileRequested) {
        if (fileRequested.endsWith(".htm") || fileRequested.endsWith(".html"))
            return "text/html";
        else if (fileRequested.endsWith(".css"))
            return "text/css";
        else if (fileRequested.endsWith(".js"))
            return "text/javascript";
        else
            return "text/plain";
    }

    private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
        File file = new File(WEB_ROOT, FILE_NOT_FOUND);
        int fileLength = (int) file.length();
        String content = "text/html";
        byte[] fileData = readFileData(file, fileLength);

        applyHeaders(out, fileLength, content, "HTTP/1.1 404 File Not Found");
        dataOut.write(fileData, 0, fileLength);
        dataOut.flush();

        if (verbose) {
            logger.log(Level.INFO, String.format("File %s not found", fileRequested));
        }
    }

}
