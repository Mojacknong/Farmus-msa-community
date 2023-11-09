package modernfarmer.server.farmuscommunity.community.entity;

import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "posting")
public class Posting extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "posting_id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "title", nullable = false, length = 26)
    private String title;

    @Column(name = "contents", length = 501)
    private String contents;

    @OneToMany(mappedBy = "posting")
    private Set<PostingImage> postingImages = new LinkedHashSet<>();

    @OneToMany(mappedBy = "posting")
    private Set<PostingTag> postingTags = new LinkedHashSet<>();

    @OneToMany(mappedBy = "posting")
    private Set<Comment> comments = new LinkedHashSet<>();

}