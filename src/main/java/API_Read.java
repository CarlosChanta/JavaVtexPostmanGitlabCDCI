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
        String galletaVTEX = "VtexRCMacIdv7=0d4abe40-a86b-11e9-8461-4be2b44df238; _ga=GA1.2.414783797.1564880657; _hjid=44928b47-fc91-426c-8270-e0511ba860e4; intercom-id-bs8us8hw=6a8abb2a-1daf-4b79-a435-7457a394f4e1; VtexFingerPrint=fd2a98dc4477751375799d4b43e9d0e4; _gid=GA1.2.1006403163.1566529130; janus_sid=59341d4a-86cc-4ae6-b65f-d19d28519ac3; vtex_segment=eyJjYW1wYWlnbnMiOm51bGwsImNoYW5uZWwiOiIxIiwicHJpY2VUYWJsZXMiOm51bGwsInJlZ2lvbklkIjpudWxsLCJ1dG1fY2FtcGFpZ24iOm51bGwsInV0bV9zb3VyY2UiOm51bGwsInV0bWlfY2FtcGFpZ24iOm51bGwsImN1cnJlbmN5Q29kZSI6IlBFTiIsImN1cnJlbmN5U3ltYm9sIjoiUy8iLCJjb3VudHJ5Q29kZSI6IlBFUiIsImN1bHR1cmVJbmZvIjoiZXMtUEUiLCJhZG1pbl9jdWx0dXJlSW5mbyI6ImVuLVVTIn0; vtex_topbar_pic=0-0; _ga=GA1.3.414783797.1564880657; _gid=GA1.3.1006403163.1566529130; VtexRCSessionIdv7=0%3A1c24efe0-c773-11e9-95a8-5997f6880c05; _vss=26F37696CF32D328895A1BFBF3E6492BE1CA23F2EEC5C4D91B219F21E2BA2871; i18next=es-ES; VtexIdclientAutCookie=eyJhbGciOiJFUzI1NiIsImtpZCI6IkJEMjlCNTUxNTgwRDZGRjA2RjEyQkQxMjU0MEJDMTA1RTg5NThENUIiLCJ0eXAiOiJqd3QifQ.eyJzdWIiOiJjYXJsb3MuY2hhbnRhQHByb21hcnQucGUiLCJhY2NvdW50IjoiX192dGV4X2FkbWluIiwic2NvcGUiOiJxYXByb21hcnQ6YWRtaW4iLCJhdXRob3JpemFibGVzIjpbInZybjppYW06X192dGV4X2FkbWluOnVzZXJzL2Nhcmxvcy5jaGFudGFAcHJvbWFydC5wZSJdLCJleHAiOjE1NjY4NDk2ODcsInVzZXJJZCI6ImM2N2RiMDdiLTBkODEtNDNiYy05OGU3LThlMjVjNGQ2ZDgyMyIsImF1dGhfbHZsIjoic3Ryb25nIiwiaWF0IjoxNTY2NzYzMjg3LCJpc3MiOiJ0b2tlbi1lbWl0dGVyIiwianRpIjoiYjZiOTI1NzgtNDA0ZS00ODExLWEwNWEtOTU4ZWNhN2E3MGUzIn0.nc1w5qwoBiW2Up5tVq9VASTF_H4QwP-WUaTVBtbTzJ188pzRuyW6bdUu8_qZHl88a5mdBOj67aa3D1XKWIHA9g; vtex_session=eyJhbGciOiJFUzI1NiIsImtpZCI6IkUxMDMwOTQyMzY2QjIzMzZERUZBRDNENDE1QkRBQTEyNjU3MUI5MTgiLCJ0eXAiOiJqd3QifQ.eyJhY2NvdW50LmlkIjoiYjJmMmUwNGItZWUzYi00NWQ0LWEwYWItYTg1ZTUxNWFlZTc0IiwiaWQiOiI2N2E1ODIyYS1jOTJjLTQyMzktOTM5Mi02ZTg5NGEyY2YzMTQiLCJ2ZXJzaW9uIjozLCJzdWIiOiJzZXNzaW9uIiwiYWNjb3VudCI6InNlc3Npb24iLCJleHAiOjE1Njc0NTQ1MDksImlhdCI6MTU2Njc2MzMwOSwiaXNzIjoidG9rZW4tZW1pdHRlciIsImp0aSI6Ijk3NjE2ODc4LTg0MzctNGRmNy1iOTc2LTVkMDMzYWJkYzM3MyJ9.fplAEVKlyTbdNlvARWMqHPfXqrUqzvfBVQsdfiMpWZlt0t_2LQGvaNJ3VjbQkV6uk1HbCyXHb1eVRAQ6NNtsqg; _hjIncludedInSample=1; VtexRCRequestCounter=3; intercom-session-bs8us8hw=aFArdVBNMUlRSml3VVVOYjlXZzVaNytvQ1BiWUpWWi94OEVPRUJ0dm5VTXRNMU05ZWlrM05CUzhiUkQ4bDZQay0tZm5YeWhlT2Y4dGZib21MWWZaVGw5UT09--b05c75fdc1d085a117ebe1c95e981af631107698; _gat_UA-43760863-20=1";        //CUENTA VTEX E INSTANCIA
        String CuentaVTEX = "qapromart";      //PARA ENTORNO PRODUCTIVO: promart      PRUEBAS: qapromart
        String InstanciaVTEX = "promartext";   //PARA ENTORNO PRODUCTIVO: promart      PRUEBAS: promartext
        //VARIABLES GITLAB
        String GitlabToken = "x4httFHKLpu-cyRcK25L"; //TOKEN GENERADO POR EL OWNER DEL PROYECTO
        String IDProyecto = "13975143";//DE PRODUCCION: 12239356 OBTENIDO DE GITALB -> CONFIGURACION GENERAL O USANDO EL API
        String RamaOrigen = "master"; //RAMA DE DONDE SE OBTENDRAN LOS ARCHIVOS A SUBIR A VTEX


        /*

        String CuentaVTEX = args[0];
        String InstanciaVTEX = args[1];
        String GitlabToken = args[2];
        String IDProyecto = args[3];
        String RamaOrigen= args[4];
        String galletaVTEX = args[5]

        */
        String ListaVTEX = ListarArchivosVTEX(galletaVTEX,CuentaVTEX,InstanciaVTEX);
        String ListaRepo = ListarArchivosRepo(IDProyecto,GitlabToken);
        IntegracionREPOaVTEX(ListaVTEX,ListaRepo,galletaVTEX,RamaOrigen,IDProyecto,GitlabToken,CuentaVTEX,InstanciaVTEX);
    }

    private static void IntegracionREPOaVTEX(String ListaVTEX, String ListaRepo, String galletaVTEX,
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
            ComparaySubeaVTEX(galletaVTEX,VTEXFiles,fileRepo,filePath,RamaOrigen,IDProyecto,GitlabToken,CuentaVTEX,InstanciaVTEX);
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

    private static String ListarArchivosVTEX(String galletaVTEX, String CuentaVTEX, String InstanciaVTEX)
    {
        String VTEXFiles = Unirest.get("https://"+CuentaVTEX+".myvtex.com/api/portal/pvt/sites/"+InstanciaVTEX+"/files")
                .header("Cookie",galletaVTEX)
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

    private static void ComparaySubeaVTEX(String galletaVTEX, JSONArray VTEXFiles, String fileRepo, String filePath,
                                          String RamaOrigen, String IDProyecto, String GitlabToken, String CuentaVTEX, String InstanciaVTEX)
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
