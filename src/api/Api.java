/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import java.io.*;
import java.net.*;
import javax.xml.xpath.*;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.*;
import org.apache.http.entity.*;
import org.apache.http.impl.client.*;
import org.apache.http.util.*;
import org.xml.sax.*;
import sun.net.www.http.HttpClient;

...

/**
 *
 * @author salvagibert
 */
public class Api {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here


         String stubsApiBaseUri = "http://localhost:7819/RTCP/rest/stubs/";
         String domain = "default";
         String environment = "addNumbers";
         String stubName = "1+1=2";

         HttpClient client = HttpClients.createDefault();

         URIBuilder builder = new URIBuilder(stubsApiBaseUri);
         builder.addParameter("domain", domain);
         builder.addParameter("env", environment);
         builder.addParameter("stub", stubName);
         String listStubsUri = builder.build().toString();
         HttpGet getStubMethod = new HttpGet(listStubsUri);
         HttpResponse getStubResponse = client.execute(getStubMethod);
         int getStubStatusCode = getStubResponse.getStatusLine()
               .getStatusCode();
         if (getStubStatusCode < 200 || getStubStatusCode >= 300) {
            // Manejar código de estado no 2xx
            return;
         }
         String responseBody = EntityUtils
               .toString(getStubResponse.getEntity());
         // Se presuponen solo dos coincidencias de apéndice
         String stubRelativeUri = XPathFactory
               .newInstance()
               .newXPath()
               .evaluate("/stubs/stub/@href",
                     new InputSource(new StringReader(responseBody)));
         String stubAbsoluteUri = new URI(stubsApiBaseUri).resolve(
               stubRelativeUri).toString();

         HttpPost startStubMethod = new HttpPost(stubAbsoluteUri);
         ContentType contentType = ContentType.APPLICATION_XML
               .withCharset("utf-8");
         String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
               + "<start-stub />";
         startStubMethod.setEntity(new ByteArrayEntity(data
               .getBytes(contentType.getCharset()), contentType));
         HttpResponse startStubResponse = client.execute(startStubMethod);
         int startStubStatusCode = startStubResponse.getStatusLine()
               .getStatusCode();
         if (startStubStatusCode < 200 || startStubStatusCode >= 300) {
            // Manejar código de estado no 2xx
            return;
         }
         // Si dese comprobar el estado del apéndice que se está iniciando,
         // puede utilizar los datos de respuesta para obtener el URI de la
         // instancia de apéndice y sondear si hay actualizaciones
         System.out.println(startStubStatusCode);
         String startStubResponseBody = EntityUtils.toString(startStubResponse
               .getEntity());
         System.out.println(startStubResponseBody);

      } catch (URISyntaxException | IOException | XPathExpressionException e) {
         // Manejar errores
      }
    }
    
}
