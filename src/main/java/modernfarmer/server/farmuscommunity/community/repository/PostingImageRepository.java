package modernfarmer.server.farmuscommunity.community.repository;

import modernfarmer.server.farmuscommunity.community.entity.Posting;
import modernfarmer.server.farmuscommunity.community.entity.PostingImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PostingImageRepository extends JpaRepository<PostingImage, Long> {

    @Modifying
    @Query("delete from PostingImage  as p where p.posting = :posting and p.imageUrl = :imageUrl")
    void deleteImage(@Param("imageUrl") String imageUrl, @Param("posting") Posting posting);

}
