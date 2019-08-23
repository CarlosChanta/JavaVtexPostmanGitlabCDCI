import kong.unirest.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.commons.codec.binary.Base64;


public class API_Read {

    public static void main(String[] args)
    {
        String file= "main-promart.css";
        String repopath= "dev%2Ffiles%2F";
        String branch = "master";
        //getContentFileRepo(file,repopath,branch);


        //getListRepoPaths("source/js");

        String ListaVTEX = ListarArchivosVTEX();

        String ListaRepo = ListarArchivosRepo();


        JSONArray VTEXFiles = new JSONArray(ListaVTEX);
        JSONArray RepoFiles = new JSONArray(ListaRepo);

        for (int i = 0; i <RepoFiles.length() ; i++) {
            JSONObject name = RepoFiles.getJSONObject(i);
            String fileRepo = name.getString("name");
            System.out.println(fileRepo);                       //SOLO PARA VERIFICAR
            for (int j = 0; j < VTEXFiles.length(); j++) {
                
            }
        }



    }

    public static String ListarArchivosRepo()
    {
        String RepoFiles="[";

        RepoFiles=RepoFiles+getListRepoPaths("dev/files")+",";
        RepoFiles=RepoFiles+getListRepoPaths("source/js")+",";
        RepoFiles=RepoFiles+getListRepoPaths("source/json");
        RepoFiles=RepoFiles+"]";

        return RepoFiles;
    }

    public static String ListarArchivosVTEX()
    {
        String galletaVTEX = "VtexRCMacIdv7=821ff380-8d21-11e9-a249-2f7b56cc9687; VtexFingerPrint=11a712643d97e54330868fec964e5bfb; _ga=GA1.2.1223822807.1560368651; vtex_topbar_pic=0-0; _ga=GA1.3.1223822807.1560368651; checkout.vtex.com=__ofid=59682cceb3b64b9ba1db0742dd0b58ec; .ASPXAUTH=5A333F14B2AEB3C39D27258FE88B419ADF9036B15D1A2CB1AFD0562458E92088EF83BDAC202969EA24392C70E3EB4BB2B01B336341B68FAAC0316ADC01E23503FF1F646B78255522630FC247FF730EDCEA06514E62E311FE788C55B75E6445293CF34C8D27F9468EDB5CABE8EA2029DB5932DCB70A57B10FFAD52F149BB5950B3F40F11095D4359B85BD94896A94D921097B758E6141619E624FA54E8CDFB3443F9F6115; databot={\"ENCRYPTED_DATA\":\"\",\"utm_source\":\"\",\"email\":\"\"}; intercom-id-bs8us8hw=8a87d987-38a4-439a-9899-6dacafebfa14; IPI=UsuarioGUID=c67db07b-0d81-43bc-98e7-8e25c4d6d823; _hjid=725b00d6-a65b-40d9-b544-fd45ec907f16; vtex_segment=eyJjYW1wYWlnbnMiOm51bGwsImNoYW5uZWwiOiIyIiwicHJpY2VUYWJsZXMiOm51bGwsInJlZ2lvbklkIjpudWxsLCJ1dG1fY2FtcGFpZ24iOm51bGwsInV0bV9zb3VyY2UiOm51bGwsInV0bWlfY2FtcGFpZ24iOm51bGwsImN1cnJlbmN5Q29kZSI6IlBFTiIsImN1cnJlbmN5U3ltYm9sIjoiUy8iLCJjb3VudHJ5Q29kZSI6IlBFUiIsImN1bHR1cmVJbmZvIjoiZXMtUEUiLCJhZG1pbl9jdWx0dXJlSW5mbyI6ImVuLVVTIn0; i18next=es-ES; _gid=GA1.2.836520032.1566230862; janus_sid=9a26c3b7-be3f-443f-ab96-89295906c1bf; VtexIdclientAutCookie=eyJhbGciOiJFUzI1NiIsImtpZCI6IjI3RkM5MUQwRENCMjJFRDI5RTVCQjU3OUZGRThFQUE0ODRGRTkxOUIiLCJ0eXAiOiJqd3QifQ.eyJzdWIiOiJjYXJsb3MuY2hhbnRhQHByb21hcnQucGUiLCJhY2NvdW50IjoiX192dGV4X2FkbWluIiwic2NvcGUiOiJwcm9tYXJ0OmFkbWluIiwiYXV0aG9yaXphYmxlcyI6WyJ2cm46aWFtOl9fdnRleF9hZG1pbjp1c2Vycy9jYXJsb3MuY2hhbnRhQHByb21hcnQucGUiXSwiZXhwIjoxNTY2NjAwMjUzLCJ1c2VySWQiOiJjNjdkYjA3Yi0wZDgxLTQzYmMtOThlNy04ZTI1YzRkNmQ4MjMiLCJhdXRoX2x2bCI6InN0cm9uZyIsImlhdCI6MTU2NjUxMzg1MywiaXNzIjoidG9rZW4tZW1pdHRlciIsImp0aSI6ImQzYTM4MzJhLTE5ODgtNDJiZC1iNTVlLTk1ZmE4MmE5OTk1YSJ9.szAHdZLA_rcYUpQZBE64yEEG2GtHk4MS71bXkT9r4CpkxcqrJ79R4woI09QS1pMyP8bk51gj9G1mLJ5zhHE7JA; vtex_session=eyJhbGciOiJFUzI1NiIsImtpZCI6IjFGOTdDMjQ4QzI0NjQzMzc1QTcyREE3MTEwQTA1REIwM0I3OUJGQjEiLCJ0eXAiOiJqd3QifQ.eyJhY2NvdW50LmlkIjoiYmM2YTcxYTgtOTQ3Yy00ZmViLWEwMDctZTQzNmRkYjNjZjZlIiwiaWQiOiI5OGYyZWJlZi03MjNmLTQyMjMtOThlNC05NTc0Y2VmMDU2ZjQiLCJ2ZXJzaW9uIjoxNiwic3ViIjoic2Vzc2lvbiIsImFjY291bnQiOiJzZXNzaW9uIiwiZXhwIjoxNTY3MjA1MDY0LCJpYXQiOjE1NjY1MTM4NjQsImlzcyI6InRva2VuLWVtaXR0ZXIiLCJqdGkiOiJhMmJmYzQ5Mi03ZGQ0LTRjZTYtOWU4My0yNTM2ZDQyOTliZWUifQ.qpB-zHOqrEc8RpkYUYiKIcvJ6eTTzvMFiN2-3yVIKK8pSdpV-xJdpWjPvvuLpziG6PMBnMvQhRm7Vb4GPlNjZg; _gid=GA1.3.836520032.1566230862; intercom-session-bs8us8hw=VisxNDlub09IWURJaHJnMjMyaEYxY3dleURZTlYxSkhOa0dwdnlZQVplWDZmbXk5K3B2c1BhWHdKZm1RZGFrKy0tU1YzdFZ1R01wYUhiblBNbHFtMm40Zz09--bf3842d7ca5d67065c468ccd7f317f4e83355cf1; _gat_UA-43760863-20=1";

        String VTEXFiles = Unirest.get("https://promart.myvtex.com/api/portal/pvt/sites/promart/files")
                .header("Cookie",galletaVTEX)
                .asString()
                .getBody();
        return VTEXFiles;
    }

    public static String getListRepoPaths(String path)
    {
        String GetListRepoPaths = Unirest.get("https://gitlab.com/api/v4/projects/12239356/repository/tree")
                .header("PRIVATE-TOKEN","GVVGLD4z4w1vRJ24UYov")
                .queryString("path",path)
                .asString()
                .getBody();

        JSONArray listrepos = new JSONArray(GetListRepoPaths);

        String arrayInit="";

        for (int i = 0; i < listrepos.length(); i++) {
            JSONObject json = listrepos.getJSONObject(i);
            String fileName = json.getString("name");
            String filePath = json.getString("path");
            if (i==listrepos.length()-1)
            {
                arrayInit = arrayInit + "{\"name\":\"" + fileName + "\",\"path\":\"" + filePath + "\"}";
            }else{
                arrayInit = arrayInit + "{\"name\":\"" + fileName + "\",\"path\":\"" + filePath + "\"},";
            }
        }
        return arrayInit;
    }

    public static void getContentFileRepo(String file,String repopath,String branch)
    {
        String GetContentFileRepo = Unirest.get("https://gitlab.com/api/v4/projects/12239356/repository/files/"+repopath+file)
                .header("PRIVATE-TOKEN","GVVGLD4z4w1vRJ24UYov")
                .queryString("ref",branch)
                .asString()
                .getBody();

        JSONObject Jfile = new JSONObject(GetContentFileRepo);
        String text = Jfile.getString("content");

        byte[] valueDecoded = Base64.decodeBase64(text);
        String jsonLoadVtex = "{\"path\":\""+file+"\",\"text\":\""+new String(valueDecoded)+"\"}";

        System.out.println(jsonLoadVtex);
    }
}
