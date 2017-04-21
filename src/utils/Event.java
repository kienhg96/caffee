/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author hoangkien
 */
public class Event {
    private Object _obj;
    private String _type;
    
    public Event(Object obj) {
        this._obj = obj;
        this._type = "";
    }
    
    public Event(Object obj, String type) {
        this._obj = obj;
        this._type = type;
    }
    
    public Object target() {
        return this._obj;
    }
    
    public String type() {
        return this._type;
    }
}
