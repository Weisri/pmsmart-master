package com.pm.intelligent.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by WeiSir on 2018/7/4.
 * 搜索历史表
 */
@Entity
public class SearchHistory {
    @Unique
    private String text;

    @Generated(hash = 1483295534)
    public SearchHistory(String text) {
        this.text = text;
    }

    @Generated(hash = 1905904755)
    public SearchHistory() {
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
