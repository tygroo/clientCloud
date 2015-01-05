
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.ws.rs.core.MediaType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.commons.io.FilenameUtils;

/**
 * Created by Bonheur on 30/12/2014.
 */
public class FileUploader {
    public static void main(String args[]) throws Exception
    {
        FileUploader fileUpload = new FileUploader () ;
        File file = new File("C:/win.jpg") ;



        FileFilter jpg = new FiltreSimple("Fichiers jpg",".jpg");
        FileFilter jpeg = new FiltreSimple("Fichiers jpeg",".jpeg");
        FileFilter png = new FiltreSimple("Fichiers png",".png");

        JFileChooser dialogue = new JFileChooser(new File("."));
        dialogue.setAccessory(new FilePreview(dialogue));
        dialogue.addChoosableFileFilter(jpg);
        dialogue.addChoosableFileFilter(jpeg);
        dialogue.addChoosableFileFilter(png);

        dialogue.setDialogTitle("Choisiez le fichier à envoyer sur CloudPix...");

        dialogue.setName("Choisiez le fichier à envoyer...");

        if (dialogue.showOpenDialog(null)==
                JFileChooser.APPROVE_OPTION) {
            String extention = FilenameUtils.getExtension(dialogue.getSelectedFile().getName());
            if (StringUtils.containsOnly(extention,"jpg")||StringUtils.containsOnly(extention,"jpeg")||StringUtils.containsOnly(extention,"png")){
                file = dialogue.getSelectedFile();
            }
        }
        //Upload the file
        //fileUpload.executeMultiPartRequest("http://localhost:8081/rest/upload/file/",
               // file, file.getName(), "File Uploaded :: win.jpg") ;
       // String test = upload("http://localhost:8081/rest/upload/file/", file);
        String test = upload("http://198.27.66.107:8082/rest/upload/file/", file);
    }
    public static String upload(String url, File uploadFile) throws IOException {
        WebResource resource = Client.create().resource(url);
        FormDataMultiPart form = new FormDataMultiPart();
        form.field("fileName", uploadFile.getName());
        FormDataBodyPart fdp = new FormDataBodyPart("content",
                new FileInputStream(uploadFile),
                MediaType.APPLICATION_OCTET_STREAM_TYPE);
        form.bodyPart(fdp);
        String response = resource.type(MediaType.MULTIPART_FORM_DATA).post(String.class, form);
        return response;
    }
    public void executeMultiPartRequest(String urlString, File file, String fileName, String fileDescription) throws Exception
    {
        HttpClient client = new DefaultHttpClient() ;
        HttpPost postRequest = new HttpPost (urlString) ;
        try
        {
            //Set various attributes
            MultipartEntity multiPartEntity = new MultipartEntity () ;
            multiPartEntity.addPart("fileDescription", new StringBody(fileDescription != null ? fileDescription : "")) ;
            multiPartEntity.addPart("fileName", new StringBody(fileName != null ? fileName : file.getName())) ;

            FileBody fileBody = new FileBody(file, "application/octect-stream") ;
            //Prepare payload
            multiPartEntity.addPart("attachment", fileBody) ;

            //Set to request body
            postRequest.setEntity(multiPartEntity) ;

            //Send request
            HttpResponse response = client.execute(postRequest) ;

            //Verify response if any
            if (response != null)
            {
                System.out.println(response.getStatusLine().getStatusCode());
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace() ;
        }
    }


}
