package modernfarmer.server.farmuscommunity.community.repository;

import modernfarmer.server.farmuscommunity.community.entity.Posting;
import modernfarmer.server.farmuscommunity.community.entity.PostingTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PostingTagRepository extends JpaRepository<PostingTag, Long> {

    @Modifying
    @Query("delete from PostingTag  as p where p.posting = :posting")
    void deleteTag( @Param("posting") Posting posting);
}
