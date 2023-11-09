package modernfarmer.server.farmuscommunity.community.repository;

import modernfarmer.server.farmuscommunity.community.entity.ReportTag;
import modernfarmer.server.farmuscommunity.community.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReportTagRepository extends JpaRepository<ReportTag, Long> {

    List<ReportTag> findAllBy();

    @Query("select r.reportReason  from ReportTag as r where r.id= :reportTagId")
    String getReportTagName(@Param("reportTagId") Long reportTagId);


}
