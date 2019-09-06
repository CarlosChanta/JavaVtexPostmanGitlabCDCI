import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.commons.codec.binary.Base64;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@SuppressWarnings("ALL")
public class API_Read {
    public static void main(String[] args) throws UnsupportedEncodingException {

        String CuentaVTEX = "promart";
        String InstanciaVTEX ="promart";
        String GitlabToken = "x4httFHKLpu-cyRcK25L" ;
        String IDProyecto = "13975143";
        String RamaOrigen= "master";
        String bodyAuth = "recaptcha=&login=carlos.chanta%40promart.pe&authenticationToken=ABA28DB438EB01C81452ADBE4B83FD887CBBB6F6EC941AC4716076AB906F5638&password=161815%40Yuki&fingerprint=9ab5cb2503a6a27141adc390a466d22b&method=POST";
        System.out.println("------INICIANDO PROCESO DE HOMOLOGACION------");
        System.out.println("CUENTA VTEX OBJETIVO: "+CuentaVTEX);
        System.out.println("INSTANCIA OBJETIVO: "+InstanciaVTEX);
        System.out.println("ID DEL PROYECTO GITLAB OBJETIVO: "+IDProyecto);
        System.out.println("RAMA ORIGEN: "+RamaOrigen);
        System.out.println("---------------------------------------------");

        JSONObject VTEXCredentials =loginvtex(bodyAuth,CuentaVTEX);
        String UserVTEX = VTEXCredentials.getString("ID");
        String galletaVTEX = VTEXCredentials.getString("Cookie");

        System.out.println("Cookie Obtenida!: "+galletaVTEX);

        String ListaVTEX = ListarArchivosVTEX(UserVTEX,galletaVTEX,CuentaVTEX,InstanciaVTEX);
        String ListaRepo = ListarArchivosRepo(IDProyecto,GitlabToken);

        IntegracionREPOaVTEX(ListaVTEX,ListaRepo,UserVTEX,galletaVTEX,RamaOrigen,IDProyecto,GitlabToken,CuentaVTEX,InstanciaVTEX);
    }

    private static void IntegracionREPOaVTEX(String ListaVTEX, String ListaRepo, String UserVTEX,String galletaVTEX,
                                             String RamaOrigen, String IDProyecto, String GitlabToken, String CuentaVTEX, String InstanciaVTEX) throws UnsupportedEncodingException {
        JSONArray VTEXFiles = new JSONArray(ListaVTEX);
        JSONArray RepoFiles = new JSONArray(ListaRepo);
        for (int i = 0; i <RepoFiles.length() ; i++) {
            JSONObject name = RepoFiles.getJSONObject(i);
            String fileRepo = name.getString("name");
            String filePath = name.getString("path");
            if (fileRepo.substring(fileRepo.length()-4).equals("html") || fileRepo.substring(fileRepo.length()-4).equals("HTML")){
                System.out.println("ARCHIVO HTML "+fileRepo+"... VERIFICANDO EN CMS");
                ComparaysubeaVTEX_CMS(CuentaVTEX,UserVTEX,galletaVTEX,fileRepo,filePath,RamaOrigen,IDProyecto,GitlabToken);
            }else {
                ComparaySubeaVTEX_OMS(UserVTEX,galletaVTEX,VTEXFiles,fileRepo,filePath,RamaOrigen,IDProyecto,GitlabToken,CuentaVTEX,InstanciaVTEX);
            }
        }
    }

    private static void ComparaysubeaVTEX_CMS(String CuentaVTEX,String UserVtex,String galletaVTEX,String fileRepo,String filePath,
                                              String branch,String IDProyecto,String GitlabToken) throws UnsupportedEncodingException
    {
        String DIRhtmltemplates = "dir=templates%253A%2F";
        String DIRSubTemplates ="dir=sub-templates%253A%2F";
        String DIRShelveTemplates = "dir=shelf-templates%253A%2F";

        JSONArray ListaHtmlTemplates = ListaTemplatesVTEX(CuentaVTEX,UserVtex,galletaVTEX,DIRhtmltemplates,1);
        JSONArray ListaSubTemplates = ListaTemplatesVTEX(CuentaVTEX,UserVtex,galletaVTEX,DIRSubTemplates,2);
        JSONArray ListaShelveTemplates = ListaTemplatesVTEX(CuentaVTEX,UserVtex,galletaVTEX,DIRShelveTemplates,3);

        System.out.println("BUSCANDO HTML EN Lista de HTMLTEMPLATES");
        if (RecorreListaTemplate(ListaHtmlTemplates,1,fileRepo,CuentaVTEX,UserVtex,galletaVTEX,filePath,branch,IDProyecto,GitlabToken).equals(0)){
            if (RecorreListaTemplate(ListaSubTemplates,2,fileRepo,CuentaVTEX,UserVtex,galletaVTEX,filePath,branch,IDProyecto,GitlabToken).equals(0)){
                if (RecorreListaTemplate(ListaShelveTemplates,3,fileRepo,CuentaVTEX,UserVtex,galletaVTEX,filePath,branch,IDProyecto,GitlabToken).equals(0)){
                    System.out.println("NO SE HA ENCONTRADO ARCHIVO HTML EN NINGUNA LISTA DE CMS");
                }else{
                    System.out.println("SHELVE TEMPLATE ACTUALIZADO EN CMS");
                }
            }else{
                System.out.println("SUB TEMPLATE ACTUALIZADO CON EXITO");
            }
        }else{
            System.out.println("HTML TEMPLATE ACTUALIZADO CON EXITO");
        }

    }

    private static Integer RecorreListaTemplate(JSONArray Lista,Integer TipoLista,String fileRepo,String CuentaVTEX,String UserVtex,String galletaVTEX,
                                             String filePath,String branch,String IDProyecto,String GitlabToken) throws UnsupportedEncodingException {
        Integer ResultadoBusqueda=0;
        for (int i = 0; i < Lista.length(); i++) {
            JSONObject objTemplate = Lista.getJSONObject(i);
            String nametemp = objTemplate.getString("name")+".html";
            if (fileRepo.equals(nametemp)){
                System.out.println("TEMPLATE HTML ENCONTRADO EN VTEX!");
                System.out.println("ACTUALIZANDO....");
                HttpResponse<JsonNode> response = Unirest.post("https://"+CuentaVTEX+".vtexcommercestable.com.br/admin/a/PortalManagement/SaveTemplate")
                        .header("userId",UserVtex)
                        .header("VtexIdclientAutCookie",galletaVTEX)
                        .header("Content-Type","application/x-www-form-urlencoded")
                        .body(getContentFileRepoHTML(TipoLista,filePath,branch,IDProyecto,GitlabToken,objTemplate.getString("name"),objTemplate.getString("nameid")))
                        .asJson();
                if (response.getStatus()==200){
                    System.out.println("TEMPLATE ACUALIZADO CON EXITO EN CMS");
                    ResultadoBusqueda = 1;
                    break;
                }else{
                    System.out.println("FALLO AL ACTUALIZAR TEMPLATE!, ERROR: "+response.getStatus()+" - "+response.getStatusText());
                    break;
                }
            }
        }
        return ResultadoBusqueda;
    }

    private static String ListarArchivosRepo(String IDProyecto, String GitlabToken)
    {
        String RepoFiles="[";
        RepoFiles=RepoFiles+getListRepoPaths("dev/files",IDProyecto,GitlabToken)+",";
        RepoFiles=RepoFiles+getListRepoPaths("source/js",IDProyecto,GitlabToken)+",";
        RepoFiles=RepoFiles+getListRepoPaths("source/json",IDProyecto,GitlabToken)+",";
        RepoFiles=RepoFiles+getListRepoPaths("source/html",IDProyecto,GitlabToken);
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

    private static void ComparaySubeaVTEX_OMS(String UserVTEX,String galletaVTEX, JSONArray VTEXFiles, String fileRepo, String filePath,
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

    public static String getContentFileRepoHTML(Integer TipoLista,String repopath, String branch, String IDProyecto, String GitlabToken,String templatename,
                                                String templateID) throws UnsupportedEncodingException
    {
        String finalPath = repopath.replace("/", "%2F");
        String GetContentFileRepo = Unirest.get("https://gitlab.com/api/v4/projects/"+IDProyecto+"/repository/files/"+finalPath)
                .header("PRIVATE-TOKEN",GitlabToken)
                .queryString("ref",branch)
                .asString()
                .getBody();

        JSONObject Jfile = new JSONObject(GetContentFileRepo);
        String text = Jfile.getString("content");
        byte[] valueDecoded = Base64.decodeBase64(text);
        String content = new String(valueDecoded);

        String content1252= ""+ URLEncoder.encode(content, "cp1252");
        String content1252fix = content1252.replace("+","%20");
        String htmlFinal = content1252fix.replace("%21","!");

        //ARMAR EL RAW A ENVIAR

        String rawTemplate="";

        switch (TipoLista){
            case 1:
                rawTemplate = rawTemplate+"templatename="+templatename+"&textConfirm=yes&template="+htmlFinal+
                        "&alternativeHtml=&templateId=" + templateID + "&isSub=False&actionForm=Update&X-Requested-With=XMLHttpRequest";
                System.out.println(rawTemplate);
                break;
            case 2:
                rawTemplate = rawTemplate+"templatename="+templatename+"&textConfirm=yes&template="+htmlFinal+
                        "&alternativeHtml=&templateId=" + templateID + "&isSub=true&actionForm=Update&X-Requested-With=XMLHttpRequest";
                System.out.println(rawTemplate);
                break;
            case 3:
                rawTemplate =""; //PENDIENTE
                break;
            default:
                System.out.println("ERROR? NO SE TIENE DIRTYPE");
        }

        return rawTemplate;
    }

    private static JSONArray ListaTemplatesVTEX(String CuentaVTEX,String userID,String Cookie,String rawpath,Integer dirtype)
    {
        String StringListaFinal = null;
        String HTMLLista = Unirest.post("https://"+CuentaVTEX+".vtexcommercestable.com.br/admin/a/PortalManagement/FolderContent")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("userId", userID)
                .header("VtexIdclientAutCookie", Cookie)
                .body(rawpath)
                .asString()
                .getBody();
        if (dirtype.equals(1)){
            String list1 = HTMLLista.replace("<ul class=\"jqueryFileTree\" rel=\"\" style=\"display: none;\"><li class=\"file add\"><a href=\"addTemplate:/admin/a/PortalManagement/AddTemplate?siteId=&isSub=false\" path=\"addTemplate:/admin/a/PortalManagement/AddTemplate?siteId=&isSub=false\" rel=\"addTemplate:/admin/a/PortalManagement/AddTemplate?siteId=&isSub=false\">new template</a></li>", "");
            String list2 = list1.replace("<li class=\"directory collapsed template-folder\"><a href=\"#\" path=\"#\" rel=\"sub-templates:/\">Sub Templates</a></li></ul>", "");
            String list3 = list2.replace("<li class=\"file template\"><a href=\"\" path=\"\" rel=\"template:", "{\"nameid\":\"");
            String list4 = list3.replace(":/\">", "\",\"name\":\"");
            String prefinalObjects = list4.replace("</a></li>", "\"},");
            String FinalObjects = prefinalObjects.substring(0, prefinalObjects.length() - 1);
            StringListaFinal = "["+FinalObjects+"]";
        }
        if (dirtype.equals(2)) {
            String sublista1 = HTMLLista.replace("<ul class=\"jqueryFileTree\" rel=\"\" style=\"display: none;\"><li class=\"file add\"><a href=\"addTemplate:/admin/a/PortalManagement/AddTemplate?siteId=&isSub=true\" path=\"addTemplate:/admin/a/PortalManagement/AddTemplate?siteId=&isSub=true\" rel=\"addTemplate:/admin/a/PortalManagement/AddTemplate?siteId=&isSub=true\">new sub template</a></li>","");
            String sublista2 = sublista1.replace("<li class=\"file template\"><a href=\"\" path=\"\" rel=\"template:","{\"nameid\":\"");
            String sublista3 = sublista2.replace(":/\">", "\",\"name\":\"");
            String sublista4 = sublista3.replace("</a></li>", "\"},");
            String prefinalObjectsSub = sublista4.replace("</ul>","");
            String FinalSubObjects = prefinalObjectsSub.substring(0,prefinalObjectsSub.length()-1);
            StringListaFinal = "["+FinalSubObjects+"]";
        }
        if (dirtype.equals(3)){
            String shelveslist1 = HTMLLista.replace("<ul class=\"jqueryFileTree\" rel=\"\" style=\"display: none;\"><li class=\"file add\"><a href=\"addShelfTemplate:/admin/a/PortalManagement/AddShelfTemplate?siteId=\" path=\"addShelfTemplate:/admin/a/PortalManagement/AddShelfTemplate?siteId=\" rel=\"addShelfTemplate:/admin/a/PortalManagement/AddShelfTemplate?siteId=\">new template</a></li>","");
            String shelveslist2 = shelveslist1.replace("<li class=\"file template\"><a href=\"\" path=\"\" rel=\"shelf-template:","{\"nameid\":\"");
            String shelveslist3 = shelveslist2.replace(":/\">", "\",\"name\":\"");
            String shelveslist4 = shelveslist3.replace("</a></li>", "\"},");
            String prefinalObjectsShelves = shelveslist4.replace("</ul>","");
            String FinalShelvesObjects = prefinalObjectsShelves.substring(0,prefinalObjectsShelves.length()-1);
            StringListaFinal = "["+FinalShelvesObjects+"]";
        }
        return new JSONArray(StringListaFinal);
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
        try (PrintStream out = new PrintStream(new FileOutputStream("GalletitaDelDia.txt"))) {
            out.print(auth.getString("Value"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new JSONObject("{\"ID\":\""+resp.getString("userId")+"\",\"Cookie\":\""+auth.getString("Value")+"\"}");
    }
}
