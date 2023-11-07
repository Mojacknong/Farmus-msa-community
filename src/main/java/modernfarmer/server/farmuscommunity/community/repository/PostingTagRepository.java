package modernfarmer.server.farmuscommunity.community.repository;

import modernfarmer.server.farmuscommunity.community.entity.PostingTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PostingTagRepository extends JpaRepository<PostingTag, Long> {

    @Modifying
    @Query("delete from PostingImage  as p where p.id = :postingId and p.imageUrl = :imageUrl")
    void deleteImage(@Param("imageUrl") String imageUrl, @Param("postingId") Long postingId);
}
