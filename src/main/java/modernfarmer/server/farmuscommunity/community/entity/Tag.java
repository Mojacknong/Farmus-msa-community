package modernfarmer.server.farmuscommunity.community.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tag")
public class Tag extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id", nullable = false)
    private Long id;

    @Column(name = "tag_name", nullable = false, length = 10)
    private String tagName;

    @OneToMany(mappedBy = "tag")
    private Set<PostingTag> postingTags = new LinkedHashSet<>();

}