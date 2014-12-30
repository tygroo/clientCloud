
import java.io.File;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;

/**
 * Created by Bonheur on 30/12/2014.
 */
public class FileUploader {
    public static void main(String args[]) throws Exception
    {
        FileUploader fileUpload = new FileUploader () ;
        File file = new File("C:/win.jpg") ;
        //Upload the file
        fileUpload.executeMultiPartRequest("http://localhost:8081/rest/upload/image-upload",
                file, file.getName(), "File Uploaded :: win.jpg") ;
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
