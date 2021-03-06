package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.api.FieldFilterComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Combobox;

import java.lang.reflect.Field;
import java.util.Arrays;


@org.springframework.stereotype.Component
public class EnumFieldFilter implements FieldFilterComponent {

    @Override
    public Class[] getToClass() {
        return null;
    }

    @Override
    public Component getComponent(Field e) {
        Class<?> type = e.getType();
        if (type.isEnum()) {
            Combobox combobox = new Combobox();
            ReflectionZKUtil.populate(combobox, Arrays.asList(type.getEnumConstants()), true);
            return combobox;
        } else {
            return null;
        }
    }

}
