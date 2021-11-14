package main.repository;

import main.model.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TagRepository extends PagingAndSortingRepository<Tag, Long> {
    @Query(value = "SELECT DISTINCT t.* FROM tags AS t "
            + "INNER JOIN tag2post t2p ON t.id = t2p.tag_id "
            + "INNER JOIN posts p ON p.id = t2p.post_id "
            + "WHERE p.is_active = 1 "
            + "AND p.moderation_status = 'ACCEPTED' "
            + "AND p.time < NOW() "
            + "ORDER BY t.id DESC",
            nativeQuery = true)
    List<Tag> getAllTagsListSortedByIdDesc();

    @Query(value = "SELECT DISTINCT t.* FROM tags AS t "
            + "INNER JOIN tag2post t2p ON t.id = t2p.tag_id "
            + "INNER JOIN posts p ON p.id = t2p.post_id "
            + "WHERE (t.name LIKE CONCAT('%',?1,'%'))"
            + "AND p.is_active = 1 "
            + "AND p.moderation_status = 'ACCEPTED' "
            + "AND p.time < NOW() "
            + "ORDER BY t.id DESC",
            nativeQuery = true)
    List<Tag> getAllTagsListByQuerySortedByIdDesc(String query);

    @Query(value = "SELECT COUNT(*) "
            + "FROM tag2post tp "
            + "INNER JOIN posts p ON tp.post_id = p.id "
            + "WHERE p.is_active = 1 "
            + "AND p.moderation_status = 'ACCEPTED' "
            + "AND p.time < NOW() "
            + "AND tag_id = ?",
            nativeQuery = true)
    Integer getTagCountByTagId(int tagId);

    @Query(value = "SELECT MAX(tag_count) "
            + "FROM (SELECT COUNT(post_id) AS tag_count "
            + "FROM tag2post tp "
            + "INNER JOIN posts p ON tp.post_id = p.id "
            + "WHERE p.is_active = 1 "
            + "AND p.moderation_status = 'ACCEPTED' "
            + "AND p.time < NOW() "
            + "GROUP BY tp.tag_id) "
            + "AS max_tag_count",
            nativeQuery = true)
    Integer getMaxTagCount();

    @Query(
            value =
                    "SELECT * "
                            + "FROM tags t "
                            + "WHERE t.name = ?",
            nativeQuery = true)
    Tag getTagByTagName(String currentTagName);
}
