package com.finalproject.idlegame;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;

public class HttpURLConnectionActivity {

    private final static String TAG = "HttpURLConnectionActivity";
    private final static String REQUEST_METHOD_GET = "GET";

    private static String mParsedSegments[];

    public static String startSendHttpRequestThread(final String reqUrl) {
        //Starts thread to make a connection to a webserver so that we can make requests.
        Thread sendHttpRequestThread = new Thread() {
            @Override
            public void run() {
                // Handler for connection status
                HttpURLConnection httpConn = null;
                // Read text input stream.
                InputStreamReader isReader = null;
                // Read text into buffer.
                BufferedReader bufReader = null;
                // Save server response text.
                StringBuffer readTextBuf = new StringBuffer();

                try {
                    // Create a URL object use page url.
                    URL url = new URL(reqUrl);

                    // Open http connection to web server.
                    httpConn = (HttpURLConnection) url.openConnection();

                    // Set http request method to get.
                    httpConn.setRequestMethod(REQUEST_METHOD_GET);

                    // Set connection timeout and read timeout value.
                    httpConn.setConnectTimeout(10000);
                    httpConn.setReadTimeout(10000);

                    // Get input stream from web url connection.
                    InputStream inputStream = httpConn.getInputStream();

                    // Create input stream reader based on url connection input stream.
                    isReader = new InputStreamReader(inputStream);

                    // Create buffered reader.
                    bufReader = new BufferedReader(isReader);

                    // Read line of text from server response.
                    String line = bufReader.readLine();

                    // Loop while return line is not null.
                    while (line != null) {
                        if(line.contains("align=\"center\"")){
                            // Append the text to string buffer.
                            readTextBuf.append(line);
                        }
                        // Continue to read text line.
                        line = bufReader.readLine();
                    }

                    Log.d(TAG, "Full string" + readTextBuf.toString());
                    mParsedSegments = readTextBuf.toString().split(">");
                    for (String aux : mParsedSegments) {
                        Log.d(TAG, aux);
                    }

                } catch (MalformedURLException ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                } catch (IOException ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                } finally {
                    try {
                        if (bufReader != null) {
                            bufReader.close();
                            bufReader = null;
                        }

                        if (isReader != null) {
                            isReader.close();
                            isReader = null;
                        }

                        if (httpConn != null) {
                            httpConn.disconnect();
                            httpConn = null;
                        }
                    } catch (IOException ex) {
                        Log.e(TAG, ex.getMessage(), ex);
                    }
                }
            }
        };
        // Start the child thread to request web page.
        sendHttpRequestThread.start();
        return mParsedSegments[1];
    }
}
