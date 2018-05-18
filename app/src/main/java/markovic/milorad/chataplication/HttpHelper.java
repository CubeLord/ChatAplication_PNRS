package markovic.milorad.chataplication;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Handler;

import markovic.milorad.chataplication.ContactsActivityPackage.Contact;

public class HttpHelper {

    private static final int SUCCESS = 200;
    HttpHelperReturn httpHelperReturn;

    /*HTTP get json Array*/

    public JSONArray getJSONContactsFromURL(String urlString, String sessionid) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        /*header fields*/
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Accept", "contacts/json");
        urlConnection.setRequestProperty("sessionid", sessionid);
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);
        try {
            urlConnection.connect();
        } catch (IOException e) {
            return null;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
            Log.d("Debugging", line);
        }
        br.close();

        String jsonString = sb.toString();
        int responseCode = urlConnection.getResponseCode();
        urlConnection.disconnect();
        return responseCode == SUCCESS ? new JSONArray(jsonString) : null;
    }


    public JSONArray getJSONArrayFromURL(String urlString) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        /*header fields*/
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);
        try {
            urlConnection.connect();
        } catch (IOException e) {
            return null;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();
        String jsonString = sb.toString();
        Log.d("HTTP GET", "JSON data- " + jsonString);
        int responseCode = urlConnection.getResponseCode();
        urlConnection.disconnect();
        return responseCode == SUCCESS ? new JSONArray(jsonString) : null;
    }

    /*HTTP get json object*/
    public JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        /*header fields*/
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);
        try {
            urlConnection.connect();
        } catch (IOException e) {
            return null;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();

        String jsonString = sb.toString();
        Log.d("HTTP GET", "JSON obj- " + jsonString);
        int responseCode = urlConnection.getResponseCode();
        urlConnection.disconnect();
        return responseCode == SUCCESS ? new JSONObject(jsonString) : null;
    }

    public HttpHelperReturn postJSONObjectFromURL(String urlString, JSONObject jsonObject, Context context) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept", "application/json");
        /* needed when used POST or PUT methods */
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        try {
            urlConnection.connect();
        } catch (IOException e) {
            Log.d("Debugging", "IOException in postJSONObjectFromURL");
            httpHelperReturn = new HttpHelperReturn();
            httpHelperReturn.setSuccess(false);
            return httpHelperReturn;
        }

        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
        /* write json object */
        os.writeBytes(jsonObject.toString());
        os.flush();
        os.close();

//        Toast.makeText(context, urlConnection.getResponseMessage(), Toast.LENGTH_LONG).show();

        int responseCode = urlConnection.getResponseCode();
        Log.i("STATUS", String.valueOf(urlConnection.getResponseMessage()));
        Log.i("MSG", urlConnection.getResponseMessage());
        urlConnection.disconnect();
        httpHelperReturn = new HttpHelperReturn();
        httpHelperReturn.setCode(urlConnection.getResponseCode());
        httpHelperReturn.setMessage(urlConnection.getResponseMessage());
        httpHelperReturn.setSuccess(responseCode == SUCCESS);
        httpHelperReturn.setSessionid(urlConnection.getHeaderField("sessionid"));
        return (httpHelperReturn);
    }


    public HttpHelperReturn postlogoutJSONObjectFromURL(String urlString, String sessionid, Context context) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("sessionid", sessionid);
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept", "application/json");
        /* needed when used POST or PUT methods */
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        try {
            urlConnection.connect();
        } catch (IOException e) {
            Log.d("Debugging", "IOException in postJSONObjectFromURL");
            httpHelperReturn = new HttpHelperReturn();
            httpHelperReturn.setSuccess(false);
            return httpHelperReturn;
        }

        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
        /* write json object */
        os.flush();
        os.close();

//        Toast.makeText(context, urlConnection.getResponseMessage(), Toast.LENGTH_LONG).show();

        int responseCode = urlConnection.getResponseCode();
        Log.i("STATUS", String.valueOf(urlConnection.getResponseMessage()));
        Log.i("MSG", urlConnection.getResponseMessage());
        urlConnection.disconnect();
        httpHelperReturn = new HttpHelperReturn();
        httpHelperReturn.setCode(urlConnection.getResponseCode());
        httpHelperReturn.setMessage(urlConnection.getResponseMessage());
        httpHelperReturn.setSuccess(responseCode == SUCCESS);
        httpHelperReturn.setSessionid(urlConnection.getHeaderField("sessionid"));
        return (httpHelperReturn);
    }

    public HttpHelperReturn postMessageJSONObjectFromURL(String urlString, JSONObject jsonObject, String sessionid) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("sessionid", sessionid);
        urlConnection.setRequestProperty("Accept", "application/json");
        /* needed when used POST or PUT methods */
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        try {
            urlConnection.connect();
        } catch (IOException e) {
            Log.d("Debugging", "IOException in postJSONObjectFromURL");
            httpHelperReturn = new HttpHelperReturn();
            httpHelperReturn.setSuccess(false);
            return httpHelperReturn;
        }

        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
        /* write json object */
        os.writeBytes(jsonObject.toString());
        os.flush();
        os.close();
        int responseCode = urlConnection.getResponseCode();
        Log.d("Debugging", Integer.toString(urlConnection.getResponseCode()));
        Log.i("STATUS", String.valueOf(urlConnection.getResponseMessage()));
        Log.i("MSG", urlConnection.getResponseMessage());
        urlConnection.disconnect();
        httpHelperReturn = new HttpHelperReturn();
        httpHelperReturn.setCode(urlConnection.getResponseCode());
        httpHelperReturn.setMessage(urlConnection.getResponseMessage());
        httpHelperReturn.setSuccess(responseCode == SUCCESS);
        httpHelperReturn.setSessionid(urlConnection.getHeaderField("sessionid"));
        return (httpHelperReturn);
    }


    public JSONArray getJSONMessagesFromURL(String urlString, String sessionid) throws IOException, JSONException {
        Log.d("Debugging", "Entered Messages method 0");
        HttpURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        Log.d("Debugging", "Entered Messages method 1");
        /*header fields*/
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Accept", "contacts/json");
        urlConnection.setRequestProperty("sessionid", sessionid);
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);
        Log.d("Debugging", "Entered Messages method 2");
        try {
            urlConnection.connect();
        } catch (IOException e) {
            return null;
        }

//        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while ((line = br.readLine()) != null) {
//            sb.append(line + "\n");
//            Log.d("Debugging", line);
//        }
//        br.close();

//        String jsonString = sb.toString();
        int responseCode = urlConnection.getResponseCode();
        urlConnection.disconnect();
        return responseCode == SUCCESS ? null : null;
    }


    /*HTTP delete*/
    public boolean httpDelete(String urlString) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("DELETE");
        urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        urlConnection.setRequestProperty("Accept", "application/json");
        try {
            urlConnection.connect();
        } catch (IOException e) {
            return false;
        }
        int responseCode = urlConnection.getResponseCode();

        Log.i("STATUS", String.valueOf(responseCode));
        Log.i("MSG", urlConnection.getResponseMessage());
        urlConnection.disconnect();
        return (responseCode == SUCCESS);
    }
}
