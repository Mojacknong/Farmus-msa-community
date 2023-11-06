package modernfarmer.server.farmuscommunity.community.repository;

import modernfarmer.server.farmuscommunity.community.entity.PostingTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PostingTagRepository extends JpaRepository<PostingTag, Long> {
}
