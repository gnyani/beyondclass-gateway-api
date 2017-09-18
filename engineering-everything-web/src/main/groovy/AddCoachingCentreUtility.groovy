//import api.Coachingcentre
//import api.coachingcentres.ContactInfo
//import com.engineering.core.Service.FilenameGenerator
//import com.mongodb.*
//import com.mongodb.gridfs.GridFS
//import com.mongodb.gridfs.GridFSDBFile
//import com.mongodb.gridfs.GridFSInputFile
//import constants.Area
//import constants.City
//import constants.CoachingCentreType
//
//
//public class AddCoachingCentreUtility {
//    public static void main(String[] args) {
//
//        try {
//
//            Mongo mongo = new Mongo("localhost", 27017);
//            DB db = mongo.getDB("mydatabase");
//            //	DBCollection collection = db.getCollection("images");
//            Coachingcentre coachingcentre = new Coachingcentre();
//
//            coachingcentre.setType(CoachingCentreType.GMAT)
//
//            coachingcentre.setCity(City.HYD)
//
//            coachingcentre.setArea(Area.HYMN)
//
//            coachingcentre.setOrgname("Princeton")
//
//            coachingcentre.setDescription("some description about the centre")
//
//            ContactInfo contactInfo = new ContactInfo()
//
//            contactInfo.setEmail("testorg@gamail.com")
//
//            contactInfo.setMobileNumber("34567891234")
//
//            coachingcentre.setContactinfo(contactInfo)
//
//            File imageFile = new File("/Users/GnyaniMac/Desktop/mywork/testimage.jpg");
//
//            //init array with file length
//            byte[] bytesArray = new byte[(int) imageFile.length()];
//
//            FileInputStream fis = new FileInputStream(imageFile);
//
//            fis.read(bytesArray)
//
//            fis.close()
//
//            coachingcentre.setFeedetails(bytesArray)
//
//            String newFileName = new FilenameGenerator().generateCoachingCentreId(coachingcentre);
//            coachingcentre.setCaochingcentreId(newFileName)
//
//            // create a "photo" namespace
//            GridFS gfsPhoto = new GridFS(db, "");
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
//
//
//            // print the result
//            DBCursor cursor = gfsPhoto.getFileList();
//            while (cursor.hasNext()) {
//                System.out.println(cursor.next());
//            }
//            BasicDBObject regexQuery = new BasicDBObject();
//            regexQuery.put("filename",
//                    new BasicDBObject('$regex', "OU-*")
//                            .append('$options', "i"));
//
//            System.out.println(regexQuery.toString());
//
//
//            // get image file by it's filename
//            GridFSDBFile imageForOutput = gfsPhoto.findOne(regexQuery);
//
//            // save it into a new image file
//           // imageForOutput.writeTo("/Users/GnyaniMac/Desktop/mywork/regex.jpg");
//
//// remove the image file from mongoDB
////			gfsPhoto.remove(gfsPhoto.findOne(newFileName));
//
//            System.out.println("Done" + imageForOutput );
//
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        } catch (MongoException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//}