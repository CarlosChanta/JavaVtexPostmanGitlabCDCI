import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.json.JSONObject;
import org.apache.commons.codec.binary.Base64;


public class API_Read {

    public static void main(String[] args)
    {
        getFile();
    }

    public static void getFile()
    {
        String archive= "main-promart.css";
        String repopath= "dev%2Ffiles%2F";

        String file = Unirest.get("https://gitlab.com/api/v4/projects/12239356/repository/files/"+repopath+archive+"?ref=master")
                .header("PRIVATE-TOKEN","GVVGLD4z4w1vRJ24UYov")
                .asString()
                .getBody();

        JSONObject Jfile = new JSONObject(file);
        String text = Jfile.getString("content");

        byte[] valueDecoded = Base64.decodeBase64(text);
        String jsonLoadVtex = "{\"path\":\""+archive+"\",\"text\":\""+new String(valueDecoded)+"\"}";

        System.out.println(jsonLoadVtex);
    }

}
