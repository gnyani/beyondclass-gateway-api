package Service


/**
 * Created by GnyaniMac on 02/05/17.
 */


class filenamegenerator {
    private String university
    private String collegecode
    private String branch
    private String year
    private String sem
    private String subject

    filenamegenerator() {
    }

    public String generateName(String university, String collegecode, String branch, String year, String sem, String subject)
    {
        def filename =null;
        filename = university+"-"+collegecode+"-"+branch+"-"+year+"-"+sem+"-"+subject;
        return  filename;
    }
}
