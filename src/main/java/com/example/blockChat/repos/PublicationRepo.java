package com.example.blockChat.repos;

import com.example.blockChat.domain.IPublication;
import com.example.blockChat.domain.Publication;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface PublicationRepo extends CrudRepository<Publication, Long> {
    List<Publication> findByTag(String tag);

    @Query(value = """
                     select
                        p.id,
                        p.tag,
                        p.text,
                        p.user_id,
                        p.filename,
                        p.post_time,
                        p.last_view,
                        count(m.id) as countNewMessage
                    from publication p
                    left outer join message m on p.id = m.publication_id
                    where m.send_time is not null and coalesce(m.send_time, CURRENT_TIMESTAMP) >= coalesce(p.last_view, coalesce(m.send_time,CURRENT_TIMESTAMP)) and
                    p.user_id = :id
                    group by p.id
                  
                                                          """, nativeQuery = true)
    Set<IPublication> findAllPublicationWithNewMessage(@Param("id") long id);

    @Query(value = """
                     select
                        count(m.id) as countNewMessage
                    from publication p
                    left outer join message m on p.id = m.publication_id
                    where m.send_time is not null and coalesce(m.send_time, CURRENT_TIMESTAMP) >= coalesce(p.last_view, coalesce(m.send_time,CURRENT_TIMESTAMP)) and
                    p.user_id =:id
                    group by p.user_id
                  
                                                          """, nativeQuery = true)
    Long getCountNewComments(@Param("id") long id);

}

