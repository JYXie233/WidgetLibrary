package com.xjy.widget.example;

/**
 * User: Tom
 * Date: 2016-11-18
 * Time: 17:46
 * FIXME
 */
public class Model {
    public String name;

    public Class clazz;

    public Model(String name) {
        this.name = name;
    }

    public Model(String name, Class clazz) {
        this.name = name;
        this.clazz = clazz;
    }
}