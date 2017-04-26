package testreceiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

public class SigFoxRESTConnector {

	private HashMap<String, String> idMap = new HashMap<String, String>();
	String userPassword = "590085799058c204f6c19cc7" + ":" + "e9ed3a2921f0e1ec85dfce1c207ed1fa";
	
	public static void main(String[] args) {
		SigFoxRESTConnector s = new SigFoxRESTConnector();
		s.getIDs();
	}
	
	public void getIDs()  {
		URL url;
		try {
			
			
			url = new URL("https://backend.sigfox.com/api/devicetypes/");
			
			TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
			    public X509Certificate[] getAcceptedIssuers(){return null;}
			    public void checkClientTrusted(X509Certificate[] certs, String authType){}
			    public void checkServerTrusted(X509Certificate[] certs, String authType){}
			}};

			// Install the all-trusting trust manager
			try {
			    SSLContext sc = SSLContext.getInstance("TLS");
			    sc.init(null, trustAllCerts, new SecureRandom());
			    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			} catch (Exception e) {
			    ;
			}
			
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			String encoding = Base64.encodeBase64String(userPassword.getBytes());
			
			connection.setRequestProperty("Authorization", "Basic " + encoding);
		    // set connection output to true
		    connection.setDoOutput(true);
		    // instead of a GET, we're going to send using method="POST"
		    connection.setRequestMethod("GET");

		    // stuff in the first if block
		    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
		    	StringBuilder sb = new StringBuilder();
		    	BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
		    	while (br.ready()) {
		    		String x = br.readLine();
		    		sb.append(x);
		    		System.out.println(x);
		    	}
		    	
		    	JSONObject obj = new JSONObject(sb.toString());
				
		    	// id - name
		    	
				JSONArray jsonarr= obj.getJSONArray("data");
				for (int i = 0; i < jsonarr.length(); i++)
				{
					String id = jsonarr.getJSONObject(i).getString("id");
					String name = jsonarr.getJSONObject(i).getString("name");
					idMap.put(id, name);
				}
				
				Iterator it = idMap.entrySet().iterator();
				while (it.hasNext()) {
				        Map.Entry pair = (Map.Entry)it.next();
				        System.out.println(pair.getKey() + " = " + pair.getValue());
				        
				}
		    } else {
		    	String response = connection.getResponseMessage();
		    	System.out.println(response);
		    }
		
		    
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    
	}

}
