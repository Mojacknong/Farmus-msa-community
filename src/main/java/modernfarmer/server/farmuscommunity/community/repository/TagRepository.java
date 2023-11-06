package modernfarmer.server.farmuscommunity.community.repository;

import modernfarmer.server.farmuscommunity.community.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    @Modifying
    @Query("select t.id from Tag as t where t.tagName= :tagName")
    String selectTag(@Param("tagName") String tagName);

    Tag findTagByAndTagName(String tagName);
}
