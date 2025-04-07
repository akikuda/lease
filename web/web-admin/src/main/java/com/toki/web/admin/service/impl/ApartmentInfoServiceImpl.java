package com.toki.web.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toki.common.exception.LeaseException;
import com.toki.common.result.ResultCodeEnum;
import com.toki.model.entity.*;
import com.toki.model.enums.ItemType;
import com.toki.web.admin.mapper.*;
import com.toki.web.admin.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.web.admin.vo.apartment.ApartmentDetailVo;
import com.toki.web.admin.vo.apartment.ApartmentItemVo;
import com.toki.web.admin.vo.apartment.ApartmentQueryVo;
import com.toki.web.admin.vo.apartment.ApartmentSubmitVo;
import com.toki.web.admin.vo.fee.FeeValueVo;
import com.toki.web.admin.vo.graph.GraphVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author toki
 * 针对表【apartment_info(公寓信息表)】的数据库操作Service实现
 */
@Service
@RequiredArgsConstructor
public class ApartmentInfoServiceImpl extends ServiceImpl<ApartmentInfoMapper, ApartmentInfo>
        implements ApartmentInfoService {

    // 单表的用对应的Service即可
    private final GraphInfoService graphInfoService;
    private final ApartmentFacilityService apartmentFacilityService;
    private final ApartmentLabelService apartmentLabelService;
    private final ApartmentFeeValueService apartmentFeeValueService;
    private final RoomInfoService roomInfoService;

    // 涉及多表的，用对应的Mapper,自写sql
    private final ApartmentInfoMapper apartmentInfoMapper;
    private final GraphInfoMapper graphInfoMapper;
    private final LabelInfoMapper labelInfoMapper;
    private final FeeKeyMapper feeKeyMapper;
    private final FeeValueMapper feeValueMapper;
    private final FacilityInfoMapper facilityInfoMapper;

    /**
     * <p>
     * 插入或更新公寓信息 <br>
     * id存在则为更新，不存在则为插入 <br>
     * 逻辑： 若更新则删除原有信息，然后不管更新还是插入，都执行插入操作
     * </p>
     *
     * @param apartmentSubmitVo 公寓提交信息VO，同时包含了公寓的配套、标签、杂费值、图片等IdList
     */
    @Override
    public void saveOrUpdateApartment(ApartmentSubmitVo apartmentSubmitVo) {
        final Long apartmentId = apartmentSubmitVo.getId();
        final boolean isUpdate = apartmentId != null;
        super.saveOrUpdate(apartmentSubmitVo);

        // 更新
        if (isUpdate) {
            // 更新,直接删除原有信息
            // 1.图片Graph 注意指定图片类型
            graphInfoService.remove(
                    new LambdaQueryWrapper<>(GraphInfo.class)
                            .eq(GraphInfo::getItemType, ItemType.APARTMENT)
                            .eq(GraphInfo::getItemId, apartmentId)
            );
            // 2.配套Facility
            apartmentFacilityService.remove(
                    new LambdaQueryWrapper<>(ApartmentFacility.class)
                            .eq(ApartmentFacility::getApartmentId, apartmentId)
            );
            // 3.标签Label
            apartmentLabelService.remove(
                    new LambdaQueryWrapper<>(ApartmentLabel.class)
                            .eq(ApartmentLabel::getApartmentId, apartmentId)
            );
            // 4.杂费值FeeValue
            apartmentFeeValueService.remove(
                    new LambdaQueryWrapper<>(ApartmentFeeValue.class)
                            .eq(ApartmentFeeValue::getApartmentId, apartmentId)
            );
        }

        // 插入 ①获取列表 ②判空 ③遍历列表 ④VO转Entity ④插入
        // 1.图片列表GraphList
        final List<GraphVo> graphVoList = apartmentSubmitVo.getGraphVoList();
        if (!CollUtil.isEmpty(graphVoList)) {
            final ArrayList<GraphInfo> graphInfoList = new ArrayList<>();
            // 遍历VO
            for (GraphVo graphVo : graphVoList) {
                // VO转Entity
                GraphInfo graphInfo = CglibUtil.copy(graphVo, GraphInfo.class);
                graphInfo.setItemType(ItemType.APARTMENT);
                graphInfo.setItemId(apartmentId);
                // 加入列表
                graphInfoList.add(graphInfo);
            }
            // 批量插入
            graphInfoService.saveBatch(graphInfoList);
        }

        // 2.配套列表FacilityList
        final List<Long> facilityInfoIds = apartmentSubmitVo.getFacilityInfoIds();
        if (!CollUtil.isEmpty(facilityInfoIds)) {
            final ArrayList<ApartmentFacility> facilityList = new ArrayList<>();
            for (Long id : facilityInfoIds) {

                final ApartmentFacility apartmentFacility = ApartmentFacility.builder()
                        .facilityId(id)
                        .apartmentId(apartmentId)
                        .build();

                facilityList.add(apartmentFacility);
            }
            apartmentFacilityService.saveBatch(facilityList);
        }

        // 3.标签列表LabelList
        final List<Long> labelIds = apartmentSubmitVo.getLabelIds();
        if (!CollUtil.isEmpty(labelIds)) {
            final ArrayList<ApartmentLabel> labelList = new ArrayList<>();
            for (Long id : labelIds) {
                final ApartmentLabel apartmentLabel = ApartmentLabel.builder()
                        .labelId(id)
                        .apartmentId(apartmentId)
                        .build();
                labelList.add(apartmentLabel);
            }
            apartmentLabelService.saveBatch(labelList);
        }

        // 4.杂费值列表FeeValueList
        final List<Long> feeValueIds = apartmentSubmitVo.getFeeValueIds();
        if (!CollUtil.isEmpty(feeValueIds)) {
            final ArrayList<ApartmentFeeValue> feeValueList = new ArrayList<>();
            for (Long id : feeValueIds) {
                final ApartmentFeeValue apartmentFeeValue = ApartmentFeeValue.builder()
                        .feeValueId(id)
                        .apartmentId(apartmentId)
                        .build();
                feeValueList.add(apartmentFeeValue);
            }
            apartmentFeeValueService.saveBatch(feeValueList);
        }
    }

    @Override
    public IPage<ApartmentItemVo> pageItem(Page<ApartmentItemVo> page, ApartmentQueryVo queryVo) {
        return apartmentInfoMapper.pageItem(page, queryVo);
    }

    /**
     * 涉及多张表，但结果为单个对象，适合分别查询，然后用代码统一组装结果
     *
     * @param id 公寓id
     */
    @Override
    public ApartmentDetailVo getApartmentDetailById(Long id) {
        // 1.查询公寓信息
        final ApartmentInfo apartmentInfo = getById(id);
        if (apartmentInfo == null) {
            return null;
        }
        // 2.查询图片列表
        List<GraphVo> graphVoList = graphInfoMapper.selectListByItemTypeAndId(ItemType.APARTMENT, id);
        // 3.查询标签列表
        List<LabelInfo> labelInfoList = labelInfoMapper.selectListByApartmentId(id);
        // 4.查询配套列表
        List<FacilityInfo> facilityInfoList = facilityInfoMapper.selectListByApartmentId(id);
        // 5.查询杂费列表
        List<FeeValueVo> feeValueVoList = feeValueMapper.selectListByApartmentId(id);
        // 6.组装结果
        final ApartmentDetailVo apartmentDetailVo = CglibUtil.copy(apartmentInfo, ApartmentDetailVo.class);
        apartmentDetailVo.setGraphVoList(graphVoList);
        apartmentDetailVo.setLabelInfoList(labelInfoList);
        apartmentDetailVo.setFacilityInfoList(facilityInfoList);
        apartmentDetailVo.setFeeValueVoList(feeValueVoList);
        return apartmentDetailVo;
    }

    @Override
    public void removeApartmentById(Long id) {

        // 若公寓内有房间则不删除
        final long count = roomInfoService.count(new LambdaQueryWrapper<>(RoomInfo.class).eq(RoomInfo::getApartmentId, id));
        if (count > 0){
            // 直接抛出自定义异常让全局异常处理器处理
            throw new LeaseException(ResultCodeEnum.ADMIN_APARTMENT_DELETE_ERROR);
        }

        removeById(id);
        // 1.图片Graph 注意指定图片类型
        graphInfoService.remove(
                new LambdaQueryWrapper<>(GraphInfo.class)
                        .eq(GraphInfo::getItemType, ItemType.APARTMENT)
                        .eq(GraphInfo::getItemId, id)
        );
        // 2.配套Facility
        apartmentFacilityService.remove(
                new LambdaQueryWrapper<>(ApartmentFacility.class)
                        .eq(ApartmentFacility::getApartmentId, id)
        );
        // 3.标签Label
        apartmentLabelService.remove(
                new LambdaQueryWrapper<>(ApartmentLabel.class)
                        .eq(ApartmentLabel::getApartmentId, id)
        );
        // 4.杂费值FeeValue
        apartmentFeeValueService.remove(
                new LambdaQueryWrapper<>(ApartmentFeeValue.class)
                        .eq(ApartmentFeeValue::getApartmentId, id)
        );
    }
}




