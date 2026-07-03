package com.yub.edu.biz.controller.app;

import com.yub.common.model.PageQuery;
import com.yub.common.model.PageResult;
import com.yub.common.model.Response;
import com.yub.edu.api.dto.app.ChangePasswordDTO;
import com.yub.edu.api.dto.app.MyCoursePageQueryDTO;
import com.yub.edu.api.dto.app.ProfileInfoUpdateDTO;
import com.yub.edu.api.vo.app.ExchangeResultVO;
import com.yub.edu.api.vo.app.LoginLogRespVO;
import com.yub.edu.api.vo.app.MyCourseVO;
import com.yub.edu.api.vo.app.PointsAccountVO;
import com.yub.edu.api.vo.app.PointsProductVO;
import com.yub.edu.api.vo.app.PointsRecordVO;
import com.yub.edu.api.vo.app.ProfileInfoRespVO;
import com.yub.edu.biz.dto.ExchangeReqDTO;
import com.yub.edu.biz.entity.EduAddress;
import com.yub.edu.biz.entity.EduPointsOrder;
import com.yub.edu.biz.entity.EduPointsProduct;
import com.yub.edu.biz.exception.EduErrorCode;
import com.yub.edu.biz.exception.EduException;
import com.yub.edu.biz.mapper.EduAddressMapper;
import com.yub.edu.biz.mapper.EduLoginLogMapper;
import com.yub.edu.biz.mapper.EduPointsProductMapper;
import com.yub.edu.biz.mapper.EduStudyCardInstanceMapper;
import com.yub.edu.biz.service.EduCourseService;
import com.yub.edu.biz.service.PointsOrderService;
import com.yub.edu.biz.service.PointsService;
import com.yub.edu.biz.service.StudentService;
import com.yub.framework.security.SecurityUtils;
import com.yub.framework.util.BeanUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: bing.yu
 * @CreateTime: 2026-07-02
 * @Description: APP-个人中心
 * @Version: 1.0
 */
@RestController
@RequestMapping("/app/profile")
@RequiredArgsConstructor
public class AppProfileController {

    private final EduCourseService courseService;
    private final StudentService studentService;
    private final EduLoginLogMapper eduLoginLogMapper;
    private final PointsService pointsService;
    private final EduPointsProductMapper eduPointsProductMapper;
    private final EduStudyCardInstanceMapper eduStudyCardInstanceMapper;
    private final PointsOrderService pointsOrderService;
    private final EduAddressMapper addressMapper;

    /**
     * 我的课程
     * @param pageQuery 分页查询参数
     * @return 分页结果
     */
    @PostMapping("/myCourse")
    public Response<PageResult<MyCourseVO>> myCourse(@RequestBody PageQuery<MyCoursePageQueryDTO> pageQuery) {
        return Response.success(courseService.myCourse(pageQuery));
    }

    /**
     * 获取个人信息
     * @return 个人信息
     */
    @GetMapping("/getInfo")
    public Response<ProfileInfoRespVO> getInfo() {
        Long userId = SecurityUtils.getCurrentUserId();
        return Response.success(studentService.getProfileInfo(userId));
    }

    /**
     * 更新个人信息
     * @param req 更新参数
     * @return 响应
     */
    @PostMapping("/updateInfo")
    public Response<Void> updateInfo(@RequestBody @Validated ProfileInfoUpdateDTO req) {
        Long userId = SecurityUtils.getCurrentUserId();
        studentService.updateProfileInfo(userId, req);
        return Response.success();
    }

    /**
     * 修改密码
     * @param req 密码参数
     * @return 响应
     */
    @PostMapping("/changePassword")
    public Response<Void> changePassword(@RequestBody @Validated ChangePasswordDTO req) {
        Long userId = SecurityUtils.getCurrentUserId();
        studentService.changePassword(userId, req.getOldPassword(), req.getNewPassword());
        return Response.success();
    }

    /**
     * 登录日志
     * @return 登录日志列表
     */
    @GetMapping("/loginLog")
    public Response<List<LoginLogRespVO>> loginLog() {
        Long userId = SecurityUtils.getCurrentUserId();
        List<LoginLogRespVO> list = BeanUtils.copyList(
                eduLoginLogMapper.selectByStudentId(userId, 50),
                LoginLogRespVO.class);
        return Response.success(list);
    }

    /**
     * 获取积分账户信息
     * @return 积分账户信息
     */
    @GetMapping("/points")
    public Response<PointsAccountVO> getPoints() {
        Long userId = SecurityUtils.getCurrentUserId();
        return Response.success(pointsService.getAccountInfo(userId));
    }

    /**
     * 分页查询积分记录
     * @param pageQuery 分页参数
     * @return 分页积分记录
     */
    @PostMapping("/points/records")
    public Response<PageResult<PointsRecordVO>> getPointsRecords(
            @RequestBody PageQuery<Void> pageQuery) {
        Long userId = SecurityUtils.getCurrentUserId();
        return Response.success(pointsService.getRecords(
                userId, pageQuery.getPageParam().getPageNum(), pageQuery.getPageParam().getPageSize()));
    }

    /**
     * 分页查询积分商品列表（上架且未删除）
     * @param pageQuery 分页参数
     * @return 分页商品列表
     */
    @PostMapping("/points/products")
    public Response<PageResult<PointsProductVO>> getPointsProducts(
            @RequestBody PageQuery<Void> pageQuery) {
        PageHelper.startPage(pageQuery.getPageParam().getPageNum(), pageQuery.getPageParam().getPageSize());
        List<EduPointsProduct> products = eduPointsProductMapper.selectPage(null, 1);
        PageInfo<EduPointsProduct> pageInfo = new PageInfo<>(products);
        List<PointsProductVO> voList = products.stream()
                .map(this::toProductVO)
                .collect(Collectors.toList());
        return Response.success(PageResult.of(voList, pageInfo.getTotal()));
    }

    /**
     * 积分兑换商品
     * @param req 兑换参数
     * @return 兑换结果（含兑换码或学习卡信息）
     */
    @PostMapping("/points/exchange")
    public Response<ExchangeResultVO> exchangeProduct(@RequestBody @Validated ExchangeReqDTO req) {
        Long userId = SecurityUtils.getCurrentUserId();
        EduPointsProduct product = eduPointsProductMapper.selectById(req.getProductId());
        if (product == null) {
            throw new EduException(EduErrorCode.POINTS_INSUFFICIENT);
        }
        String receiverName = req.getReceiverName();
        String receiverPhone = req.getReceiverPhone();
        String receiverAddress = req.getReceiverAddress();
        if (req.getAddressId() != null) {
            EduAddress address = addressMapper.selectById(req.getAddressId());
            if (address != null) {
                receiverName = address.getName();
                receiverPhone = address.getPhone();
                receiverAddress = address.getProvince() + address.getCity()
                        + address.getDistrict() + address.getDetail();
            }
        }
        int exchangeType = req.getExchangeType() != null ? req.getExchangeType() : 1;
        EduPointsOrder order = pointsOrderService.createOrder(
                userId, product, exchangeType,
                receiverName, receiverPhone, receiverAddress);
        ExchangeResultVO vo = new ExchangeResultVO();
        vo.setOrderNo(order.getOrderNo());
        vo.setExchangeCode(order.getExchangeCode());
        vo.setCardNo(order.getCardNo());
        vo.setCardSecret(order.getCardSecret());
        return Response.success(vo);
    }

    /**
     * 实体转商品VO
     * @param product 商品实体
     * @return 商品VO
     */
    private PointsProductVO toProductVO(EduPointsProduct product) {
        PointsProductVO vo = new PointsProductVO();
        vo.setId(product.getId());
        vo.setName(product.getName());
        vo.setProductType(product.getProductType());
        vo.setImageUrl(product.getImageUrl());
        vo.setRequiredPoints(product.getRequiredPoints());
        vo.setStockCount(product.getStockCount());
        return vo;
    }

}
