package com.gitlab.pedrioko.core.view.api;

import org.zkoss.zk.ui.Component;

import java.lang.reflect.Field;

public interface FieldComponent {

    public Class[] getToClass();

    public Component getComponent(Field e);
}
