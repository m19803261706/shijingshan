<template>
  <div class="stat-dashboard p-4">
    <!-- 时间筛选 -->
    <a-card size="small" class="mb-4">
      <a-space>
        <span>时间范围：</span>
        <a-range-picker
          v-model:value="dateRange"
          :presets="rangePresets"
          format="YYYY-MM-DD"
          valueFormat="YYYY-MM-DD"
          @change="handleDateChange"
        />
        <a-button type="primary" @click="handleRefresh" :loading="loading">
          <template #icon><ReloadOutlined /></template>
          刷新数据
        </a-button>
        <a-button @click="handleExport" :loading="exportLoading">
          <template #icon><DownloadOutlined /></template>
          导出报表
        </a-button>
      </a-space>
    </a-card>

    <!-- 统计概览卡片 -->
    <a-row :gutter="16" class="mb-4">
      <a-col :span="6">
        <a-card hoverable>
          <a-statistic
            title="事件总数"
            :value="overview.total"
            :value-style="{ color: '#1890ff' }"
          >
            <template #prefix>
              <FileTextOutlined />
            </template>
          </a-statistic>
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card hoverable>
          <a-statistic
            title="本月新增"
            :value="overview.monthlyNew"
            :value-style="{ color: '#52c41a' }"
          >
            <template #prefix>
              <RiseOutlined />
            </template>
            <template #suffix v-if="overview.growthRate !== undefined">
              <span class="growth-rate" :class="overview.growthRate >= 0 ? 'up' : 'down'">
                {{ overview.growthRate >= 0 ? '+' : '' }}{{ overview.growthRate }}%
              </span>
            </template>
          </a-statistic>
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card hoverable>
          <a-statistic
            title="待处理"
            :value="overview.pendingProcess"
            :value-style="{ color: '#faad14' }"
          >
            <template #prefix>
              <ClockCircleOutlined />
            </template>
          </a-statistic>
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card hoverable>
          <a-statistic
            title="待整改"
            :value="overview.pendingRectify"
            :value-style="{ color: '#ff4d4f' }"
          >
            <template #prefix>
              <ToolOutlined />
            </template>
          </a-statistic>
        </a-card>
      </a-col>
    </a-row>

    <!-- 图表区域 -->
    <a-row :gutter="16">
      <!-- 科室分布饼图 -->
      <a-col :span="12">
        <a-card title="科室分布" size="small" class="mb-4">
          <div ref="deptChartRef" class="chart-container"></div>
        </a-card>
      </a-col>

      <!-- 分类分布柱状图 -->
      <a-col :span="12">
        <a-card title="分类分布" size="small" class="mb-4">
          <div ref="categoryChartRef" class="chart-container"></div>
        </a-card>
      </a-col>

      <!-- 月度趋势折线图 -->
      <a-col :span="16">
        <a-card title="月度趋势" size="small" class="mb-4">
          <div ref="trendChartRef" class="chart-container"></div>
        </a-card>
      </a-col>

      <!-- 级别分布环形图 -->
      <a-col :span="8">
        <a-card title="级别分布" size="small" class="mb-4">
          <div ref="levelChartRef" class="chart-container"></div>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts" name="adverse-stat-dashboard" setup>
/**
 * 统计分析仪表盘
 * @description 展示事件统计图表
 * @author TC Agent
 * @since 2025-12-18
 */
import { ref, onMounted, onUnmounted, nextTick } from 'vue';
import { useMessage } from '/@/hooks/web/useMessage';
import {
  getStatOverview,
  getDeptDistribution,
  getCategoryDistribution,
  getMonthlyTrend,
  getLevelDistribution,
  exportStatReport,
  StatOverview,
} from '/@/api/adverse/stat';
import { downloadByData } from '/@/utils/file/download';
import dayjs, { Dayjs } from 'dayjs';
import * as echarts from 'echarts';
import {
  FileTextOutlined,
  RiseOutlined,
  ClockCircleOutlined,
  ToolOutlined,
  ReloadOutlined,
  DownloadOutlined,
} from '@ant-design/icons-vue';

const { createMessage } = useMessage();

// 加载状态
const loading = ref(false);
const exportLoading = ref(false);

// 时间范围
const dateRange = ref<[Dayjs, Dayjs]>([dayjs().startOf('year'), dayjs()]);

// 时间范围预设
const rangePresets = [
  { label: '本周', value: [dayjs().startOf('week'), dayjs()] },
  { label: '本月', value: [dayjs().startOf('month'), dayjs()] },
  { label: '本季度', value: [dayjs().startOf('quarter'), dayjs()] },
  { label: '本年', value: [dayjs().startOf('year'), dayjs()] },
];

// 统计概览数据
const overview = ref<StatOverview>({
  total: 0,
  monthlyNew: 0,
  pendingProcess: 0,
  pendingRectify: 0,
});

// 图表引用
const deptChartRef = ref<HTMLElement>();
const categoryChartRef = ref<HTMLElement>();
const trendChartRef = ref<HTMLElement>();
const levelChartRef = ref<HTMLElement>();

// ECharts 实例
let deptChart: echarts.ECharts | null = null;
let categoryChart: echarts.ECharts | null = null;
let trendChart: echarts.ECharts | null = null;
let levelChart: echarts.ECharts | null = null;

/**
 * 获取日期范围参数
 */
function getDateParams() {
  if (dateRange.value && dateRange.value.length === 2) {
    return {
      startDate: dayjs(dateRange.value[0]).format('YYYY-MM-DD'),
      endDate: dayjs(dateRange.value[1]).format('YYYY-MM-DD'),
    };
  }
  return {};
}

/**
 * 加载概览数据
 */
async function loadOverview() {
  try {
    const data = await getStatOverview(getDateParams());
    overview.value = data || {
      total: 0,
      monthlyNew: 0,
      pendingProcess: 0,
      pendingRectify: 0,
    };
  } catch (e) {
    console.error('加载概览数据失败', e);
  }
}

/**
 * 加载科室分布图表
 */
async function loadDeptChart() {
  if (!deptChartRef.value) return;

  try {
    const data = await getDeptDistribution(getDateParams());

    if (!deptChart) {
      deptChart = echarts.init(deptChartRef.value);
    }

    const option: echarts.EChartsOption = {
      tooltip: {
        trigger: 'item',
        formatter: '{b}: {c} ({d}%)',
      },
      legend: {
        orient: 'vertical',
        right: 10,
        top: 'center',
      },
      series: [
        {
          name: '科室分布',
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['40%', '50%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 10,
            borderColor: '#fff',
            borderWidth: 2,
          },
          label: {
            show: false,
          },
          emphasis: {
            label: {
              show: true,
              fontSize: 14,
              fontWeight: 'bold',
            },
          },
          labelLine: {
            show: false,
          },
          data: (data || []).map((item) => ({
            name: item.name,
            value: item.value,
          })),
        },
      ],
    };

    deptChart.setOption(option);
  } catch (e) {
    console.error('加载科室分布失败', e);
  }
}

/**
 * 加载分类分布图表
 */
async function loadCategoryChart() {
  if (!categoryChartRef.value) return;

  try {
    const data = await getCategoryDistribution(getDateParams());

    if (!categoryChart) {
      categoryChart = echarts.init(categoryChartRef.value);
    }

    const option: echarts.EChartsOption = {
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow',
        },
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true,
      },
      xAxis: {
        type: 'category',
        data: (data || []).map((item) => item.name),
        axisLabel: {
          rotate: 30,
          interval: 0,
        },
      },
      yAxis: {
        type: 'value',
      },
      series: [
        {
          name: '事件数量',
          type: 'bar',
          data: (data || []).map((item) => item.value),
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#83bff6' },
              { offset: 0.5, color: '#188df0' },
              { offset: 1, color: '#188df0' },
            ]),
          },
          emphasis: {
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#2378f7' },
                { offset: 0.7, color: '#2378f7' },
                { offset: 1, color: '#83bff6' },
              ]),
            },
          },
          barWidth: '60%',
        },
      ],
    };

    categoryChart.setOption(option);
  } catch (e) {
    console.error('加载分类分布失败', e);
  }
}

/**
 * 加载月度趋势图表
 */
async function loadTrendChart() {
  if (!trendChartRef.value) return;

  try {
    const year = dateRange.value?.[0] ? dayjs(dateRange.value[0]).year() : dayjs().year();
    const data = await getMonthlyTrend({ year });

    if (!trendChart) {
      trendChart = echarts.init(trendChartRef.value);
    }

    const option: echarts.EChartsOption = {
      tooltip: {
        trigger: 'axis',
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true,
      },
      xAxis: {
        type: 'category',
        boundaryGap: false,
        data: (data || []).map((item) => item.month),
      },
      yAxis: {
        type: 'value',
      },
      series: [
        {
          name: '事件数量',
          type: 'line',
          smooth: true,
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(24, 144, 255, 0.3)' },
              { offset: 1, color: 'rgba(24, 144, 255, 0.05)' },
            ]),
          },
          lineStyle: {
            width: 3,
            color: '#1890ff',
          },
          itemStyle: {
            color: '#1890ff',
          },
          data: (data || []).map((item) => item.count),
        },
      ],
    };

    trendChart.setOption(option);
  } catch (e) {
    console.error('加载月度趋势失败', e);
  }
}

/**
 * 加载级别分布图表
 */
async function loadLevelChart() {
  if (!levelChartRef.value) return;

  try {
    const data = await getLevelDistribution(getDateParams());

    if (!levelChart) {
      levelChart = echarts.init(levelChartRef.value);
    }

    const colorMap: Record<string, string> = {
      '一级': '#ff4d4f',
      '二级': '#fa8c16',
      '三级': '#1890ff',
      '四级': '#52c41a',
    };

    const option: echarts.EChartsOption = {
      tooltip: {
        trigger: 'item',
        formatter: '{b}: {c} ({d}%)',
      },
      legend: {
        bottom: '5%',
        left: 'center',
      },
      series: [
        {
          name: '级别分布',
          type: 'pie',
          radius: ['30%', '60%'],
          center: ['50%', '45%'],
          label: {
            show: true,
            formatter: '{b}\n{d}%',
          },
          data: (data || []).map((item) => ({
            name: item.name,
            value: item.value,
            itemStyle: {
              color: colorMap[item.name] || '#1890ff',
            },
          })),
        },
      ],
    };

    levelChart.setOption(option);
  } catch (e) {
    console.error('加载级别分布失败', e);
  }
}

/**
 * 加载所有数据
 */
async function loadAllData() {
  loading.value = true;
  try {
    await Promise.all([
      loadOverview(),
      loadDeptChart(),
      loadCategoryChart(),
      loadTrendChart(),
      loadLevelChart(),
    ]);
  } finally {
    loading.value = false;
  }
}

/**
 * 处理时间变化
 */
function handleDateChange() {
  loadAllData();
}

/**
 * 刷新数据
 */
function handleRefresh() {
  loadAllData();
}

/**
 * 导出报表
 */
async function handleExport() {
  try {
    exportLoading.value = true;
    const params = getDateParams();
    const data = await exportStatReport({ ...params, type: 'full' });
    const fileName = `不良事件统计报表_${dayjs().format('YYYYMMDD_HHmmss')}.xlsx`;
    downloadByData(data, fileName);
    createMessage.success('导出成功');
  } catch (e) {
    console.error('导出失败', e);
    createMessage.error('导出失败');
  } finally {
    exportLoading.value = false;
  }
}

/**
 * 窗口大小变化时重绘图表
 */
function handleResize() {
  deptChart?.resize();
  categoryChart?.resize();
  trendChart?.resize();
  levelChart?.resize();
}

// 初始化
onMounted(() => {
  nextTick(() => {
    loadAllData();
  });
  window.addEventListener('resize', handleResize);
});

// 清理
onUnmounted(() => {
  window.removeEventListener('resize', handleResize);
  deptChart?.dispose();
  categoryChart?.dispose();
  trendChart?.dispose();
  levelChart?.dispose();
});
</script>

<style scoped>
.stat-dashboard {
  background: #f0f2f5;
  min-height: 100%;
}

.p-4 {
  padding: 16px;
}

.mb-4 {
  margin-bottom: 16px;
}

.chart-container {
  height: 300px;
  width: 100%;
}

.growth-rate {
  font-size: 12px;
  margin-left: 8px;
}

.growth-rate.up {
  color: #52c41a;
}

.growth-rate.down {
  color: #ff4d4f;
}

:deep(.ant-card-hoverable:hover) {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}
</style>
