
import com.engineering.core.Service.ServiceUtilities
import com.mongodb.BasicDBObject
import com.mongodb.DB
import com.mongodb.DBCursor
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;



public class QuestionPaperinsertUtility {
    public static void main(String[] args) {

        try {

            Mongo mongo = new Mongo("localhost", 27017);
            DB db = mongo.getDB("mydatabase");
            //	DBCollection collection = db.getCollection("images");



           // String newFileName = new ServiceUtilities().generateFileName("OU","VASV","CSE","JAVA","2009");

//            File imageFile = new File("/Users/GnyaniMac/Desktop/mywork/questionpapers/JAVA/OU-VASV-CSE-JAVA-2009.pdf");
//
//            // create a "photo" namespace
           GridFS gfsPhoto = new GridFS(db, "questionpapers");
//
//            // get image file from local drive
//            GridFSInputFile gfsFile = gfsPhoto.createFile(imageFile);
//
//            // set a new filename for identify purpose
//            gfsFile.setFilename(newFileName);
//
//
//            // save the image file into mongoDB
//            gfsFile.save();


            // print the result
            DBCursor cursor = gfsPhoto.getFileList();
            while (cursor.hasNext()) {
                cursor.next()
            }
            BasicDBObject regexQuery = new BasicDBObject();
            regexQuery.put("filename",
                    new BasicDBObject('$regex', "OU-*")
                            .append('$options', "i"));


            // get image file by it's filename
            GridFSDBFile imageForOutput = gfsPhoto.findOne(regexQuery);

            // save it into a new image file
            imageForOutput.writeTo("/Users/GnyaniMac/Desktop/mywork/regex.jpg");

// remove the image file from mongoDB
//			gfsPhoto.remove(gfsPhoto.findOne(newFileName));


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (MongoException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}