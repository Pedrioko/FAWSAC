package com.gitlab.pedrioko.core.view.action;

import com.gitlab.pedrioko.core.lang.annotation.ToolAction;
import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.enums.CrudAction;
import com.gitlab.pedrioko.core.view.enums.FormStates;
import com.gitlab.pedrioko.core.view.enums.MessageType;
import com.gitlab.pedrioko.core.view.forms.EntityForm;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.ZKUtil;

import java.util.Arrays;
import java.util.List;

@ToolAction
public class EditAction implements Action {

    @Override
    public String getIcon() {
        return "fas fa-edit";
    }

    @Override
    public void actionPerform(CrudActionEvent event) {
        Object value = event.getValue();
        if (value == null) {
            ZKUtil.showMessage(ReflectionZKUtil.getLabel("seleccione"), MessageType.WARNING);
        } else {
            EntityForm formUtilsnew = new EntityForm(value.getClass());
            formUtilsnew.addAction(ApplicationContextUtils.getBean(SaveAction.class), event);
            formUtilsnew.addAction(ApplicationContextUtils.getBean(CancelAction.class), event);
            formUtilsnew.setEstado(FormStates.UPDATE);
            formUtilsnew.setValueForm(value);
            event.getCrudViewParent().setContent(formUtilsnew);
        }
    }

    @Override
    public List<?> getAplicateClass() {
        return Arrays.asList(CrudAction.class);
    }

    @Override
    public String getLabel() {
        return "";
    }

    @Override
    public String getClasses() {
        return "btn-primary";
    }

    @Override
    public FormStates getFormState() {
        return FormStates.UPDATE;
    }

    @Override
    public Integer position() {
        return 2;
    }

    @Override
    public String getColor() {
        return "#f1c40f";
    }

    @Override
    public int getGroup() {
        return 0;
    }

    @Override
    public String getTooltipText() {
        return ReflectionZKUtil.getLabel("editar");
    }

    @Override
    public boolean isDefault() {
        return true;
    }
}
