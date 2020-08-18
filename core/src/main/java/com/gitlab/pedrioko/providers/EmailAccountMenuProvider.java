package com.gitlab.pedrioko.providers;

import com.gitlab.pedrioko.core.lang.AuditLog;
import com.gitlab.pedrioko.core.lang.Page;
import com.gitlab.pedrioko.core.lang.annotation.Menu;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.action.SaveAction;
import com.gitlab.pedrioko.core.view.action.TestEmailAccountAction;
import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.enums.FormStates;
import com.gitlab.pedrioko.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

import java.util.Arrays;
import java.util.HashMap;

@Menu
public class EmailAccountMenuProvider implements MenuProvider {
    private final MailService mailService;
    private final SaveAction saveAction;
    private final TestEmailAccountAction testEmailAccountAction;

    Page page = new Page("~./zul/forms/form.zul");

    public EmailAccountMenuProvider(MailService mailService, SaveAction saveAction, TestEmailAccountAction testEmailAccountAction) {
        this.mailService = mailService;
        this.saveAction = saveAction;
        this.testEmailAccountAction = testEmailAccountAction;
    }

    @Override
    public String getLabel() {
        return ReflectionZKUtil.getLabel("EmailAccount");
    }

    @Override
    public Page getView() {
        HashMap<Object, Object> arg = new HashMap<>();
        arg.put("actions-form", Arrays.asList(saveAction, testEmailAccountAction));
        arg.put("value", mailService.getEmailAccount());
        CrudActionEvent actionEvent = new CrudActionEvent();
        actionEvent.setFormstate(FormStates.UPDATE);
        arg.put("event-crud", actionEvent);
        arg.put("estado-form", FormStates.UPDATE);

        page.setArg(arg);
        return page;
    }

    @Override
    public String getIcon() {
        return "fas fa-envelope-square";
    }

    @Override
    public int getPosition() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String getGroup() {
        return "administracion";
    }
}
