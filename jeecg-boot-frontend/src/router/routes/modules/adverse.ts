/**
 * 不良事件上报系统路由配置
 * @description 包含事件上报、科室审核、职能处理等功能模块
 * @author TC Agent
 * @since 2025-12-18
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
    // ========== 事件上报 ==========
    {
      path: 'event',
      name: 'AdverseEvent',
      component: () => import('/@/views/adverse/event/index.vue'),
      meta: {
        title: '我的上报',
        icon: 'ant-design:form-outlined',
      },
    },

    // ========== 药品不良反应上报 ==========
    {
      path: 'drug',
      name: 'AdverseDrug',
      redirect: '/adverse/drug/report',
      meta: {
        title: '药品不良反应',
        icon: 'ant-design:medicine-box-outlined',
      },
      children: [
        {
          path: 'report',
          name: 'AdverseDrugReport',
          component: () => import('/@/views/adverse/drug/index.vue'),
          meta: {
            title: '我的上报',
          },
        },
        {
          path: 'form',
          name: 'AdverseDrugForm',
          component: () => import('/@/views/adverse/drug/DrugReportForm.vue'),
          meta: {
            title: '报告表单',
            hideMenu: true,
            currentActiveMenu: '/adverse/drug/report',
          },
        },
        // ========== 药品常用配置 ==========
        {
          path: 'suspect',
          name: 'DrugCommonSuspect',
          component: () => import('/@/views/adverse/drug/suspect/index.vue'),
          meta: {
            title: '怀疑药品配置',
            icon: 'ant-design:database-outlined',
          },
        },
        {
          path: 'concomitant',
          name: 'DrugCommonConcomitant',
          component: () => import('/@/views/adverse/drug/concomitant/index.vue'),
          meta: {
            title: '并用药品配置',
            icon: 'ant-design:cluster-outlined',
          },
        },
      ],
    },

    // ========== 科室审核 ==========
    {
      path: 'audit',
      name: 'AdverseAudit',
      redirect: '/adverse/audit/pending',
      meta: {
        title: '科室审核',
        icon: 'ant-design:audit-outlined',
      },
      children: [
        {
          path: 'pending',
          name: 'AdverseAuditPending',
          component: () => import('/@/views/adverse/audit/PendingList.vue'),
          meta: {
            title: '待审核',
          },
        },
        {
          path: 'completed',
          name: 'AdverseAuditCompleted',
          component: () => import('/@/views/adverse/audit/CompletedList.vue'),
          meta: {
            title: '已审核',
          },
        },
      ],
    },

    // ========== 职能处理 ==========
    {
      path: 'process',
      name: 'AdverseProcess',
      redirect: '/adverse/process/pending',
      meta: {
        title: '职能处理',
        icon: 'ant-design:solution-outlined',
      },
      children: [
        {
          path: 'pending',
          name: 'AdverseProcessPending',
          component: () => import('/@/views/adverse/process/PendingList.vue'),
          meta: {
            title: '待处理',
          },
        },
        {
          path: 'confirm',
          name: 'AdverseProcessConfirm',
          component: () => import('/@/views/adverse/process/ConfirmList.vue'),
          meta: {
            title: '待确认',
          },
        },
        {
          path: 'closed',
          name: 'AdverseProcessClosed',
          component: () => import('/@/views/adverse/process/ClosedList.vue'),
          meta: {
            title: '已结案',
          },
        },
      ],
    },

    // ========== 整改管理 ==========
    {
      path: 'rectify',
      name: 'AdverseRectify',
      redirect: '/adverse/rectify/pending',
      meta: {
        title: '整改管理',
        icon: 'ant-design:tool-outlined',
      },
      children: [
        {
          path: 'pending',
          name: 'AdverseRectifyPending',
          component: () => import('/@/views/adverse/rectify/PendingList.vue'),
          meta: {
            title: '待整改',
          },
        },
      ],
    },

    // ========== 综合查询 ==========
    {
      path: 'query',
      name: 'AdverseQuery',
      component: () => import('/@/views/adverse/query/index.vue'),
      meta: {
        title: '综合查询',
        icon: 'ant-design:search-outlined',
      },
    },

    // ========== 统计分析 ==========
    {
      path: 'stat',
      name: 'AdverseStat',
      component: () => import('/@/views/adverse/stat/Dashboard.vue'),
      meta: {
        title: '统计分析',
        icon: 'ant-design:bar-chart-outlined',
      },
    },

    // ========== 系统配置 ==========
    {
      path: 'config',
      name: 'AdverseConfig',
      redirect: '/adverse/config/category',
      meta: {
        title: '系统配置',
        icon: 'ant-design:setting-outlined',
      },
      children: [
        {
          path: 'category',
          name: 'AdverseCategory',
          component: () => import('/@/views/adverse/category/index.vue'),
          meta: {
            title: '事件分类管理',
          },
        },
      ],
    },
  ],
};

export default adverse;
