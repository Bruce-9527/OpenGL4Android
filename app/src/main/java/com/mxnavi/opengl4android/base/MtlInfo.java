package com.mxnavi.opengl4android.base;

/**
 * @author xuebb
 * @description mtl文件信息保存类
 * @date: 2019/6/20 14:11
 */
public class MtlInfo {

    // 材质对象名称
    public String name;
    // 环境光
    public int Ka_Color;
    // 散射光
    public int Kd_Color;
    // 镜面光
    public int Ks_Color;
    // 高光调整参数
    public float ns;
    // 溶解度，为0时完全透明，1完全不透明
    public float alpha = 1f;
    // map_Ka，map_Kd，map_Ks：材质的环境（ambient），散射（diffuse）和镜面（specular）贴图
    public String Ka_Texture;
    public String Kd_Texture;
    public String Ks_ColorTexture;
    public String Ns_Texture;
    public String alphaTexture;
    public String bumpTexture;

}
