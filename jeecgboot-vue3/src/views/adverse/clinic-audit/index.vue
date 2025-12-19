<template>
  <div class="clinic-audit-container">
    <a-card :bordered="false">
      <a-result
        status="info"
        title="临床科室审核"
        sub-title="请从左侧菜单选择需要审核的报告类型"
      >
        <template #extra>
          <div class="audit-options">
            <a-card hoverable class="option-card" @click="goToDrugAudit">
              <template #cover>
                <div class="card-icon">
                  <Icon icon="ant-design:medicine-box-outlined" :size="48" />
                </div>
              </template>
              <a-card-meta title="药品不良反应审核" description="审核本科室上报的药品不良反应报告" />
            </a-card>
          </div>
        </template>
      </a-result>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
/**
 * 临床科室审核入口页面
 * @description 临床科室审核功能的入口，用于展示审核选项
 * @author TC Agent
 * @since 2025-12-19
 * @see https://github.com/m19803261706/shijingshan/issues/46
 */
import { useRouter } from 'vue-router';
import { Icon } from '/@/components/Icon';

const router = useRouter();

/**
 * 跳转到药品不良反应审核页面
 */
function goToDrugAudit() {
  router.push('/adverse/clinic-audit/drug');
}
</script>

<style lang="less" scoped>
.clinic-audit-container {
  padding: 16px;
}

.audit-options {
  display: flex;
  justify-content: center;
  gap: 24px;
  margin-top: 24px;
}

.option-card {
  width: 280px;
  cursor: pointer;
  transition: all 0.3s;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }

  .card-icon {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 120px;
    background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
    color: #fff;
  }
}
</style>
