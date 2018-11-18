package com.pm.intelligent.bean;

/**
 * Created by WeiSir on 2018/9/10.
 */
public class ProjectEntity {
    private String projectName;
    private int projectId;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return "ProjectEntity{" +
                "projectName='" + projectName + '\'' +
                ", projectId=" + projectId +
                '}';
    }
}
