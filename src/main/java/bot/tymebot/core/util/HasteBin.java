package bot.tymebot.core.util;

import org.jetbrains.annotations.NotNull;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HasteBin {
    String binURL = "https://bin.hypews.com/";

    /**
     * Create a new HasteBin instance with your custom HasteBin (optional)
     *
     * @param binURL The exact URL to your pastebin - for example <a href="https://bin.seailz.com/">...</a>
     */
    public HasteBin(@NotNull String binURL) {
        if (!binURL.endsWith("/")) binURL = binURL + "/";
        this.binURL = binURL;
    }

    public HasteBin() {
    }

    public String post(@NotNull String text, boolean raw) throws IOException {
        byte[] postData = text.getBytes(StandardCharsets.UTF_8);
        int postDataLength = postData.length;
        URL url = new URL(binURL + "documents");
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("User-Agent", "Hastebin Java Api");
        conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        conn.setUseCaches(false);
        String response = null;

        try {
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(postData);
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            response = reader.readLine();
        } catch (IOException var11) {
            var11.printStackTrace();
        }

        assert response != null;

        if (response.contains("\"key\"")) {
            response = response.substring(response.indexOf(":") + 2, response.length() - 2);
            String postURL = raw ? binURL + "raw/" : binURL;
            response = postURL + response;
        }

        return response;
    }
}

