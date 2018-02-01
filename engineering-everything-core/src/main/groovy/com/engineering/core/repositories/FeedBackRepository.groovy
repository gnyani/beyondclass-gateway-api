package com.engineering.core.repositories

import api.feedback.ReportIssue
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * Created by GnyaniMac on 11/01/18.
 */
interface FeedBackRepository extends MongoRepository<ReportIssue,String>{

    ReportIssue findByEmail(String email)
}
