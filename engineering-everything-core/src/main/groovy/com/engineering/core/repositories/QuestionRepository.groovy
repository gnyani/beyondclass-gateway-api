package com.engineering.core.repositories

import api.Organisation
import api.Question
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * Created by manoj on 22/7/17.
 */
interface QuestionRepository extends MongoRepository<Question, String> {

    public List<Question> findByQuestionPaperIdLike(String questionId);
    public Question findByQuestionPaperId(String questionId);
}
