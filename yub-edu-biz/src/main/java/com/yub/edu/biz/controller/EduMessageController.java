package com.yub.edu.biz.controller;

import com.yub.common.model.Response;
import com.yub.edu.biz.dto.MessageQueryDTO;
import com.yub.edu.biz.entity.EduMessage;
import com.yub.edu.biz.mapper.EduMessageMapper;
import com.yub.framework.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 留言 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 */
@RestController
@RequestMapping("/edu/message")
@RequiredArgsConstructor
public class EduMessageController {

    private final EduMessageMapper eduMessageMapper;

    @PostMapping("/page")
    public Response<List<EduMessage>> page(@RequestBody MessageQueryDTO query) {
        return Response.success(eduMessageMapper.selectPage(query));
    }

    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable Long id) {
        eduMessageMapper.deleteById(id);
        return Response.success();
    }
}
