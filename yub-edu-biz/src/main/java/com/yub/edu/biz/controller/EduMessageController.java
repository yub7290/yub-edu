package com.yub.edu.biz.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yub.common.annotation.Log;
import com.yub.common.model.PageResult;
import com.yub.common.model.Response;
import com.yub.edu.biz.dto.MessageQueryDTO;
import com.yub.edu.biz.entity.EduMessage;
import com.yub.edu.biz.mapper.EduMessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 留言 Controller
 *
 * @Author: bing.yu
 * @CreateTime: 2026-06-15
 * @Description: 留言管理接口
 * @Version: 1.0.0
 */
@RestController
@RequestMapping("/edu/message")
@RequiredArgsConstructor
public class EduMessageController {

    private final EduMessageMapper eduMessageMapper;

    /**
     * 分页查询留言列表
     *
     * @param query 查询条件（含分页参数）
     * @return 分页结果
     */
    @PostMapping("/page")
    public Response<PageResult<EduMessage>> page(@RequestBody com.yub.common.model.PageQuery<MessageQueryDTO> query) {
        PageHelper.startPage(query.getPageParam().getPageNum(), query.getPageParam().getPageSize());
        List<EduMessage> list = eduMessageMapper.selectPage(query.getQueryParam());
        PageInfo<EduMessage> pageInfo = new PageInfo<>(list);
        return Response.success(PageResult.of(list, pageInfo.getTotal()));
    }

    /**
     * 删除留言
     *
     * @param id 留言ID
     * @return 操作结果
     */
    @Log(value = "删除留言", type = "DELETE")
    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable Long id) {
        EduMessage exist = eduMessageMapper.selectById(id);
        if (exist == null) {
            return Response.error(404, "留言不存在");
        }
        eduMessageMapper.deleteById(id);
        return Response.success();
    }
}
