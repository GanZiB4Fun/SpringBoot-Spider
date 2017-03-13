package com.bigwis.service.serviceImpl;

import com.bigwis.mapper.KeywordMapper;
import com.bigwis.model.KeyWord;
import com.bigwis.service.KeyWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/3/8.
 */
@Service
public class KeyWordServiceImpl implements KeyWordService {

    @Autowired
    private KeywordMapper keywordMapper;

    @Override
    public int insertKeyWord(KeyWord keyWord) {
        return keywordMapper.insert(keyWord);
    }

}
