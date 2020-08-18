package com.gitlab.pedrioko.providers;

import com.gitlab.pedrioko.core.lang.Page;
import com.gitlab.pedrioko.core.lang.Report;
import com.gitlab.pedrioko.core.lang.annotation.Menu;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.viewers.crud.CrudView;
import com.gitlab.pedrioko.domain.Reporte;
import org.zkoss.zk.ui.Component;

@Menu
public class ReportesMenuProvider implements MenuProvider {

	Page page = new Page(Reporte.class);

	@Override
	public String getLabel() {
		return ReflectionZKUtil.getLabel("reportes");
	}

	@Override
	public Page getView() {
		return this.page;
	}

	@Override
	public String getIcon() {
		return "fas fa-chart-pie";
	}

	@Override
	public int getPosition() {
		return 1;
	}

	@Override
	public String getGroup() {
		return "administracion";
	}
}