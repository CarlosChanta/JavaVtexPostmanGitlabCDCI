import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.commons.codec.binary.Base64;

@SuppressWarnings("ALL")
public class API_Read {
    public static void main(String[] args)
    {
        /*
        String CuentaVTEX = args[0];
        String InstanciaVTEX = args[1];
        String GitlabToken = args[2];
        String IDProyecto = args[3];
        String RamaOrigen= args[4];
        String bodyAuth = args[5];
        */
        String CuentaVTEX = "qapromart";
        String InstanciaVTEX ="promartext";
        String GitlabToken = "x4httFHKLpu-cyRcK25L" ;
        String IDProyecto = "13975143";
        String RamaOrigen= "master";
        String bodyAuth = "recaptcha=&login=carlos.chanta%40promart.pe&authenticationToken=9C315D7969D3086ADB06E60156D3AFE264EDCD3D223652E0DA5088831968980A&password=161815%40Yuki&fingerprint=9ab5cb2503a6a27141adc390a466d22b&method=POST";

        System.out.println("------INICIANDO PROCESO DE HOMOLOGACION------");
        System.out.println("CUENTA VTEX OBJETIVO: "+CuentaVTEX);
        System.out.println("INSTANCIA OBJETIVO: "+InstanciaVTEX);
        System.out.println("ID DEL PROYECTO GITLAB OBJETIVO: "+IDProyecto);
        System.out.println("RAMA ORIGEN: "+RamaOrigen);
        System.out.println("---------------------------------------------");
        System.out.println(bodyAuth);

        JSONObject VTEXCredentials =loginvtex(bodyAuth,CuentaVTEX);
        String UserVTEX = VTEXCredentials.getString("ID");
        String galletaVTEX = VTEXCredentials.getString("Cookie");

        System.out.println("Cookie Obtenida!: "+galletaVTEX);

        String ListaVTEX = ListarArchivosVTEX(UserVTEX,galletaVTEX,CuentaVTEX,InstanciaVTEX);
        String ListaRepo = ListarArchivosRepo(IDProyecto,GitlabToken);
        IntegracionREPOaVTEX(ListaVTEX,ListaRepo,UserVTEX,galletaVTEX,RamaOrigen,IDProyecto,GitlabToken,CuentaVTEX,InstanciaVTEX);
    }

    private static void IntegracionREPOaVTEX(String ListaVTEX, String ListaRepo, String UserVTEX,String galletaVTEX,
                                             String RamaOrigen, String IDProyecto, String GitlabToken, String CuentaVTEX, String InstanciaVTEX)
    {
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
        return Unirest.get("https://"+CuentaVTEX+".myvtex.com/api/portal/pvt/sites/"+InstanciaVTEX+"/files")
                .header("userId",UserVTEX)
                .header("VtexIdclientAutCookie",galletaVTEX)
                .asString()
                .getBody();
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
        //SE REEMPLAZAN LOS SALTOS DE LINEA,TABULACIONES Y COMILLAS POR \n,\t Y \" PARA QUE SEA LEIBLE POR VTEX
        String content = new String(valueDecoded);
        String textNoNextLine = content.replaceAll("[\\\n]","\\\\n");
        String textNoTabs = textNoNextLine.replaceAll("[\\\t]","\\\\t");
        String textFinal = textNoTabs.replaceAll("\"","\\\\\"");

        return "{\"path\":\""+file+"\",\"text\":\""+textFinal+"\"}"; //SE RETORNA EL JSON QUE SE USARA PARA EL API QUE HACE LA SUBIDA A VTEX
    }

    private static void ComparaySubeaVTEX(String UserVTEX,String galletaVTEX, JSONArray VTEXFiles, String fileRepo, String filePath,
                                          String RamaOrigen, String IDProyecto, String GitlabToken, String CuentaVTEX, String InstanciaVTEX)
    {
        for (int k = 0; k < VTEXFiles.length(); k++) {
            String fileVTEX = VTEXFiles.getString(k);
            if (fileRepo.equals(fileVTEX)){
                System.out.println("ARCHIVO "+fileRepo+" DE REPOSITORIO, ENCONTRADO EN VTEX PORTAL!");
                System.out.println("ACTUALIZANDO...");
                HttpResponse<JsonNode> response = Unirest.put("https://"+CuentaVTEX+".myvtex.com/api/portal/pvt/sites/"+InstanciaVTEX+"/files/" + fileRepo)
                        .header("userId",UserVTEX)
                        .header("VtexIdclientAutCookie", galletaVTEX)
                        .header("Content-Type", "application/json")
                        .body(getContentFileRepo(fileRepo,filePath,RamaOrigen,IDProyecto,GitlabToken))
                        .asJson();
                if (response.isSuccess()){
                    System.out.println("ARCHIVO ACUALIZADO CON EXITO");
                    break;
                }else{
                    System.out.println("FALLO AL ACTUALIZAR ARCHIVO!, ERROR: "+response.getStatus()+" - "+response.getStatusText());
                    break;
                }
            }else{
                if (k==VTEXFiles.length()-1){
                    System.out.println("ARCHIVO "+fileRepo+" DE REPOSITORIO, NO ENCONTRADO EN VTEX PORTAL");
                    System.out.println("CREANDO!!!!...");
                    HttpResponse<JsonNode> response = Unirest.put("https://"+CuentaVTEX+".myvtex.com/api/portal/pvt/sites/"+InstanciaVTEX+"/files/" + fileRepo)
                            .header("userId",UserVTEX)
                            .header("VtexIdclientAutCookie", galletaVTEX)
                            .header("Content-Type", "application/json")
                            .body(getContentFileRepo(fileRepo,filePath,RamaOrigen,IDProyecto,GitlabToken))
                            .asJson();
                    if (response.isSuccess()){
                        System.out.println("ARCHIVO CREADO CON EXITO");
                        break;
                    }else{
                        System.out.println("FALLO AL CREAR ARCHIVO!, ERROR: "+response.getStatus()+" - "+response.getStatusText());
                        break;
                    }
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
        return new JSONObject("{\"ID\":\""+resp.getString("userId")+"\",\"Cookie\":\""+auth.getString("Value")+"\"}");
    }
}
