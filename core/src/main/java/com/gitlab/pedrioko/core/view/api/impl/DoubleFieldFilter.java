package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.annotation.Duration;
import com.gitlab.pedrioko.core.lang.annotation.FileSize;
import com.gitlab.pedrioko.core.view.api.FieldFilterComponent;
import com.gitlab.pedrioko.core.zk.component.rangebox.DoubleRangeBox;
import com.gitlab.pedrioko.core.zk.component.rangebox.DurationRangeBox;
import com.gitlab.pedrioko.core.zk.component.rangebox.FileSizeRangeBox;
import org.zkoss.zk.ui.Component;

import java.lang.reflect.Field;

@org.springframework.stereotype.Component
public class DoubleFieldFilter implements FieldFilterComponent {

    @Override
    public Class[] getToClass() {
        return new Class[]{double.class, Double.class, float.class, Float.class};
    }

    @Override
    public Component getComponent(Field field) {
        if (field.isAnnotationPresent(FileSize.class)) return new FileSizeRangeBox();
        return field.isAnnotationPresent(Duration.class) ? new DurationRangeBox() : new DoubleRangeBox();
    }

}
