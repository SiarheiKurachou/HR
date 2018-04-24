package by.molchanov.humanresources.controller;

import by.molchanov.humanresources.exception.CustomException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class RequestHolder {
    private Map<String, Object> requestAttribute = new HashMap<>();
    private Map<String, Object> sessionAttribute = new HashMap<>();
    private Map<String, String[]> requestParameter = new HashMap<>();

    public RequestHolder(HttpServletRequest request) {
        Object retrievedObject;
        String retrievedName;
        requestParameter = request.getParameterMap();
        HttpSession session = request.getSession();
        Enumeration<String> sessionAttributeNames = session.getAttributeNames();
        while (sessionAttributeNames.hasMoreElements()) {
            retrievedName = sessionAttributeNames.nextElement();
            retrievedObject = session.getAttribute(retrievedName);
            sessionAttribute.put(retrievedName, retrievedObject);
        }
        Enumeration<String> requestAttributeNames = request.getAttributeNames();
        while (requestAttributeNames.hasMoreElements()) {
            retrievedName = requestAttributeNames.nextElement();
            retrievedObject = request.getAttribute(retrievedName);
            requestAttribute.put(retrievedName, retrievedObject);
        }
    }

    public void addRequestAttribute(String key, Object value) {
        requestAttribute.put(key, value);
    }

    public void addSessionAttribute(String key, Object value) {
        sessionAttribute.put(key, value);
    }

    public Object getRequestAttribute(String key) {
        return requestAttribute.get(key);
    }

    public Object getSessionAttribute(String key) {
        return sessionAttribute.get(key);
    }

    public String getSingleRequestParameter(int position, String key) {
        return requestParameter.get(key)[position];
    }

    public String[] getRequestParameter(String key) {
        return requestParameter.get(key);
    }

    public void update(HttpServletRequest request) throws CustomException {
        String key;
        Object value;
        for (Map.Entry<String, Object> attribute : requestAttribute.entrySet()) {
            key = attribute.getKey();
            value = attribute.getValue();
            request.setAttribute(key, value);
        }
        HttpSession session = request.getSession();
        for (Map.Entry<String, Object> attribute : sessionAttribute.entrySet()) {
            key = attribute.getKey();
            value = attribute.getValue();
            if (value instanceof Serializable) {
                session.setAttribute(key, value);
            } else {
                throw new CustomException("Try to add non-serializable object to session!");
            }
        }
    }
}
