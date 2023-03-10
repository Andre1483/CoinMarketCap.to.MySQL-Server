package coin.market.cap.api;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andre1483
 * @version 10032023.2248
 */
public class CoinMarketCapApi {
    
    private static final String apiKey = "paste apiKey here";  //paste apiKey here

    public static String JSON_String = "";

    public static void main(String[] args) {
        String uri = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        List<NameValuePair> paratmers = new ArrayList<NameValuePair>();
        paratmers.add(new BasicNameValuePair("start","1"));
        paratmers.add(new BasicNameValuePair("limit","2"));
        paratmers.add(new BasicNameValuePair("convert","EUR"));
    
        try {
          String result = makeAPICall(uri, paratmers);
          //JSON_String = result;
          System.out.println(formatJSONStr(result, 3));

        } catch (IOException e) {
          System.out.println("Error: cannont access content - " + e.toString());
        } catch (URISyntaxException e) {
          System.out.println("Error: Invalid URL " + e.toString());
        }
      }
    
      /**
       * get json as unformatted String
       * @param uri
       * @param parameters
       * @return    json unformatted
       * @throws URISyntaxException
       * @throws IOException
       */
      public static String makeAPICall(String uri, List<NameValuePair> parameters)
          throws URISyntaxException, IOException {
        String response_content = "";
    
        URIBuilder query = new URIBuilder(uri);
        query.addParameters(parameters);
    
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(query.build());
    
        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        request.addHeader("X-CMC_PRO_API_KEY", apiKey);
    
        CloseableHttpResponse response = client.execute(request);
    
        try {
          //System.out.println(response.getStatusLine());
          HttpEntity entity = response.getEntity();
          response_content = EntityUtils.toString(entity);
          EntityUtils.consume(entity);
        } finally {
          response.close();
        }
        
        JSON_String = response_content;

        return response_content;
      }
    
      /**
       * json-formatter
       * @param json_str
       * @param indent_width
       * @return  json formatted
       */
      public static String formatJSONStr(final String json_str, final int indent_width) {
        final char[] chars = json_str.toCharArray();
        final String newline = System.lineSeparator();
    
        String ret = "";
        boolean begin_quotes = false;
    
        for (int i = 0, indent = 0; i < chars.length; i++) {
            char c = chars[i];
    
            if (c == '\"') {
                ret += c;
                begin_quotes = !begin_quotes;
                continue;
            }
    
            if (!begin_quotes) {
                switch (c) {
                case '{':
                case '[':
                    ret += c + newline + String.format("%" + (indent += indent_width) + "s", "");
                    continue;
                case '}':
                case ']':
                    ret += newline + ((indent -= indent_width) > 0 ? String.format("%" + indent + "s", "") : "") + c;
                    continue;
                case ':':
                    ret += c + " ";
                    continue;
                case ',':
                    ret += c + newline + (indent > 0 ? String.format("%" + indent + "s", "") : "");
                    continue;
                default:
                    if (Character.isWhitespace(c)) continue;
                }
            }
    
            ret += c + (c == '\\' ? "" + chars[++i] : "");
        }
    
        return ret;
    }
}