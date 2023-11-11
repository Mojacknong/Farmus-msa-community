package modernfarmer.server.farmuscommunity.community.repository;



import modernfarmer.server.farmuscommunity.community.entity.PostingReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostingReportRepository extends JpaRepository<PostingReport, Long> {



}
