package com.mxnavi.opengl4android.base;

import android.content.res.Resources;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * @author xuebb
 * @description mtl文件解析类
 * @date: 2019/6/20 14:09
 */
public class MtlLoaderUtil {

    private static final String TAG = "MtlLoaderUtil";


    /**
     * 加载材质的方法
     *
     * @param fname assets的mtl文件路径
     * @param res
     * @return
     */
    public static HashMap<String, MtlInfo> load(String fname, Resources res) throws Exception {
        // 材质数组
        HashMap<String, MtlInfo> mMTLMap = new HashMap<String, MtlInfo>();
        //
        if (res == null || TextUtils.isEmpty(fname)) {
            return mMTLMap;
        }
        //
        MtlInfo currMtlInfo = null;
        try {
            // 读取assets下文件
            InputStream in = res.getAssets().open(fname);
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader buffer = new BufferedReader(isr);
            // 行数据
            String line;
            //
            while ((line = buffer.readLine()) != null) {
                // Skip comments and empty lines.
                if (line.length() == 0 || line.charAt(0) == '#') {
                    continue;
                }
                //
                StringTokenizer parts = new StringTokenizer(line, " ");
                int numTokens = parts.countTokens();
                if (numTokens == 0) {
                    continue;
                }
                //
                String type = parts.nextToken();
                type = type.replaceAll("\\t", "");
                type = type.replaceAll(" ", "");
                switch (type) {
                    case MtlLoaderUtil.NEWMTL:
                        // 定义一个名为 'xxx'的材质
                        String name = parts.hasMoreTokens() ? parts.nextToken() : "def";
                        // 将上一个对象加入到列表中
                        if (currMtlInfo != null) {
                            mMTLMap.put(currMtlInfo.name, currMtlInfo);
                        }
                        // 创建材质对象
                        currMtlInfo = new MtlInfo();
                        // 材质对象名称
                        currMtlInfo.name = name;
                        break;
                    case MtlLoaderUtil.KA:
                        // 环境光
                        currMtlInfo.Ka_Color = getColorFromParts(parts);
                        break;
                    case MtlLoaderUtil.KD:
                        // 散射光
                        currMtlInfo.Kd_Color = getColorFromParts(parts);
                        break;
                    case MtlLoaderUtil.KS:
                        // 镜面光
                        currMtlInfo.Ks_Color = getColorFromParts(parts);
                        break;
                    case MtlLoaderUtil.NS:
                        // 高光调整参数
                        String ns = parts.nextToken();
                        currMtlInfo.ns = Float.parseFloat(ns);
                        break;
                    case MtlLoaderUtil.D:
                        // 溶解度，为0时完全透明，1完全不透明
                        currMtlInfo.alpha = Float.parseFloat(parts.nextToken());
                        break;
                    case MtlLoaderUtil.MAP_KA:
                        currMtlInfo.Ka_Texture = parts.nextToken();
                        break;
                    case MtlLoaderUtil.MAP_KD:
                        currMtlInfo.Kd_Texture = parts.nextToken();
                        break;
                    case MtlLoaderUtil.MAP_KS:
                        currMtlInfo.Ks_ColorTexture = parts.nextToken();
                        break;
                    case MtlLoaderUtil.MAP_NS:
                        currMtlInfo.Ns_Texture = parts.nextToken();
                        break;
                    case MtlLoaderUtil.MAP_D:
                    case MtlLoaderUtil.MAP_TR:
                        currMtlInfo.alphaTexture = parts.nextToken();
                        break;
                    case MtlLoaderUtil.MAP_BUMP:
                        currMtlInfo.bumpTexture = parts.nextToken();
                        break;
                    default:
                        break;
                }
            }
            if (currMtlInfo != null) {
                mMTLMap.put(currMtlInfo.name, currMtlInfo);
            }
            buffer.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            throw new Exception(e.getMessage(), e.getCause());
        }
        return mMTLMap;
    }

    //####################################################################################
    /**
     * 材质需解析字段
     */
    // 定义一个名为 'xxx'的材质
    private static final String NEWMTL = "newmtl";
    // 材质的环境光（ambient color）
    private static final String KA = "Ka";
    // 散射光（diffuse color）用Kd
    private static final String KD = "Kd";
    // 镜面光（specular color）用Ks
    private static final String KS = "Ks";
    // 反射指数 定义了反射高光度。该值越高则高光越密集，一般取值范围在0~1000。
    private static final String NS = "Ns";
    // 渐隐指数描述 参数factor表示物体融入背景的数量，取值范围为0.0~1.0，取值为1.0表示完全不透明，取值为0.0时表示完全透明。
    private static final String D = "d";
    // 滤光透射率
    private static final String TR = "Tr";
    // map_Ka，map_Kd，map_Ks：材质的环境（ambient），散射（diffuse）和镜面（specular）贴图
    private static final String MAP_KA = "map_Ka";
    private static final String MAP_KD = "map_Kd";
    private static final String MAP_KS = "map_Ks";
    private static final String MAP_NS = "map_Ns";
    private static final String MAP_D = "map_d";
    private static final String MAP_TR = "map_Tr";
    private static final String MAP_BUMP = "map_Bump";

    /**
     * 返回一个oxffffffff格式的颜色值
     *
     * @param parts
     * @return
     */
    private static int getColorFromParts(StringTokenizer parts) {
        int r = (int) (Float.parseFloat(parts.nextToken()) * 255f);
        int g = (int) (Float.parseFloat(parts.nextToken()) * 255f);
        int b = (int) (Float.parseFloat(parts.nextToken()) * 255f);
        return Color.rgb(r, g, b);
    }

}
