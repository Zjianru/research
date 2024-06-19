package com.cz.bytebuddy.demo.pojo;

import lombok.*;

import java.util.List;
import java.util.Random;

/**
 * code desc
 *
 * @author Zjianru
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    private String name;
    private Long id;
    private List<Curriculum> Curriculums;


    // add method for test dynamic enhancement

    public User queryUserByName(String name) {
        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return User.builder()
                .name(name)
                .id(1L)
                .Curriculums(List.of(Curriculum.builder()
                        .name("math")
                        .score(100)
                        .teacher("teacher1")
                        .classTime("1-2")
                        .build()))
                .build();
    }

    public User queryUserById(Long id) {
        return User.builder()
                .name("cz")
                .id(id)
                .Curriculums(List.of(Curriculum.builder()
                        .name("math")
                        .score(100)
                        .teacher("teacher1")
                        .classTime("1-2")
                        .build()))
                .build();
    }


    public User queryUserByCurriculum(Curriculum curriculum) {
        return User.builder()
                .name("cz")
                .id(1L)
                .Curriculums(List.of(curriculum))
                .build();
    }




}
