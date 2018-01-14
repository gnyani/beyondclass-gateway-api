package com.engineering.core.repositories

import api.notes.Notes
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * Created by GnyaniMac on 01/07/17.
 */
public interface NotesRepository extends MongoRepository<Notes,String>{

    List<Notes> findByfilenameStartingWith(String filename)

}
