package main.service;

import main.api.response.TagResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * The type Tag service.
 */
@Service
public class TagService {

    /**
     * The constant JAVA_WEIGHT.
     */
    public static final int JAVA_WEIGHT = 1;
    /**
     * The constant SPRING_WEIGHT.
     */
    public static final double SPRING_WEIGHT = 0.56;
    /**
     * The constant HIBERNATE_WEIGHT.
     */
    public static final double HIBERNATE_WEIGHT = 0.22;
    /**
     * The constant HADOOP_WEIGHT.
     */
    public static final double HADOOP_WEIGHT = 0.17;

    /**
     * Gets tag response.
     *
     * @return the tag response
     */
    public TagResponse getTagResponse() {
        TagResponse tagResponse = new TagResponse();

        TagResponse.Tag tag01 = new TagResponse.Tag();
        TagResponse.Tag tag02 = new TagResponse.Tag();
        TagResponse.Tag tag03 = new TagResponse.Tag();
        TagResponse.Tag tag04 = new TagResponse.Tag();

        ArrayList<TagResponse.Tag> tagArrayList = new ArrayList<>();

        tag01.setName("Java");
        tag01.setWeight(JAVA_WEIGHT);
        tag02.setName("Spring");
        tag02.setWeight(SPRING_WEIGHT);
        tag03.setName("Hibernate");
        tag03.setWeight(HIBERNATE_WEIGHT);
        tag04.setName("Hadoop");
        tag04.setWeight(HADOOP_WEIGHT);
        tagArrayList.add(tag01);
        tagArrayList.add(tag02);
        tagArrayList.add(tag03);
        tagArrayList.add(tag04);

        tagResponse.setTags(tagArrayList);

        return tagResponse;
    }
}
