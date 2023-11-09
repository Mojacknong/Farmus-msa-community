package modernfarmer.server.farmuscommunity.community.repository;

import modernfarmer.server.farmuscommunity.community.entity.Comment;
import modernfarmer.server.farmuscommunity.community.entity.Posting;
import modernfarmer.server.farmuscommunity.community.entity.PostingImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentRepository  extends JpaRepository<Comment, Long> {

    @Modifying
    @Query("update Comment as c set c.commentContents = :comment where c.userId = :userId and c.id = :commentId and c.posting = :posting")
    void updateComment(@Param("userId") Long userId, @Param("posting") Posting posting, @Param("commentId") Long commentId, @Param("comment") String comment);


    @Modifying
    @Query("delete from Comment as c where c.userId = :userId and c.id = :commentId and c.posting = :posting")
    void deleteComment(@Param("userId") Long userId, @Param("posting") Posting posting, @Param("commentId") Long commentId);



}
