//package com.example.dto;
//
//import java.util.Map;
//
//public class CheckResult {
//    private final int correct;
//    private final int total;
//    private final Map<String, Boolean> perQuestion; // id -> true/false
//
//    public CheckResult(int correct, int total, Map<String, Boolean> perQuestion) {
//        this.correct = correct;
//        this.total = total;
//        this.perQuestion = perQuestion;
//    }
//
//    public int getCorrect() { return correct; }
//    public int getTotal() { return total; }
//    public Map<String, Boolean> getPerQuestion() { return perQuestion; }
//}
package com.example.dto;

import java.util.Map;

public class CheckResult {
    private final int correct;
    private final int total;
    private final Map<String, Boolean> perQuestion; // id -> true/false

    public CheckResult(int correct, int total, Map<String, Boolean> perQuestion) {
        this.correct = correct;
        this.total = total;
        this.perQuestion = perQuestion;
    }

    public int getCorrect() { return correct; }
    public int getTotal() { return total; }
    public Map<String, Boolean> getPerQuestion() { return perQuestion; }
}
