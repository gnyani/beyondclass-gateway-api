import com.engineering.core.Service.FilenameGenerator
import com.engineering.core.Service.ServiceUtilities
import com.mongodb.BasicDBObject
import com.mongodb.DB
import com.mongodb.DBCursor
import com.mongodb.Mongo
import com.mongodb.MongoException
import com.mongodb.gridfs.GridFS
import com.mongodb.gridfs.GridFSDBFile
import com.mongodb.gridfs.GridFSInputFile

/**
 * Created by GnyaniMac on 03/05/17.
 */
class   SyllabusInsertUtility {
    public static void main(String[] args) {

        try {

            Mongo mongo = new Mongo("localhost", 27017);
            DB db = mongo.getDB("mydatabase");
            //	DBCollection collection = db.getCollection("images");



//            String newFileName = new ServiceUtilities().generateFileName("OU","VASV","CSE","");
//
//            File imageFile = new File("/Users/GnyaniMac/Desktop/mywork/Syllabus/OU-VASV-CSE-2-2/DAA.pdf");
//
//            // create a "photo" namespace
              GridFS gfsPhoto = new GridFS(db, "syllabus");
//
//            // get image file from local drive
//            GridFSInputFile gfsFile = gfsPhoto.createFile(imageFile);
//
//            BasicDBObject regexQuery = new BasicDBObject();
//            regexQuery.put("filename",
//                    new BasicDBObject('$regex', "OU-*")
//                            .append('$options', "i"));
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
