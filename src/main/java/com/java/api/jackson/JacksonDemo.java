package com.java.api.jackson;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * ref:
 * 1. http://blog.csdn.net/clementad/article/details/46416647
 * 2. http://blog.csdn.net/accountwcx/article/details/24585987
 * 3. http://bijian1013.iteye.com/blog/2284401
 * <p>
 * Created by kevintian on 2017/9/22.
 */
public class JacksonDemo {
    public static void main(String[] args) throws Exception {
        JacksonDemo demo = new JacksonDemo();
        // para data
        Container javaObj = new Container();
        List<Element> elementList = new ArrayList<>();
        Map<String, Element> elementMap = new HashMap<>();
        long idx = 0;
        while (idx < 10) {
            Element element = new Element(++idx, String.valueOf(idx), new Date());
            elementList.add(element);
            elementMap.put(String.valueOf(idx), element);
        }
        javaObj.setId(0);
        javaObj.setName("javaObj");
        javaObj.setElementList(elementList);
        javaObj.setElementMap(elementMap);
        javaObj.setOthers("others");
        javaObj.setCreateTime(new Date());
        // do test
        System.out.println("***object 2 json: ");
        String json = demo.obj2Json(javaObj);
        System.out.println(json);
        System.out.println("***json 2 object: ");
        Container retObj = demo.json2Obj(json, Container.class);
        System.out.println(retObj);
        System.out.println("***json 2 JsonNode:");
        JsonNode retNode = demo.json2JsonNode(json);
        for(Field f : Container.class.getDeclaredFields()) {
            String fieldName = f.getName();
            System.out.println(String.format("%s=%s", fieldName, retNode.get(fieldName)));
        }
    }

    /**
     * convert java object 2 json string
     * @param obj
     * @return
     * @throws JsonProcessingException
     */
    String obj2Json(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

    /**
     * restore java object from its json string
     * CAUTIOUS!!!: the java-object-class must fulfill the default non-parameter constructor
     *
     * @param json
     * @param objCls
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> T json2Obj(String json, Class<T> objCls) throws IOException {
        return new ObjectMapper().readValue(json, objCls);
    }

    /**
     * convert json string to JsonNode, which can be used as a map
     * @param json
     * @return
     * @throws IOException
     */
    JsonNode json2JsonNode(String json) throws IOException {
        return new ObjectMapper().readTree(json);
    }

    static class Container {
        long id;
        String name;
        @JsonProperty("elements")
        List<Element> elementList;
        Map<String, Element> elementMap;
        @JsonFormat(pattern = "yyyy-MM-dd")
        Date createTime;
        @JsonIgnore
        String others;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Element> getElementList() {
            return elementList;
        }

        public void setElementList(List<Element> elementList) {
            this.elementList = elementList;
        }

        public Map<String, Element> getElementMap() {
            return elementMap;
        }

        public void setElementMap(Map<String, Element> elementMap) {
            this.elementMap = elementMap;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }

        public String getOthers() {
            return others;
        }

        public void setOthers(String others) {
            this.others = others;
        }

        @Override
        public String toString() {
            return "Container{" +
                    "id=" + id + ",\n" +
                    "name='" + name + "',\n" +
                    "elementList=" + elementList + ",\n" +
                    "elementMap=" + elementMap + ",\n" +
                    "createTime=" + createTime + ",\n" +
                    "others='" + others + "',\n" +
                    "}";
        }
    }

    static class Element {
        long id;
        String name;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        Date createTime;

        // required by json
        public Element() {
        }

        public Element(long id, String name, Date createTime) {
            this.id = id;
            this.name = name;
            this.createTime = createTime;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }

        @Override
        public String toString() {
            return "Element{" +
                    "id=" + id + "," +
                    "name='" + name + "'," +
                    "createTime=" + createTime + "," +
                    "}";
        }
    }
}
