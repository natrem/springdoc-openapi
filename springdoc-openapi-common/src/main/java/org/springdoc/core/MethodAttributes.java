package org.springdoc.core;

import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;

public class MethodAttributes {

    private String[] classProduces;
    private String[] classConsumes;
    private String[] methodProduces = {};
    private String[] methodConsumes = {};
    private boolean methodOverloaded;
    private boolean withApiResponseDoc;
    private JsonView jsonViewAnnotation;
    private JsonView jsonViewAnnotationForRequestBody;

    public MethodAttributes(String[] methodProducesNew) {
        this.methodProduces = methodProducesNew;
    }

    public MethodAttributes() {
    }

    public String[] getClassProduces() {
        return classProduces;
    }

    public void setClassProduces(String[] classProduces) {
        this.classProduces = classProduces;
    }

    public String[] getClassConsumes() {
        return classConsumes;
    }

    public void setClassConsumes(String[] classConsumes) {
        this.classConsumes = classConsumes;
    }

    public String[] getMethodProduces() {
        return methodProduces;
    }

    public String[] getMethodConsumes() {
        return methodConsumes;
    }


    public void calculateConsumesProduces(Method method) {
        PostMapping reqPostMappingMethod = AnnotatedElementUtils.findMergedAnnotation(method, PostMapping.class);
        if (reqPostMappingMethod != null) {
            fillMethods(reqPostMappingMethod.produces(), reqPostMappingMethod.consumes());
            return;
        }
        GetMapping reqGetMappingMethod = AnnotatedElementUtils.findMergedAnnotation(method, GetMapping.class);
        if (reqGetMappingMethod != null) {
            fillMethods(reqGetMappingMethod.produces(), reqGetMappingMethod.consumes());
            return;
        }
        DeleteMapping reqDeleteMappingMethod = AnnotatedElementUtils.findMergedAnnotation(method, DeleteMapping.class);
        if (reqDeleteMappingMethod != null) {
            fillMethods(reqDeleteMappingMethod.produces(), reqDeleteMappingMethod.consumes());
            return;
        }
        PutMapping reqPutMappingMethod = AnnotatedElementUtils.findMergedAnnotation(method, PutMapping.class);
        if (reqPutMappingMethod != null) {
            fillMethods(reqPutMappingMethod.produces(), reqPutMappingMethod.consumes());
            return;
        }
        RequestMapping reqMappingMethod = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
        RequestMapping reqMappingClass = AnnotatedElementUtils.findMergedAnnotation(method.getDeclaringClass(), RequestMapping.class);

        if (reqMappingMethod != null && reqMappingClass != null) {
            fillMethods(ArrayUtils.addAll(reqMappingMethod.produces(), reqMappingClass.produces()), ArrayUtils.addAll(reqMappingMethod.consumes(), reqMappingClass.consumes()));
        } else if (reqMappingMethod != null) {
            fillMethods(reqMappingMethod.produces(), reqMappingMethod.consumes());
        } else if (reqMappingClass != null) {
            fillMethods(reqMappingClass.produces(), reqMappingClass.consumes());
        }
    }

    private void fillMethods(String[] produces, String[] consumes) {
        methodProduces = ArrayUtils.isNotEmpty(produces) ? produces : new String[]{MediaType.ALL_VALUE};
        methodConsumes = ArrayUtils.isNotEmpty(consumes) ? consumes : new String[]{MediaType.ALL_VALUE};
    }

    public String[] getAllConsumes() {
        return ArrayUtils.addAll(methodConsumes, classConsumes);
    }

    public String[] getAllProduces() {
        return ArrayUtils.addAll(methodProduces, classProduces);
    }

    public boolean isMethodOverloaded() {
        return methodOverloaded;
    }

    public void setMethodOverloaded(boolean overloaded) {
        methodOverloaded = overloaded;
    }

    public void setWithApiResponseDoc(boolean withApiDoc) {
        this.withApiResponseDoc = withApiDoc;
    }

    public boolean isNoApiResponseDoc() {
        return !withApiResponseDoc;
    }

    public JsonView getJsonViewAnnotation() {
        return jsonViewAnnotation;
    }

    public void setJsonViewAnnotation(JsonView jsonViewAnnotation) {
        this.jsonViewAnnotation = jsonViewAnnotation;
    }

    public JsonView getJsonViewAnnotationForRequestBody() {
        if (jsonViewAnnotationForRequestBody == null)
            return jsonViewAnnotation;
        return jsonViewAnnotationForRequestBody;
    }

    public void setJsonViewAnnotationForRequestBody(JsonView jsonViewAnnotationForRequestBody) {
        this.jsonViewAnnotationForRequestBody = jsonViewAnnotationForRequestBody;
    }
}
