package com.gitlab.pedrioko.core.zk.component.crud;

import com.gitlab.pedrioko.core.view.viewers.crud.CrudView;
import org.zkoss.zul.Tabbox;

import java.util.List;

public class Crudbox extends Tabbox {


    public <T> T getValue() {
        return ((CrudView) getTabpanels().getChildren().get(0)).getValue();
    }

    public void setValue(List<?> value) {
        ((CrudView) getTabpanels().getChildren().get(0)).setValue(value);
    }


    public void setDisabled(boolean disable) {
        ((CrudView) getTabpanels().getChildren().get(0)).setDisabled(disable);
    }
}
