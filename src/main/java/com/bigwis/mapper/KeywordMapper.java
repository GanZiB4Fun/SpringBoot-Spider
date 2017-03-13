package com.bigwis.mapper;

import com.bigwis.model.KeyWord;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by Administrator on 2017/3/8.
 */

@Mapper
public interface KeywordMapper {
    int insert(KeyWord keyWord);
}
