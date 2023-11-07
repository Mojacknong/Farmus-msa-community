package modernfarmer.server.farmuscommunity.community.repository;

import modernfarmer.server.farmuscommunity.community.entity.ReportTag;
import modernfarmer.server.farmuscommunity.community.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReportTagRepository extends JpaRepository<ReportTag, Long> {

    List<ReportTag> findAllBy();


}
