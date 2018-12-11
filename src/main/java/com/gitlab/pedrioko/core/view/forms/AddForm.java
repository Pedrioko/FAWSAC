package com.gitlab.pedrioko.core.view.forms;

import com.gitlab.pedrioko.core.zk.component.ChosenFileEntityBox;

import java.util.LinkedHashMap;


public class AddForm extends CustomForm {
    private static final String AGREGAR = "Agregar";

    private static final long serialVersionUID = 1L;

    public AddForm(Class klass, String field) {
        super(klass, new LinkedHashMap<>());
       // if()
        this.addField(AGREGAR, ChosenFileEntityBox.class);

    }


}
