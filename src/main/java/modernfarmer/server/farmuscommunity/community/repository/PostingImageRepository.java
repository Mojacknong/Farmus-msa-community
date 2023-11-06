package modernfarmer.server.farmuscommunity.community.repository;

import modernfarmer.server.farmuscommunity.community.entity.PostingImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PostingImageRepository extends JpaRepository<PostingImage, Long> {
}
