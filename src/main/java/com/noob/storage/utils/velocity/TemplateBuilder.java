package com.noob.storage.utils.velocity;

import org.apache.velocity.VelocityContext;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;


public class TemplateBuilder {

    public final static String MAPPER = "template.vm";
    public final static String MODEL = "model.vm";

    public static String build(TableConfig table, String templateName) {
        VelocityContext velocityContext = new VelocityContext();
        ByteArrayOutputStream temp = new ByteArrayOutputStream();
        OutputStreamWriter output = new OutputStreamWriter(temp);
        Template template = VelocityFactory.getInstances().getTemplate(templateName, output, velocityContext);
        velocityContext.put("table", table);
        try {
            template.outPut();
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] source;
        source = temp.toByteArray();
        String mapper = new String(source);
        return mapper;
    }
}
