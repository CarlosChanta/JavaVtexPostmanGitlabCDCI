import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.commons.codec.binary.Base64;

public class API_Read {
    public static void main(String[] args)
    {
        //DATOS DE LOGIN VTEX
        String bodyAuth="recaptcha=&login=carlos.chanta%40promart.pe&authenticationToken=939A78CF126C44E0A16A1E33E80914895506F1BE13C5F48D9C00953319A378C5&password=161815%40Yuki&fingerprint=246049c9a2110d5cbc69fe110af90bab&method=POST";
        String CuentaVTEX = "qapromart";      //PARA ENTORNO PRODUCTIVO: promart      PRUEBAS: qapromart
        String InstanciaVTEX = "promartext";   //PARA ENTORNO PRODUCTIVO: promart      PRUEBAS: promartext
        //VARIABLES GITLAB
        String GitlabToken = "x4httFHKLpu-cyRcK25L"; //TOKEN GENERADO POR EL OWNER DEL PROYECTO
        String IDProyecto = "13975143";//DE PRODUCCION: 12239356 OBTENIDO DE GITALB -> CONFIGURACION GENERAL O USANDO EL API
        String RamaOrigen = "master"; //RAMA DE DONDE SE OBTENDRAN LOS ARCHIVOS A SUBIR A VTEX

        JSONObject VTEXCredentials =loginvtex(bodyAuth,CuentaVTEX);
        String UserVTEX = VTEXCredentials.getString("ID");
        String galletaVTEX = VTEXCredentials.getString("Cookie");

        /*
        String CuentaVTEX = args[0];
        String InstanciaVTEX = args[1];
        String GitlabToken = args[2];
        String IDProyecto = args[3];
        String RamaOrigen= args[4];
        String galletaVTEX = args[5]
        */
        String ListaVTEX = ListarArchivosVTEX(UserVTEX,galletaVTEX,CuentaVTEX,InstanciaVTEX);
        String ListaRepo = ListarArchivosRepo(IDProyecto,GitlabToken);
        IntegracionREPOaVTEX(ListaVTEX,ListaRepo,UserVTEX,galletaVTEX,RamaOrigen,IDProyecto,GitlabToken,CuentaVTEX,InstanciaVTEX);
    }

    private static void IntegracionREPOaVTEX(String ListaVTEX, String ListaRepo, String UserVTEX,String galletaVTEX,
                                             String RamaOrigen, String IDProyecto, String GitlabToken, String CuentaVTEX, String InstanciaVTEX)
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
            ComparaySubeaVTEX(UserVTEX,galletaVTEX,VTEXFiles,fileRepo,filePath,RamaOrigen,IDProyecto,GitlabToken,CuentaVTEX,InstanciaVTEX);
        }
    }

    private static String ListarArchivosRepo(String IDProyecto, String GitlabToken)
    {
        String RepoFiles="[";
        RepoFiles=RepoFiles+getListRepoPaths("dev/files",IDProyecto,GitlabToken)+",";
        RepoFiles=RepoFiles+getListRepoPaths("source/js",IDProyecto,GitlabToken)+",";
        RepoFiles=RepoFiles+getListRepoPaths("source/json",IDProyecto,GitlabToken);
        RepoFiles=RepoFiles+"]";
        return RepoFiles;
    }

    private static String ListarArchivosVTEX(String UserVTEX,String galletaVTEX, String CuentaVTEX, String InstanciaVTEX)
    {
        String VTEXFiles = Unirest.get("https://"+CuentaVTEX+".myvtex.com/api/portal/pvt/sites/"+InstanciaVTEX+"/files")
                .header("userId",UserVTEX)
                .header("VtexIdclientAutCookie",galletaVTEX)
                .asString()
                .getBody();
        return VTEXFiles;
    }

    private static String getListRepoPaths(String path, String IDProyecto, String GitlabToken)
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

    private static String getContentFileRepo(String file, String repopath, String branch, String IDProyecto, String GitlabToken)
    {
        //CONVERTIR LOS "/" EN "%2F" PARA QUE SEA LEIBLE POR EL API DE GITLAB
        // ES DECIR, EL PATH source/js se convertira en source%2Fjs
        String finalPath = repopath.replace("/", "%2F");
        String GetContentFileRepo = Unirest.get("https://gitlab.com/api/v4/projects/"+IDProyecto+"/repository/files/"+finalPath)
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

    private static void ComparaySubeaVTEX(String UserVTEX,String galletaVTEX, JSONArray VTEXFiles, String fileRepo, String filePath,
                                          String RamaOrigen, String IDProyecto, String GitlabToken, String CuentaVTEX, String InstanciaVTEX)
    {
        for (int k = 0; k < VTEXFiles.length(); k++) {
            String fileVTEX = VTEXFiles.getString(k);
            if (fileRepo.equals(fileVTEX)){
                System.out.println(fileRepo+"="+fileVTEX+"    ARCHIVO DE REPO ENCONTRADO!!!!");
                System.out.println("SUBIENDO A VTEX...");
                HttpResponse<JsonNode> response = Unirest.put("https://"+CuentaVTEX+".myvtex.com/api/portal/pvt/sites/"+InstanciaVTEX+"/files/" + fileRepo)
                        .header("userId",UserVTEX)
                        .header("VtexIdclientAutCookie", galletaVTEX)
                        .header("Content-Type", "application/json")
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

    private static JSONObject loginvtex(String bodyAuth, String CuentaVtex)
    {
        String ResponseLogin = Unirest.post("https://"+CuentaVtex+".myvtex.com/api/vtexid/pub/authentication/classic/validate")
                .header("Content-Type","application/x-www-form-urlencoded")
                .body(bodyAuth)
                .asString()
                .getBody();
        JSONObject resp = new JSONObject(ResponseLogin);
        JSONObject auth = resp.getJSONObject("authCookie");
        JSONObject VTEXCredentials = new JSONObject("{\"ID\":\""+resp.getString("userId")+"\",\"Cookie\":\""+auth.getString("Value")+"\"}");
        return VTEXCredentials;
    }
}
