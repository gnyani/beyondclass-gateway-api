package com.engineering.Application.Controller

import api.Question
import com.engineering.core.Service.ServiceUtilities
import com.engineering.core.Service.FilenameGenerator
import com.engineering.core.repositories.QuestionRepository
import com.engineering.core.repositories.UserRepository
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user/questions")
class QuestionController {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ServiceUtilities emailGenerationService;

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
    @ResponseBody
    public ResponseEntity addAnswer(@RequestBody String json){
        JsonSlurper jsonSlurper = new JsonSlurper();
        def parsedJson = jsonSlurper.parseText(json);
        List<String> details = parsedJson.detailsMap;
        Question question = questionRepository.findByQuestionPaperId(details.get(0));
        List<String> answers = question.getAnswer();
        answers.add(details.get(1));
        question.setAnswer(answers);
        questionRepository.save(question);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/getAllQuestions")
    public ResponseEntity<?> getAllQuestions(){
        def list = questionRepository.findAll()
        return new ResponseEntity<>(list,HttpStatus.OK);
    }
}