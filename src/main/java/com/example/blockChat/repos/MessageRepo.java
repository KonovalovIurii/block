package com.example.blockChat.repos;

import com.example.blockChat.domain.Message;
import com.example.blockChat.domain.Publication;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface MessageRepo extends CrudRepository<Message, Long> {
    Set<Message> findByPublicationOrderById(Publication publication);

    @Query(value = """
                    select  count(m.id) as countNewMessage 
                   from chat ch
                   left outer join user_chat uc on ch.id = uc.chat_id
                   left outer join message m on ch.id = m.chat_id and m.user_id <> uc.user_id
                   where coalesce(m.send_time, CURRENT_TIME) >= coalesce(uc.last_view, coalesce(m.send_time,CURRENT_TIME)) and
                      uc.user_id =:id 
                   group by ch.id
                  
                                                          """, nativeQuery = true)
    Long  findAllCountNewMessage(@Param("id") long id);
}
