package com.gitlab.pedrioko.core.view.action;

import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.enums.CrudAction;
import com.gitlab.pedrioko.core.view.enums.CrudMode;
import com.gitlab.pedrioko.core.view.enums.FormStates;
import com.gitlab.pedrioko.core.view.enums.MessageType;
import com.gitlab.pedrioko.core.view.util.ZKUtil;
import com.gitlab.pedrioko.core.view.viewers.crud.CrudView;
import com.gitlab.pedrioko.services.CrudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zkoss.zul.Messagebox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DeleteAction implements Action {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteAction.class);

    @Autowired
    private CrudService crudservice;

    @Override
    public String getLabel() {
        return "";
    }

    @Override
    public String getIcon() {
        return "fa fa-trash";
    }

    @Override
    public void actionPerform(CrudActionEvent event) {
        CrudView crudViewParent = (CrudView) event.getCrudViewParent();
        Object value = event.getValue();
        ArrayList list = crudViewParent.getValue();
        if (value == null) {
            ZKUtil.showMessage(ReflectionZKUtil.getLabel("seleccione"), MessageType.WARNING);
        } else {
            try {
                Messagebox.show(ReflectionZKUtil.getLabel("eliminar") + " " + value.toString() + ", " + ReflectionZKUtil.getLabel("estaseguro"),
                        ReflectionZKUtil.getLabel("warning"), Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, e -> {
                            if (e.getName().equals("onOK")) {
                                if (crudViewParent.getCrudviewmode() == CrudMode.SUBCRUD || event.getFormstate() == FormStates.CREATE
                                        || event.getFormstate() == FormStates.UPDATE) {
                                    list.remove(value);
                                    crudViewParent.setValue(list);
                                }
                                if (crudViewParent.getCrudviewmode() == CrudMode.MAINCRUD) {
                                    crudservice.delete(value);
                                    crudViewParent.getCrudController().doQuery();
                                }
                                crudViewParent.update();
                                ZKUtil.showMessage(ReflectionZKUtil.getLabel("deleted"), MessageType.SUCCESS);
                            }
                        });
            } catch (Exception e) {
                LOGGER.error("ERROR on actionPerform()", e);
                ZKUtil.showMessage(ReflectionZKUtil.getLabel("errordelete"), MessageType.ERROR);
            }
        }
    }

    @Override
    public List<?> getAplicateClass() {
        return Arrays.asList(CrudAction.class);
    }

    @Override
    public String getClasses() {
        return "btn-danger";
    }

    @Override
    public FormStates getFormState() {
        return FormStates.DELETE;
    }

    @Override
    public Integer position() {
        return 3;
    }

    @Override
    public String getColor() {
        return "#e74c3c";
    }

    @Override
    public int getGroup() {
        return 0;
    }

    @Override
    public String getTooltipText() {
        return ReflectionZKUtil.getLabel("eliminar");
    }

    @Override
    public boolean isDefault() {
        return true;
    }
}
