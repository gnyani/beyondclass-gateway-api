package com.engineering.Application.Controller

import api.Question
import com.engineering.core.Service.EmailGenerationService
import com.engineering.core.Service.FilenameGenerator
import com.engineering.core.repositories.QuestionRepository
import com.engineering.core.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin(origins = ["http://localhost:8081","http://localhost:3000"])
@RequestMapping("/user/questions")
class QuestionController {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailGenerationService emailGenerationService;

    @Autowired
    FilenameGenerator filenameGenerator;

    @PostMapping("/ask")
    public ResponseEntity uploadQuestion(@RequestBody Question question, OAuth2Authentication oAuth2Authentication){
        def email = emailGenerationService.parseEmail(oAuth2Authentication)
        def user = userRepository.findByEmail(email)
        long dateTime = System.currentTimeMillis();
        String questionId = filenameGenerator.generateQuestionIdToSave(user,dateTime);
        question.setQuestionPaperId(questionId);
        question.setOp(user);
        question.setDateTime(dateTime);
        questionRepository.save(question);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/addAnswer")
    public ResponseEntity addAnswer(@RequestBody String[] details){
        Question question = questionRepository.find(details[0]);
        question.getAnswer().add(details[1]);
        questionRepository.save(question);
        return new ResponseEntity(HttpStatus.OK);
    }


    @GetMapping("/getAllQuestions")
    public ResponseEntity<?> getAllQuestions(){
        def list = questionRepository.findAll()
        return new ResponseEntity<>(list,HttpStatus.OK);
    }
}