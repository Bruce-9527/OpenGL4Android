package com.mxnavi.opengl4android.base;

import java.util.ArrayList;

/**
 * @author xuebb
 * @description obj文件信息类
 * @date: 2019/6/20 14:41
 */
public class ObjInfo {

    /**
     * 对象名称
     */
    public String name;
    /**
     * 材质
     */
    public MtlInfo mtlData;
    /**
     * 顶点、纹理、法向量一一对应后的数据
     */
    public float[] aVertices;
    // 顶点纹理可能会没有
    public float[] aTexCoords;
    public float[] aNormals;

    /**
     * index数组(顶点、纹理、法向量一一对应后，以下三个列表会清空)
     */
    // 顶点index数组
    public ArrayList<Integer> vertexIndices = new ArrayList<Integer>();
    // 纹理index数组
    public ArrayList<Integer> texCoordIndices = new ArrayList<Integer>();
    // 法向量index数组
    public ArrayList<Integer> normalIndices = new ArrayList<Integer>();


}
