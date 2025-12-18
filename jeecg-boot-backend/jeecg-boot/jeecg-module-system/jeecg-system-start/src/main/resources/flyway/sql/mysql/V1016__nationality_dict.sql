-- ========================================
-- 民族字典数据
-- 版本: V1016
-- 描述: 配置中国56个民族字典数据
-- 作者: TC Agent
-- 创建时间: 2025-12-19
-- ========================================

-- 民族字典主表
INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, type)
VALUES ('nationality', '民族', 'nationality', '中国56个民族', 0, 'admin', NOW(), 0);

-- 民族字典项
INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
VALUES
  ('nationality_01', 'nationality', '汉族', 'han', '汉族', 1, 1, 'admin', NOW()),
  ('nationality_02', 'nationality', '蒙古族', 'mongol', '蒙古族', 2, 1, 'admin', NOW()),
  ('nationality_03', 'nationality', '回族', 'hui', '回族', 3, 1, 'admin', NOW()),
  ('nationality_04', 'nationality', '藏族', 'tibetan', '藏族', 4, 1, 'admin', NOW()),
  ('nationality_05', 'nationality', '维吾尔族', 'uyghur', '维吾尔族', 5, 1, 'admin', NOW()),
  ('nationality_06', 'nationality', '苗族', 'miao', '苗族', 6, 1, 'admin', NOW()),
  ('nationality_07', 'nationality', '彝族', 'yi', '彝族', 7, 1, 'admin', NOW()),
  ('nationality_08', 'nationality', '壮族', 'zhuang', '壮族', 8, 1, 'admin', NOW()),
  ('nationality_09', 'nationality', '布依族', 'bouyei', '布依族', 9, 1, 'admin', NOW()),
  ('nationality_10', 'nationality', '朝鲜族', 'korean', '朝鲜族', 10, 1, 'admin', NOW()),
  ('nationality_11', 'nationality', '满族', 'manchu', '满族', 11, 1, 'admin', NOW()),
  ('nationality_12', 'nationality', '侗族', 'dong', '侗族', 12, 1, 'admin', NOW()),
  ('nationality_13', 'nationality', '瑶族', 'yao', '瑶族', 13, 1, 'admin', NOW()),
  ('nationality_14', 'nationality', '白族', 'bai', '白族', 14, 1, 'admin', NOW()),
  ('nationality_15', 'nationality', '土家族', 'tujia', '土家族', 15, 1, 'admin', NOW()),
  ('nationality_16', 'nationality', '哈尼族', 'hani', '哈尼族', 16, 1, 'admin', NOW()),
  ('nationality_17', 'nationality', '哈萨克族', 'kazakh', '哈萨克族', 17, 1, 'admin', NOW()),
  ('nationality_18', 'nationality', '傣族', 'dai', '傣族', 18, 1, 'admin', NOW()),
  ('nationality_19', 'nationality', '黎族', 'li', '黎族', 19, 1, 'admin', NOW()),
  ('nationality_20', 'nationality', '傈僳族', 'lisu', '傈僳族', 20, 1, 'admin', NOW()),
  ('nationality_21', 'nationality', '佤族', 'va', '佤族', 21, 1, 'admin', NOW()),
  ('nationality_22', 'nationality', '畲族', 'she', '畲族', 22, 1, 'admin', NOW()),
  ('nationality_23', 'nationality', '高山族', 'gaoshan', '高山族', 23, 1, 'admin', NOW()),
  ('nationality_24', 'nationality', '拉祜族', 'lahu', '拉祜族', 24, 1, 'admin', NOW()),
  ('nationality_25', 'nationality', '水族', 'shui', '水族', 25, 1, 'admin', NOW()),
  ('nationality_26', 'nationality', '东乡族', 'dongxiang', '东乡族', 26, 1, 'admin', NOW()),
  ('nationality_27', 'nationality', '纳西族', 'naxi', '纳西族', 27, 1, 'admin', NOW()),
  ('nationality_28', 'nationality', '景颇族', 'jingpo', '景颇族', 28, 1, 'admin', NOW()),
  ('nationality_29', 'nationality', '柯尔克孜族', 'kyrgyz', '柯尔克孜族', 29, 1, 'admin', NOW()),
  ('nationality_30', 'nationality', '土族', 'tu', '土族', 30, 1, 'admin', NOW()),
  ('nationality_31', 'nationality', '达斡尔族', 'daur', '达斡尔族', 31, 1, 'admin', NOW()),
  ('nationality_32', 'nationality', '仫佬族', 'mulao', '仫佬族', 32, 1, 'admin', NOW()),
  ('nationality_33', 'nationality', '羌族', 'qiang', '羌族', 33, 1, 'admin', NOW()),
  ('nationality_34', 'nationality', '布朗族', 'blang', '布朗族', 34, 1, 'admin', NOW()),
  ('nationality_35', 'nationality', '撒拉族', 'salar', '撒拉族', 35, 1, 'admin', NOW()),
  ('nationality_36', 'nationality', '毛南族', 'maonan', '毛南族', 36, 1, 'admin', NOW()),
  ('nationality_37', 'nationality', '仡佬族', 'gelao', '仡佬族', 37, 1, 'admin', NOW()),
  ('nationality_38', 'nationality', '锡伯族', 'xibe', '锡伯族', 38, 1, 'admin', NOW()),
  ('nationality_39', 'nationality', '阿昌族', 'achang', '阿昌族', 39, 1, 'admin', NOW()),
  ('nationality_40', 'nationality', '普米族', 'pumi', '普米族', 40, 1, 'admin', NOW()),
  ('nationality_41', 'nationality', '塔吉克族', 'tajik', '塔吉克族', 41, 1, 'admin', NOW()),
  ('nationality_42', 'nationality', '怒族', 'nu', '怒族', 42, 1, 'admin', NOW()),
  ('nationality_43', 'nationality', '乌孜别克族', 'uzbek', '乌孜别克族', 43, 1, 'admin', NOW()),
  ('nationality_44', 'nationality', '俄罗斯族', 'russian', '俄罗斯族', 44, 1, 'admin', NOW()),
  ('nationality_45', 'nationality', '鄂温克族', 'ewenki', '鄂温克族', 45, 1, 'admin', NOW()),
  ('nationality_46', 'nationality', '德昂族', 'deang', '德昂族', 46, 1, 'admin', NOW()),
  ('nationality_47', 'nationality', '保安族', 'bonan', '保安族', 47, 1, 'admin', NOW()),
  ('nationality_48', 'nationality', '裕固族', 'yugur', '裕固族', 48, 1, 'admin', NOW()),
  ('nationality_49', 'nationality', '京族', 'jing', '京族', 49, 1, 'admin', NOW()),
  ('nationality_50', 'nationality', '塔塔尔族', 'tatar', '塔塔尔族', 50, 1, 'admin', NOW()),
  ('nationality_51', 'nationality', '独龙族', 'derung', '独龙族', 51, 1, 'admin', NOW()),
  ('nationality_52', 'nationality', '鄂伦春族', 'oroqen', '鄂伦春族', 52, 1, 'admin', NOW()),
  ('nationality_53', 'nationality', '赫哲族', 'hezhen', '赫哲族', 53, 1, 'admin', NOW()),
  ('nationality_54', 'nationality', '门巴族', 'monba', '门巴族', 54, 1, 'admin', NOW()),
  ('nationality_55', 'nationality', '珞巴族', 'lhoba', '珞巴族', 55, 1, 'admin', NOW()),
  ('nationality_56', 'nationality', '基诺族', 'jino', '基诺族', 56, 1, 'admin', NOW());
