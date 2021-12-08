package main.repository;

import main.model.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends PagingAndSortingRepository<Tag, Long> {
    @Query("select distinct t from Tag t "
            + "inner join t.tagsToPosts t2p "
            + "inner join t2p.post p "
            + "where p.isActive = true "
            + "and p.moderationStatus = 'ACCEPTED' "
            + "and p.time < current_time "
            + "order by t.id desc")
    List<Tag> getAllTagsListSortedByIdDesc();

    @Query("select distinct t.id, t.name from Tag t "
            + "inner join t.tagsToPosts t2p "
            + "inner join t2p.post p "
            + "where t.name like %:query% "
            + "and p.isActive = true "
            + "and p.moderationStatus = 'ACCEPTED' "
            + "and p.time < current_time "
            + "order by t.id desc")
    List<Tag> getAllTagsListByQuerySortedByIdDesc(@Param("query") String query);

    @Query("select count(p.id) from TagToPost t2p "
            + "inner join t2p.post p "
            + "where p.isActive = true "
            + "and p.moderationStatus = 'ACCEPTED' "
            + "and p.time < current_time "
            + "and t2p.tag.id = :tagId "
            + "order by p.id")
    Integer getTagCountByTagId(@Param("tagId") long tagId);

    @Query("select max(p.id) from TagToPost t2p "
            + "inner join t2p.post p "
            + "where p.isActive = true "
            + "and p.moderationStatus = 'ACCEPTED' "
            + "and p.time < current_time "
            + "group by t2p.tag.id "
            + "order by t2p.tag.id asc")
    Integer getMaxTagCount(PageRequest pageRequest);

    Tag getTagByName(String currentTagName);
}
