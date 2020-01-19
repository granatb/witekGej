package project.photo.boundary;


import lombok.RequiredArgsConstructor;
import lombok.var;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.photo.boundary.command.CreateNewPhotoCommand;
import project.photo.control.repository.PhotoRepository;
import project.photo.entity.PhotoEntity;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ExternalService {

    @PersistenceContext
    private EntityManager entityManager;
    private final PhotoRepository photoRepository;


    @Transactional
    public void test(CreateNewPhotoCommand command) throws MalformedURLException, ProtocolException, IOException {
        String link = command.getPathString();
        String myurl = String.join("", "http://localhost:5000", "/upload?path=", link);
        URL url = new URL(myurl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");




        Map<String, String> params = new HashMap<>();
        params.put("path", link);


        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, String> param : params.entrySet()) {
            if (postData.length() != 0) {
                postData.append('&');
            }
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }

        byte[] postDataBytes = postData.toString().getBytes("UTF-8");
        connection.setDoOutput(true);
        try (DataOutputStream writer = new DataOutputStream(connection.getOutputStream())) {
            writer.write(postDataBytes);
            writer.flush();
            writer.close();

            StringBuilder content;

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                String line;
                content = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }
            try {
                JSONObject jsonObject = new JSONObject(content.toString());
                Iterator<String> keys = jsonObject.keys();
                System.out.println(jsonObject.getClass().getName());
                System.out.println(keys.toString());
                for(int i = 0; i<jsonObject.names().length(); i++){
                    System.out.println(jsonObject.get(jsonObject.names().getString(i)));
                    String k = Integer.toString(i);
                    System.out.println(jsonObject.getJSONObject(k).getString("xsCoord"));
                    System.out.println(jsonObject.getJSONObject(k).getString("ysCoord"));
                    PhotoEntity photoEntity = PhotoEntity.builder()
                            .xsCoord(Double.parseDouble(jsonObject.getJSONObject(k).getString("xsCoord")))
                            .pathString(command.getPathString())
                            .ysCoord(Double.parseDouble(jsonObject.getJSONObject(k).getString("ysCoord")))
                            .build();
                    entityManager.persist(photoEntity);
                }
            }catch (JSONException err){
            }

        } finally {
            connection.disconnect();
        }
    }
}
