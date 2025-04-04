package com.toki.web.admin.schedule;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.toki.model.entity.LeaseAgreement;
import com.toki.model.enums.LeaseStatus;
import com.toki.web.admin.service.LeaseAgreementService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 每天0点检查租约状态是否到期，如果到期，则更新状态
 * @author toki
 */
@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final LeaseAgreementService leaseAgreementService;

    @Scheduled(cron = "0 0 0 * * *")
    public void checkLeaseStatus() {
        leaseAgreementService.update(
                new LambdaUpdateWrapper<>(LeaseAgreement.class)
                        .le(LeaseAgreement::getLeaseEndDate, new Date())
                        .in(LeaseAgreement::getStatus, LeaseStatus.SIGNED, LeaseStatus.WITHDRAWING)
                        .set(LeaseAgreement::getStatus, LeaseStatus.EXPIRED)
        );
    }
}
