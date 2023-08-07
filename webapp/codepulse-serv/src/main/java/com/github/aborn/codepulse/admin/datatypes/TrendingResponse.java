package com.github.aborn.codepulse.admin.datatypes;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author aborn (jiangguobao)
 * @date 2023/08/07 20:12
 */
@Data
@Builder
public class TrendingResponse implements Serializable {
    private static final long serialVersionUID = 7240365948051188412L;

    private List<Double> trendTimeList;
    private List<String> trendNameList;

    public void add(Double time, String name) {
        if (this.trendTimeList == null) {
            this.trendTimeList = new ArrayList<>();
        }

        this.trendTimeList.add(time);

        if (this.trendNameList == null) {
            this.trendNameList = new ArrayList<>();
        }
        this.trendNameList.add(name);
    }
}
