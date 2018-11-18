package com.pm.intelligent.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

/**
 * Created by WeiSir on 2018/8/23.
 * 站台实体类
 *
 */

@Entity
public class HomeShelterEntity implements Parcelable {

    /**
     * shelterName : 欣园1
     * iccid : 1
     * projectId : 2
     * projectName : 武汉项目
     */

    @Id(autoincrement = true)
    private Long id;
    private String shelterName;
    @Unique
    private String iccid;
    private int projectId;
    private String projectName;


    protected HomeShelterEntity(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        shelterName = in.readString();
        iccid = in.readString();
        projectId = in.readInt();
        projectName = in.readString();
    }

    public static final Creator<HomeShelterEntity> CREATOR = new Creator<HomeShelterEntity>() {
        @Override
        public HomeShelterEntity createFromParcel(Parcel in) {
            return new HomeShelterEntity(in);
        }

        @Override
        public HomeShelterEntity[] newArray(int size) {
            return new HomeShelterEntity[size];
        }
    };

    @Override
    public String toString() {
        return "HomeShelterEntity{" +
                "id=" + id +
                ", shelterName='" + shelterName + '\'' +
                ", iccid='" + iccid + '\'' +
                ", projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                '}';
    }

    @Generated(hash = 301732792)
    public HomeShelterEntity(Long id, String shelterName, String iccid,
            int projectId, String projectName) {
        this.id = id;
        this.shelterName = shelterName;
        this.iccid = iccid;
        this.projectId = projectId;
        this.projectName = projectName;
    }
    @Generated(hash = 2014438849)
    public HomeShelterEntity() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getShelterName() {
        return this.shelterName;
    }
    public void setShelterName(String shelterName) {
        this.shelterName = shelterName;
    }
    public String getIccid() {
        return this.iccid;
    }
    public void setIccid(String iccid) {
        this.iccid = iccid;
    }
    public int getProjectId() {
        return this.projectId;
    }
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
    public String getProjectName() {
        return this.projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(shelterName);
        dest.writeString(iccid);
        dest.writeInt(projectId);
        dest.writeString(projectName);
    }
}
