package com.gitlab.pedrioko.providers.menu;

import com.gitlab.pedrioko.core.lang.Page;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.api.Provider;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(0)
public class InicioMenuProvider implements MenuProvider {
    Page page = new Page("~./zul/content/dashboard/page.zul");

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getLabel() {
        return ReflectionZKUtil.getLabel("Dashboard");
    }

    @Override
    public Page getView() {
        return this.page;
    }

    @Override
    public String getIcon() {
        return "z-icon-home";
    }

    @Override
    public int getPosition() {
        return -1;
    }

    @Override
    public Class<? extends Provider> getGroup() {
        return null;
    }

    @Override
    public boolean isOpenByDefault() {
        return true;
    }
}
