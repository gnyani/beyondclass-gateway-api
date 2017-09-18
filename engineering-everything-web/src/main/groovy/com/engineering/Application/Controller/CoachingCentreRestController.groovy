package com.engineering.Application.Controller

import api.coachingcentres.CoachingCentreImages
import api.coachingcentres.Coachingcentre
import api.coachingcentres.Rating
import com.engineering.core.Service.ServiceUtilities
import com.engineering.core.repositories.CoachingCentresRepository
import com.engineering.core.repositories.RatingRepository
import javafx.geometry.Pos
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
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
    RatingRepository ratingRepository

    @Autowired
    ServiceUtilities serviceUtils

    @Autowired
    @Qualifier("coachingcentres-files")
    GridFsTemplate gridFsTemplate

    @Value('${engineering.everything.host}')
    private String servicehost;


    @PostMapping(value= "/coachingcentres/insert")
    public String insertCentre ( @RequestBody Coachingcentre coachingcentre )
    {
        def coachingcentreId = serviceUtils.generateFileName(coachingcentre.getType().toString(),coachingcentre.getCity().toString(),coachingcentre.getArea().toString(),coachingcentre.getOrgname())
        coachingcentre.setCoachingcentreId(coachingcentreId)
        def feesDetailsUrl = "http://${servicehost}:8080/coachingcentres/get/${coachingcentreId}/feedetails"
        coachingcentre.setFeesdetailsUrl(feesDetailsUrl)
        CoachingCentreImages images = new CoachingCentreImages()
        images.setCaochingcentreId(coachingcentreId)
        images.setFeedetails(coachingcentre.getFeedetails())
        InputStream inputStream = new ByteArrayInputStream(coachingcentre.getFeedetails())
        def y = gridFsTemplate.store(inputStream,coachingcentreId)
        def x = centresRepository.insert(coachingcentre)
        (x!=null && y!=null) ? "successfully inserted with Id ${x.getCoachingcentreId()}" : "sorry something went wrong"
    }
    @GetMapping(value = "/coachingcentres/get/{type:.+}" )
    public Object getCoachingCentres (@PathVariable(value = "type", required = true) String caochingcentreId ){

        def coachingcentres = centresRepository.findBycoachingcentreIdStartingWith(caochingcentreId)
         (coachingcentres ? coachingcentres: "No data available")
    }

    @ResponseBody
    @GetMapping(value = "/coachingcentres/get/{coachingcentreId:.+}/feedetails",produces = "image/jpg" )
    public ResponseEntity<?> viewfeeDetails (@PathVariable(value = "coachingcentreId", required = true) String  coachingcentreId ){

        byte[] file
        Query query = new Query().addCriteria(Criteria.where("filename").is(coachingcentreId))
        def list = gridFsTemplate.findOne(query)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        list ?. writeTo(baos);
        file =baos ?. toByteArray();
        new ResponseEntity<>(file,HttpStatus.OK)
    }

    @PostMapping(value = "/coachingcentres/post/{coachingcentreId:.+}" )
    public ResponseEntity<?> postRating (@PathVariable(value = "coachingcentreId" , required = true) String coachingcentreId, @RequestBody Rating  rating,OAuth2Authentication auth){
        String email = serviceUtils.parseEmail(auth)
        rating.setCoachingcentreId(coachingcentreId)
        rating.setEmail(email)
        rating.setReviewID(coachingcentreId+email)
        ratingRepository.save(rating)
        def x = updateRating(coachingcentreId)
        (x ? new ResponseEntity<>("success",HttpStatus.OK) : new ResponseEntity<>("something went wrong",HttpStatus.INTERNAL_SERVER_ERROR))
    }
    @GetMapping(value = "/coachingcentres/get/{coachingcentreId:.+}/reviews")
    public ResponseEntity<?> getReviews (@PathVariable(value = "coachingcentreId" , required = true) String coachingcentreId){

        def list = ratingRepository.findBycoachingcentreId(coachingcentreId)
        def finalist = []
        list.each{

           if(it.review.length()!=0)
               finalist.add(it)
        }
        new ResponseEntity<>(finalist,HttpStatus.OK)

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
       (x ? true: false)
   }



}
