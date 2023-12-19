package com.adventofcode.year2023;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Scanner;

class Day19Test {

    @Test
    void inputExample() {
        String input = """
                px{a<2006:qkq,m>2090:A,rfg}
                pv{a>1716:R,A}
                lnx{m>1548:A,A}
                rfg{s<537:gd,x>2440:R,A}
                qs{s>3448:A,lnx}
                qkq{x<1416:A,crn}
                crn{x>2662:A,R}
                in{s<1351:px,qqz}
                qqz{s>2770:qs,m<1801:hdj,R}
                gd{a>3333:R,R}
                hdj{m>838:A,pv}
                                
                {x=787,m=2655,a=1222,s=2876}
                {x=1679,m=44,a=2067,s=496}
                {x=2036,m=264,a=79,s=2244}
                {x=2461,m=1339,a=466,s=291}
                {x=2127,m=1623,a=2188,s=1013}""";

        {
            Scanner scanner = new Scanner(input);
            long sum = Day19.PartOne.sumAccepted(scanner);
            Assertions.assertThat(sum).isEqualTo(19114);
        }

        {
            Scanner scanner = new Scanner(input);
            long combinations = Day19.PartTwo.combinations(scanner);
            Assertions.assertThat(combinations).isEqualTo(167409079868000L);
        }
    }

    @Test
    void inputPartOne() throws IOException {
        try (InputStream inputStream = Day19Test.class.getResourceAsStream("/2023/day/19/input");
             Scanner scanner = new Scanner(Objects.requireNonNull(inputStream))) {
            long sum = Day19.PartOne.sumAccepted(scanner);
            Assertions.assertThat(sum).isEqualTo(319295);
        }
    }

    @Test
    void inputPartTwo() throws IOException {
        try (InputStream inputStream = Day19Test.class.getResourceAsStream("/2023/day/19/input");
             Scanner scanner = new Scanner(Objects.requireNonNull(inputStream))) {
            long combinations = Day19.PartTwo.combinations(scanner);
            Assertions.assertThat(combinations).isEqualTo(110807725108076L);
        }
    }

}
