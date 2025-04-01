package com.toki.web.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toki.model.entity.*;
import com.toki.model.enums.ItemType;
import com.toki.web.admin.mapper.ApartmentInfoMapper;
import com.toki.web.admin.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.web.admin.vo.apartment.ApartmentItemVo;
import com.toki.web.admin.vo.apartment.ApartmentQueryVo;
import com.toki.web.admin.vo.apartment.ApartmentSubmitVo;
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

    private final GraphInfoService graphInfoService;
    private final ApartmentFacilityService apartmentFacilityService;
    private final ApartmentLabelService apartmentLabelService;
    private final ApartmentFeeValueService apartmentFeeValueService;

    private final ApartmentInfoMapper apartmentInfoMapper;

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
        final boolean isUpdate = apartmentSubmitVo.getId() != null;
        super.saveOrUpdate(apartmentSubmitVo);

        // 更新
        if (isUpdate) {
            // 更新,直接删除原有信息
            // 1.图片Graph 注意指定图片类型
            graphInfoService.remove(
                    new LambdaQueryWrapper<>(GraphInfo.class)
                            .eq(GraphInfo::getItemType, ItemType.APARTMENT)
                            .eq(GraphInfo::getItemId, apartmentSubmitVo.getId())
            );
            // 2.配套Facility
            apartmentFacilityService.remove(
                    new LambdaQueryWrapper<>(ApartmentFacility.class)
                            .eq(ApartmentFacility::getApartmentId, apartmentSubmitVo.getId())
            );
            // 3.标签Label
            apartmentLabelService.remove(
                    new LambdaQueryWrapper<>(ApartmentLabel.class)
                            .eq(ApartmentLabel::getApartmentId, apartmentSubmitVo.getId())
            );
            // 4.杂费值FeeValue
            apartmentFeeValueService.remove(
                    new LambdaQueryWrapper<>(ApartmentFeeValue.class)
                            .eq(ApartmentFeeValue::getApartmentId, apartmentSubmitVo.getId())
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
                GraphInfo graphInfo = BeanUtil.copyProperties(graphVo, GraphInfo.class);
                graphInfo.setItemType(ItemType.APARTMENT);
                graphInfo.setItemId(apartmentSubmitVo.getId());
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
                        .apartmentId(apartmentSubmitVo.getId())
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
                        .apartmentId(apartmentSubmitVo.getId())
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
                        .apartmentId(apartmentSubmitVo.getId())
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
}




