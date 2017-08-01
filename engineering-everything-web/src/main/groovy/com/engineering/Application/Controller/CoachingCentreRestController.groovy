package com.engineering.Application.Controller

import api.CoachingCentreImages
import api.Coachingcentre
import api.Rating
import com.engineering.core.Service.EmailGenerationService
import com.engineering.core.Service.FilenameGenerator
import com.engineering.core.repositories.CoachingCentresRepository
import com.engineering.core.repositories.RatingRepository
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

/**
 * Created by GnyaniMac on 13/07/17.
 */
@RestController
class CoachingCentreRestController {

    @Autowired
    CoachingCentresRepository centresRepository

    @Autowired
    FilenameGenerator filenameGenerator

    @Autowired
    RatingRepository ratingRepository

    @Autowired
    EmailGenerationService emailGenerationService

    @Autowired
    @Qualifier("coachingcentres-files")
    GridFsTemplate gridFsTemplate

    @Value('${engineering.everything.host}')
    private String servicehost;


    @RequestMapping(value= "/coachingcentres/insert", method = RequestMethod.POST )
    public String insertCentre ( @RequestBody Coachingcentre coachingcentre )
    {
        def coachingcentreId = filenameGenerator.generateCoachingCentreId(coachingcentre)
        coachingcentre.setCaochingcentreId(coachingcentreId)
        def feesDetailsUrl = "http://"+servicehost+":8080/coachingcentres/get/"+coachingcentreId+"/feedetails"
        coachingcentre.setFeesdetailsUrl(feesDetailsUrl)
        CoachingCentreImages images = new CoachingCentreImages()
        images.setCaochingcentreId(coachingcentreId)
        images.setFeedetails(coachingcentre.getFeedetails())
        InputStream inputStream = new ByteArrayInputStream(coachingcentre.getFeedetails())
        def y = gridFsTemplate.store(inputStream,coachingcentreId)
        def x = centresRepository.insert(coachingcentre)
        println(x)
        return( (x!=null && y!=null) ? "successfully inserted with Id ${x.getCaochingcentreId()}" : "sorry something went wrong")
    }
    @CrossOrigin(origins = ["http://localhost:8081","http://localhost:3000"])
    @RequestMapping(value = "/coachingcentres/get/{type:.+}",method = RequestMethod.GET )
    public Object getCoachingCentres (@PathVariable(value = "type", required = true) String caochingcentreId ){

        def coachingcentres = centresRepository.findBycoachingcentreIdLike(caochingcentreId)
        return (coachingcentres ? coachingcentres: "No data available")
    }

    @ResponseBody
    @CrossOrigin(origins = ["http://localhost:8081","http://localhost:3000"])
    @RequestMapping(value = "/coachingcentres/get/{coachingcentreId:.+}/feedetails",method = RequestMethod.GET,produces = "image/jpg" )
    public byte[] viewfeeDetails (@PathVariable(value = "coachingcentreId", required = true) String  coachingcentreId ){

        byte[] file = null;
        Query query = new Query().addCriteria(Criteria.where("filename").is(coachingcentreId))
        System.out.println("coaching centre id  is" + coachingcentreId);
        System.out.print("query is"+query);
        def list = gridFsTemplate.findOne(query)
        System.out.println("list is"  + list);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        list.writeTo(baos);
        file =baos.toByteArray();
        return file
    }

    @CrossOrigin(origins = ["http://localhost:8081","http://localhost:3000"])
    @RequestMapping(value = "/coachingcentres/post/{coachingcentreId:.+}" ,method = RequestMethod.POST )
    public String postRating (@PathVariable(value = "coachingcentreId" , required = true) String coachingcentreId, @RequestBody Rating  rating,OAuth2Authentication auth){
        String email = emailGenerationService.parseEmail(auth)
        rating.setCoachingcentreId(coachingcentreId)
        rating.setEmail(email)
        rating.setReviewID(coachingcentreId+email)
        ratingRepository.save(rating)
        def x = updateRating(coachingcentreId)
        return (x ? "success" : "something went wrong")
    }
    @CrossOrigin(origins = ["http://localhost:8081","http://localhost:3000"])
    @RequestMapping(value = "/coachingcentres/get/{coachingcentreId:.+}/reviews" ,method = RequestMethod.GET )
    public Object getReviews (@PathVariable(value = "coachingcentreId" , required = true) String coachingcentreId){

        def list = ratingRepository.findBycoachingcentreId(coachingcentreId)
        def finalist = []
        list.each{

           if(it.review.length()!=0)
               finalist.add(it)
        }
        return  finalist;

    }


   public boolean updateRating(String coachingcentreId){
       def list = ratingRepository.findBycoachingcentreId(coachingcentreId)
       float actualrating = 0
       list.each {
           actualrating += it.getRating()
       }
       actualrating = actualrating/list.size()
       def coachingcentre = centresRepository.findBycoachingcentreId(coachingcentreId)
       coachingcentre.setRating(actualrating)
       def x = centresRepository.save(coachingcentre)
       return(x ? true: false)
   }



}
