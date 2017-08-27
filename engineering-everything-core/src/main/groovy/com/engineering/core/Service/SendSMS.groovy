package com.engineering.core.Service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 * Created by GnyaniMac on 26/08/17.
 */

@Component
class SendSMS {

    @Value('${textlocal.api.key}')
    private String apikey;

    public String sendSms(String number, int otp) {
        try {
            // Construct data
            String apiKey = "apikey=${apikey}";
            String message = "&message=Your otp for registering into StudentAdda is ${otp}";
            String sender = "&sender=" + "TXTLCL";
            String numbers = "&numbers=${number}";

            println("apikey ${apiKey}")

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
            String data = apiKey + numbers + message + sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }
            rd.close();

            return stringBuffer.toString();
        } catch (Exception e) {
            System.out.println("Error SMS "+e);
            return "Error "+e;
        }
    }
}
