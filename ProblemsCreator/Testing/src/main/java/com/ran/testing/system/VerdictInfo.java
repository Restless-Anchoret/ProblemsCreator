package com.ran.testing.system;

public class VerdictInfo implements Cloneable {

    public static final VerdictInfo VERDICT_NOT_TESTED = new VerdictInfo(Verdict.NOT_TESTED);
    public static final VerdictInfo VERDICT_COMPILE_ERROR = new VerdictInfo(Verdict.COMPILE_ERROR);
    public static final VerdictInfo VERDICT_TIME_LIMIT = new VerdictInfo(Verdict.TIME_LIMIT);
    public static final VerdictInfo VERDICT_MEMORY_LIMIT = new VerdictInfo(Verdict.MEMORY_LIMIT);
    public static final VerdictInfo VERDICT_SECUR_VIOL = new VerdictInfo(Verdict.SECUR_VIOL);
    public static final VerdictInfo VERDICT_FAIL = new VerdictInfo(Verdict.FAIL);

    private Verdict verdict;
    private Integer decisionTime;
    private Short decisionMemory;
    private Short points;
    private Integer wrongTestNumber;

    public VerdictInfo(Verdict verdict, Short points) {
        this.verdict = verdict;
        this.points = points;
    }

    public VerdictInfo(Verdict verdict) {
        this(verdict, null);
    }

    public Verdict getVerdict() {
        return verdict;
    }

    public Integer getDecisionTime() {
        return decisionTime;
    }

    public Short getDecisionMemory() {
        return decisionMemory;
    }

    public Short getPoints() {
        return points;
    }

    public Integer getWrongTestNumber() {
        return wrongTestNumber;
    }

    public VerdictInfo setDecisionTime(Integer decisionTime) {
        this.decisionTime = decisionTime;
        return this;
    }

    public VerdictInfo setDecisionMemory(Short decisionMemory) {
        this.decisionMemory = decisionMemory;
        return this;
    }

    public VerdictInfo setWrongTestNumber(Integer wrongTestNumber) {
        this.wrongTestNumber = wrongTestNumber;
        return this;
    }

    @Override
    public VerdictInfo clone() {
        return new VerdictInfo(this.verdict, this.points)
                .setDecisionTime(this.decisionTime)
                .setDecisionMemory(this.decisionMemory)
                .setWrongTestNumber(this.wrongTestNumber);
    }

}