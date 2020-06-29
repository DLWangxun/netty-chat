package com.xun.wang.vlog.chat.model.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @param <T>
 */
@Data
@AllArgsConstructor
public class ServiceMultiResult<T> {
    private long total;
    private List<T> result;


    public int getResultSize() {
        if (this.result == null) {
            return 0;
        }
        return this.result.size();
    }
}
