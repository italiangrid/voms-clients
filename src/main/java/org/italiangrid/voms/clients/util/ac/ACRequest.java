package org.italiangrid.voms.clients.util.ac;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ACRequest {

  final String vo;
  final List<String> fqans;

  final Date startTime;
  final Date endTime;

  private ACRequest(Builder builder) {
    this.vo = builder.vo;
    this.fqans = builder.fqans;
    this.startTime = builder.startTime;
    this.endTime = builder.endTime;
  }
  
  public String getVo() {
    return vo;
  }


  public List<String> getFqans() {
    return fqans;
  }


  public Date getStartTime() {
    return startTime;
  }


  public Date getEndTime() {
    return endTime;
  }


  public static class Builder {
    String vo;
    List<String> fqans;
    Date startTime;
    Date endTime;

    public Builder(String vo) {
      this.vo = vo;
      fqans = new ArrayList<String>();
    }

    public Builder vo(String vo) {
      this.vo = vo;
      return this;
    }

    public Builder fqan(String fqan) {
      fqans.add(fqan);
      return this;
    }

    public Builder fqans(List<String> fqans) {
      fqans.addAll(fqans);
      return this;
    }

    public Builder startTime(Date startTime) {
      this.startTime = startTime;
      return this;
    }

    public Builder endTime(Date endTime) {
      this.endTime = endTime;
      return this;
    }

    public ACRequest build() {
      return new ACRequest(this);
    }
  }
  
}
