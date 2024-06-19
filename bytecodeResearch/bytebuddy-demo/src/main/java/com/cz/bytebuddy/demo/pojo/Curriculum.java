package com.cz.bytebuddy.demo.pojo;

import lombok.Builder;
import lombok.Data;

/**
 * code desc
 *
 * @author Zjianru
 */
@Data
@Builder
public class Curriculum {

    String name;
    int score;
    String teacher;
    String classTime;
}
