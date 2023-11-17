package modernfarmer.server.farmuscommunity.community.repository;



import modernfarmer.server.farmuscommunity.community.entity.Posting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostingRepository extends JpaRepository<Posting, Long> {

    @Modifying
    @Query("update Posting as p set p.title = :title, p.contents = :contents, p.tag = :tag where p.userId = :userId and p.id = :postingId")
    void updatePosting(@Param("userId") Long userId, @Param("title") String title,
                       @Param("contents") String contents, @Param("postingId") Long postingId,
                       @Param("tag") String tag
    );

    Optional<Posting> findById(Long postingId);

    @Query("select p  from Posting p join p.postingImages pi where p.id = :postingId")
    Optional<Posting> getPostingData(@Param("postingId") Long postingId);



    List<Posting> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("select p from Posting p " +
            "order by p.createdAt desc")
    List<Posting> allPostingSelect();





}
