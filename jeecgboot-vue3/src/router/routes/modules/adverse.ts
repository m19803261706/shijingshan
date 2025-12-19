/**
 * 不良事件管理模块路由配置
 * @description 药品不良反应、输血使用不良事件上报管理
 * @author TC Agent
 * @since 2025-12-19
 * @updated 2025-12-20 添加输血使用不良事件路由
 */
import type { AppRouteModule } from '/@/router/types';
import { LAYOUT } from '/@/router/constant';

const adverse: AppRouteModule = {
  path: '/adverse',
  name: 'Adverse',
  component: LAYOUT,
  redirect: '/adverse/event',
  meta: {
    orderNo: 100,
    icon: 'ant-design:alert-outlined',
    title: '不良事件管理',
  },
  children: [
    // ========== 不良事件上报 ==========
    {
      path: 'event',
      name: 'AdverseEvent',
      component: () => import('/@/views/adverse/event/index.vue'),
      meta: {
        title: '不良事件上报',
        icon: 'ant-design:form-outlined',
      },
    },
    {
      path: 'event/form',
      name: 'AdverseEventForm',
      component: () => import('/@/views/adverse/event/EventForm.vue'),
      meta: {
        title: '不良事件表单',
        hideMenu: true,
        currentActiveMenu: '/adverse/event',
      },
    },
    // ========== 药品不良反应 ==========
    {
      path: 'drug',
      name: 'AdverseDrug',
      component: () => import('/@/views/adverse/drug/index.vue'),
      meta: {
        title: '药品不良反应',
        icon: 'ant-design:medicine-box-outlined',
      },
    },
    {
      path: 'drug/form',
      name: 'AdverseDrugForm',
      component: () => import('/@/views/adverse/drug/DrugReportForm.vue'),
      meta: {
        title: '药品不良反应表单',
        hideMenu: true,
        currentActiveMenu: '/adverse/drug',
      },
    },
    // ========== 输血使用不良事件 ==========
    {
      path: 'blood',
      name: 'AdverseBlood',
      redirect: '/adverse/blood/report',
      meta: {
        title: '输血不良事件',
        icon: 'ant-design:heart-outlined',
      },
      children: [
        {
          path: 'report',
          name: 'AdverseBloodReport',
          component: () => import('/@/views/adverse/blood/index.vue'),
          meta: {
            title: '我的报告',
          },
        },
        {
          path: 'form',
          name: 'AdverseBloodForm',
          component: () => import('/@/views/adverse/blood/BloodReportForm.vue'),
          meta: {
            title: '报告表单',
            hideMenu: true,
            currentActiveMenu: '/adverse/blood/report',
          },
        },
      ],
    },
    // ========== 临床科室审核 ==========
    {
      path: 'clinic-audit',
      name: 'AdverseClinicAudit',
      redirect: '/adverse/clinic-audit/drug',
      meta: {
        title: '临床科室审核',
        icon: 'ant-design:audit-outlined',
      },
      children: [
        {
          path: 'drug',
          name: 'AdverseClinicAuditDrug',
          component: () => import('/@/views/adverse/clinic-audit/drug/index.vue'),
          meta: {
            title: '药品不良反应审核',
            icon: 'ant-design:medicine-box-outlined',
          },
        },
        {
          path: 'blood',
          name: 'AdverseClinicAuditBlood',
          component: () => import('/@/views/adverse/clinic-audit/blood/index.vue'),
          meta: {
            title: '输血使用不良事件审核',
            icon: 'ant-design:heart-outlined',
          },
        },
      ],
    },
    // ========== 报告审核（职能科室） ==========
    {
      path: 'audit',
      name: 'AdverseAudit',
      component: () => import('/@/views/adverse/audit/index.vue'),
      meta: {
        title: '职能科室审核',
        icon: 'ant-design:safety-certificate-outlined',
      },
    },
    // ========== 药品科室处理（药剂科） ==========
    {
      path: 'drug-process',
      name: 'AdverseDrugProcess',
      component: () => import('/@/views/adverse/drug-process/index.vue'),
      meta: {
        title: '药品科室处理',
        icon: 'ant-design:experiment-outlined',
      },
    },
    // ========== 输血科处理 ==========
    {
      path: 'blood-process',
      name: 'AdverseBloodProcess',
      component: () => import('/@/views/adverse/blood-process/index.vue'),
      meta: {
        title: '输血科处理',
        icon: 'ant-design:heart-outlined',
      },
    },
    // ========== 临床科室整改 ==========
    {
      path: 'clinic-rectify',
      name: 'AdverseClinicRectify',
      redirect: '/adverse/clinic-rectify/drug',
      meta: {
        title: '临床科室整改',
        icon: 'ant-design:edit-outlined',
      },
      children: [
        {
          path: 'drug',
          name: 'AdverseClinicRectifyDrug',
          component: () => import('/@/views/adverse/clinic-rectify/index.vue'),
          meta: {
            title: '药品不良反应整改',
            icon: 'ant-design:medicine-box-outlined',
          },
        },
        {
          path: 'blood',
          name: 'AdverseClinicRectifyBlood',
          component: () => import('/@/views/adverse/clinic-rectify/blood/index.vue'),
          meta: {
            title: '输血使用不良事件整改',
            icon: 'ant-design:heart-outlined',
          },
        },
      ],
    },
    // ========== 事件处理 ==========
    {
      path: 'process',
      name: 'AdverseProcess',
      component: () => import('/@/views/adverse/process/index.vue'),
      meta: {
        title: '事件处理',
        icon: 'ant-design:tool-outlined',
      },
    },
    // ========== 整改追踪 ==========
    {
      path: 'rectify',
      name: 'AdverseRectify',
      component: () => import('/@/views/adverse/rectify/index.vue'),
      meta: {
        title: '整改追踪',
        icon: 'ant-design:check-circle-outlined',
      },
    },
    // ========== 事件分类 ==========
    {
      path: 'category',
      name: 'AdverseCategory',
      component: () => import('/@/views/adverse/category/index.vue'),
      meta: {
        title: '事件分类',
        icon: 'ant-design:appstore-outlined',
      },
    },
    // ========== 统计报表 ==========
    {
      path: 'stat',
      name: 'AdverseStat',
      component: () => import('/@/views/adverse/stat/index.vue'),
      meta: {
        title: '统计报表',
        icon: 'ant-design:bar-chart-outlined',
      },
    },
    // ========== 高级查询 ==========
    {
      path: 'query',
      name: 'AdverseQuery',
      component: () => import('/@/views/adverse/query/index.vue'),
      meta: {
        title: '高级查询',
        icon: 'ant-design:search-outlined',
      },
    },
  ],
};

export default adverse;
