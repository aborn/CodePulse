package com.github.aborn.codepulse.api.service;

import com.github.aborn.codepulse.api.CodePulseInfo;
import com.github.aborn.codepulse.api.CodePulseMapper;
import com.github.aborn.codepulse.common.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author aborn (jiangguobao)
 * @date 2023/02/10 10:30
 */
@Service
@RequiredArgsConstructor
public class CodePulseDataService implements DataService {
    private final CodePulseMapper codePulseMapper;

    public CodePulseInfo findById(long id) {
        CodePulseInfo flow = codePulseMapper.findById(id);
        if (flow == null) {
            throw new ResourceNotFoundException("id", id);
        }
        return flow;
    }
}
