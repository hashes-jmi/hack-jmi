package com.abdulwd.lifeline.models;

/**
 * Created by abdul on 24/3/18.
 */

public class Record {
    private String problem, hospital, department;

    public Record(String problem, String hospital, String department) {
        this.problem = problem;
        this.hospital = hospital;
        this.department = department;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
