import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.commons.codec.binary.Base64;


public class API_Read {

    public static void main(String[] args)
    {
        //COOKIE DE SESION VTEX
        String galletaVTEX = "VtexRCMacIdv7=0d4abe40-a86b-11e9-8461-4be2b44df238; _ga=GA1.2.414783797.1564880657; _hjid=44928b47-fc91-426c-8270-e0511ba860e4; intercom-id-bs8us8hw=6a8abb2a-1daf-4b79-a435-7457a394f4e1; vtex_topbar_pic=0-0; _ga=GA1.3.414783797.1564880657; VtexFingerPrint=fd2a98dc4477751375799d4b43e9d0e4; janus_sid=5355bcb2-e59f-414f-b2cf-c24127ac240e; vtex_segment=eyJjYW1wYWlnbnMiOm51bGwsImNoYW5uZWwiOiIyIiwicHJpY2VUYWJsZXMiOm51bGwsInJlZ2lvbklkIjpudWxsLCJ1dG1fY2FtcGFpZ24iOm51bGwsInV0bV9zb3VyY2UiOm51bGwsInV0bWlfY2FtcGFpZ24iOm51bGwsImN1cnJlbmN5Q29kZSI6IlBFTiIsImN1cnJlbmN5U3ltYm9sIjoiUy8iLCJjb3VudHJ5Q29kZSI6IlBFUiIsImN1bHR1cmVJbmZvIjoiZXMtUEUiLCJhZG1pbl9jdWx0dXJlSW5mbyI6ImVuLVVTIn0; _gid=GA1.3.1006403163.1566529130; _gid=GA1.2.1006403163.1566529130; _vss=9512E7744DFAD6CEB6B4DB78E9AC8DE9E9059D3C36579AF6D625ADE78E239C78; VtexRCSessionIdv7=0%3A6287bee0-c61f-11e9-ac6a-59430fc4249a; i18next=es-ES; VtexIdclientAutCookie=eyJhbGciOiJFUzI1NiIsImtpZCI6IjcyMTE1REY3RTVFNDJEMjZGMDgzNTBBQjRCODI0NTNENEE2QURDNUMiLCJ0eXAiOiJqd3QifQ.eyJzdWIiOiJjYXJsb3MuY2hhbnRhQHByb21hcnQucGUiLCJhY2NvdW50IjoiX192dGV4X2FkbWluIiwic2NvcGUiOiJwcm9tYXJ0OmFkbWluIiwiYXV0aG9yaXphYmxlcyI6WyJ2cm46aWFtOl9fdnRleF9hZG1pbjp1c2Vycy9jYXJsb3MuY2hhbnRhQHByb21hcnQucGUiXSwiZXhwIjoxNTY2NzAzNzg3LCJ1c2VySWQiOiJjNjdkYjA3Yi0wZDgxLTQzYmMtOThlNy04ZTI1YzRkNmQ4MjMiLCJhdXRoX2x2bCI6InN0cm9uZyIsImlhdCI6MTU2NjYxNzM4NywiaXNzIjoidG9rZW4tZW1pdHRlciIsImp0aSI6IjRhODNmYjU2LTJmMTAtNGUxYy04MmEyLTNiNWEyZDkxZGM0NSJ9.zZgDO362131G1xbhz6vDMufN6bkkkIJ0Y5eDkNBAubvQXOOu2AVU5dR9VuicjDFeqqVTYcb7YT-7AhRI6J0qWQ; vtex_session=eyJhbGciOiJFUzI1NiIsImtpZCI6IkJGMjM5Q0E0REM1RURDQUFCNUQ4MTJEOThFODMzQjk0MURCM0JEQTgiLCJ0eXAiOiJqd3QifQ.eyJhY2NvdW50LmlkIjoiYmM2YTcxYTgtOTQ3Yy00ZmViLWEwMDctZTQzNmRkYjNjZjZlIiwiaWQiOiI1OTAwMDgyMy0xMzhmLTRiZGYtODJkNy1iNWY4NzY4MTYyODEiLCJ2ZXJzaW9uIjozLCJzdWIiOiJzZXNzaW9uIiwiYWNjb3VudCI6InNlc3Npb24iLCJleHAiOjE1NjczMDg2MjIsImlhdCI6MTU2NjYxNzQyMiwiaXNzIjoidG9rZW4tZW1pdHRlciIsImp0aSI6ImViNDkzODE5LWJiNjgtNDE5YS04ZGRjLWYzMTQ0NTg0NmY0MCJ9.fFthwTOyfRaEhwvk49HtHuxk_h4YSrYNVuj_XQsyuMaqgzO1sbotjZStScxrQgmdrdhNmT9LeaX9Ps8p8DCDYg; _hjIncludedInSample=1; VtexRCRequestCounter=3; intercom-session-bs8us8hw=VEp4NGFtR25GRmZNa0RTUTFnSXV6RFpYZllQUHYvd1VFRUc0UThxc25yRC9UMlRPYjh1UW0vK2p5WG1zbnQyaS0tdGdxbUVuVDJueGRnWEJIa2toVm1lUT09--239e067312368959701bc21c358d30c9585348ae; _gat_UA-43760863-20=1";
        //CUENTA VTEX E INSTANCIA
        String CuentaVTEX = "promart";      //PARA ENTORNO PRODUCTIVO: promart      PRUEBAS: qapromart
        String InstanciaVTEX = "promart";   //PARA ENTORNO PRODUCTIVO: promart      PRUEBAS: promartext
        //VARIABLES GITLAB
        String GitlabToken = "GVVGLD4z4w1vRJ24UYov"; //TOKEN GENERADO POR EL OWNER DEL PROYECTO
        String IDProyecto = "12239356"; //OBTENIDO DE GITALB -> CONFIGURACION GENERAL O USANDO EL API
        String RamaOrigen = "master"; //RAMA DE DONDE SE OBTENDRAN LOS ARCHIVOS A SUBIR A VTEX

        String ListaVTEX = ListarArchivosVTEX(galletaVTEX,CuentaVTEX,InstanciaVTEX);
        String ListaRepo = ListarArchivosRepo(IDProyecto,GitlabToken);
        IntegracionREPOaVTEX(ListaVTEX,ListaRepo,galletaVTEX,RamaOrigen,IDProyecto,GitlabToken,CuentaVTEX,InstanciaVTEX);

    }

    public static void IntegracionREPOaVTEX(String ListaVTEX,String ListaRepo,String galletaVTEX,
                                            String RamaOrigen,String IDProyecto,String GitlabToken,String CuentaVTEX,String InstanciaVTEX)
    {
        //FUNCION QUE SUBE ARCHIVOS DE REPO A VTEX
        //SI EL ARCHIVO ESTA EN EL REPO Y EN VTEX, SE OBTIENE SU CONTENIDO Y SE ACTUALIZA
        //SI EL ARCHIVO ESTA EN EL REPO, PERO NO EN VTEX, SE CREA EL ARCHIVO EN EL OMS DE VTEX EN CHECKOUT/CODIGO

        JSONArray VTEXFiles = new JSONArray(ListaVTEX);
        JSONArray RepoFiles = new JSONArray(ListaRepo);

        for (int i = 0; i <RepoFiles.length() ; i++) {
            JSONObject name = RepoFiles.getJSONObject(i);
            String fileRepo = name.getString("name");
            String filePath = name.getString("path");
            ComparaySubeaVTEX(galletaVTEX,VTEXFiles,fileRepo,filePath,RamaOrigen,IDProyecto,GitlabToken,CuentaVTEX,InstanciaVTEX);
        }
    }

    public static String ListarArchivosRepo(String IDProyecto,String GitlabToken)
    {
        String RepoFiles="[";
        RepoFiles=RepoFiles+getListRepoPaths("dev/files",IDProyecto,GitlabToken)+",";
        RepoFiles=RepoFiles+getListRepoPaths("source/js",IDProyecto,GitlabToken)+",";
        RepoFiles=RepoFiles+getListRepoPaths("source/json",IDProyecto,GitlabToken);
        RepoFiles=RepoFiles+"]";
        return RepoFiles;
    }

    public static String ListarArchivosVTEX(String galletaVTEX, String CuentaVTEX, String InstanciaVTEX)
    {
        String VTEXFiles = Unirest.get("https://"+CuentaVTEX+".myvtex.com/api/portal/pvt/sites/"+InstanciaVTEX+"/files")
                .header("Cookie",galletaVTEX)
                .asString()
                .getBody();
        return VTEXFiles;
    }

    public static String getListRepoPaths(String path,String IDProyecto,String GitlabToken)
    {
        String GetListRepoPaths = Unirest.get("https://gitlab.com/api/v4/projects/"+IDProyecto+"/repository/tree")
                .header("PRIVATE-TOKEN",GitlabToken)
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

    public static String getContentFileRepo(String file,String repopath,String branch,String IDProyecto, String GitlabToken)
    {
        //CONVERTIR LOS "/" EN "%2F" PARA QUE SEA LEIBLE POR EL API DE GITLAB
        // ES DECIR, EL PATH source/js se convertira en source%2Fjs
        String finalPath = repopath.replace("/", "%2F");
        String GetContentFileRepo = Unirest.get("https://gitlab.com/api/v4/projects/"+IDProyecto+"/repository/files/"+finalPath+file)
                .header("PRIVATE-TOKEN",GitlabToken)
                .queryString("ref",branch)
                .asString()
                .getBody();

        JSONObject Jfile = new JSONObject(GetContentFileRepo);
        String text = Jfile.getString("content");
        byte[] valueDecoded = Base64.decodeBase64(text);
        String jsonLoadVtex = "{\"path\":\""+file+"\",\"text\":\""+new String(valueDecoded)+"\"}";
        return  jsonLoadVtex; //SE RETORNA EL JSON QUE SE USARA PARA EL API QUE HACE LA SUBIDA A VTEX
    }

    public static void ComparaySubeaVTEX(String galletaVTEX,JSONArray VTEXFiles,String fileRepo,String filePath,
                                         String RamaOrigen,String IDProyecto,String GitlabToken,String CuentaVTEX,String InstanciaVTEX)
    {
        for (int k = 0; k < VTEXFiles.length(); k++) {
            String fileVTEX = VTEXFiles.getString(k);
            if (fileRepo.equals(fileVTEX)){
                System.out.println(fileRepo+"="+fileVTEX+"    ARCHIVO DE REPO ENCONTRADO!!!!");
                System.out.println("SUBIENDO A VTEX...");
                HttpResponse<JsonNode> response = Unirest.put("https://"+CuentaVTEX+".myvtex.com/api/portal/pvt/sites/"+InstanciaVTEX+"/files/" + fileRepo)
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json, text/plain")
                        .header("Cookie", galletaVTEX)
                        .body(getContentFileRepo(fileRepo,filePath,RamaOrigen,IDProyecto,GitlabToken))
                        .asJson();
                if (response.isSuccess()){
                    System.out.println("ARCHIVO SUBIDO CON EXITO");
                    break;
                }else{
                    System.out.println("FALLO LA SUBIDA DE ARCHIVO, ERROR: "+response.getStatusText());
                    break;
                }
            }
        }
    }
}
