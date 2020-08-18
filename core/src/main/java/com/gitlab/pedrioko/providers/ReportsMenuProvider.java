package com.gitlab.pedrioko.providers;

import com.gitlab.pedrioko.core.lang.AuditLog;
import com.gitlab.pedrioko.core.lang.Page;
import com.gitlab.pedrioko.core.lang.Report;
import com.gitlab.pedrioko.core.lang.annotation.Menu;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.viewers.crud.CrudView;
import org.zkoss.zk.ui.Component;

@Menu
public class ReportsMenuProvider implements MenuProvider {
    Page page = new Page(Report.class);

    @Override
    public String getLabel() {
        return ReflectionZKUtil.getLabel("Print Reports");
    }

    @Override
    public Page getView() {
        return page;
    }

    @Override
    public String getIcon() {
        return "fas fa-print";
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
