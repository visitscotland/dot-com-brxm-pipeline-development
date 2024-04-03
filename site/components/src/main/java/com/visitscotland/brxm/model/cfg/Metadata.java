package com.visitscotland.brxm.model.cfg;

public class Metadata {

    private String version;

    private String branch;
    private String lastCommitAuthor;
    private String pr;


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getLastCommitAuthor() {
        return lastCommitAuthor;
    }

    public void setLastCommitAuthor(String lastCommitAuthor) {
        this.lastCommitAuthor = lastCommitAuthor;
    }

    public String getPr() {
        return pr;
    }

    public void setPr(String pr) {
        this.pr = pr;
    }
}
