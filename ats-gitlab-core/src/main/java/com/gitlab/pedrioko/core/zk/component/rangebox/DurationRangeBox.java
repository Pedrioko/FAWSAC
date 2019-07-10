package com.gitlab.pedrioko.core.zk.component.rangebox;

import com.gitlab.pedrioko.core.lang.DurationRange;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublebox;

import java.util.Arrays;

public class DurationRangeBox extends Div {
    private static final long serialVersionUID = -8290174092737445717L;
    Doublebox fin = new Doublebox();
    Doublebox inicio = new Doublebox();
    private DurationRange value;
    String[] sizes = new String[]{"SG", "MM", "HH", "DD"};
    private Combobox comboboxinicio;
    private Combobox comboboxfin;

    public DurationRangeBox() {
        value = new DurationRange();
        this.setSclass("ats-rangebox");
        load();

    }

    private void load() {
        inicio.setClass("ats-rangebox-input");
        fin.setClass("ats-rangebox-input");

        Div hlayout = new Div();
        hlayout.setClass("ats-rangebox-input-group");
        hlayout.appendChild(inicio);
        comboboxinicio = new Combobox();
        ReflectionZKUtil.populate(comboboxinicio, Arrays.asList(sizes), true);
        hlayout.appendChild(comboboxinicio);
        appendChild(hlayout);

        hlayout = new Div();
        hlayout.setClass("ats-rangebox-input-group");
        hlayout.appendChild(fin);
        comboboxfin = new Combobox();
        ReflectionZKUtil.populate(comboboxfin, Arrays.asList(sizes), true);
        hlayout.appendChild(comboboxfin);
        appendChild(hlayout);
    }

    public DurationRange getValue() {
        Double inicio = this.inicio.getValue();

        Double fin = this.fin.getValue();
        if (inicio != null && fin != null) {
            value = new DurationRange();
            value.setInicio(getSeconds(inicio, comboboxinicio.getValue()));
            value.setFin(getSeconds(fin, comboboxfin.getValue()));
            return value;
        } else {
            return null;
        }
    }

    private Double getSeconds(Double decimal, String metric) {
        switch (metric) {
            case ("SG"): {
                return decimal;
            }
            case ("MM"): {
                return decimal * 60;
            }
            case ("HH"): {
                return decimal * 60 * 60;
            }
            case ("DD"): {
                return decimal * 24 * 60 * 60;
            }

            default:
                return 0D;
        }
    }

    public void setValue(DurationRange value) {
        this.value = value;
        if (value != null) {
            value.setInicio(value.getInicio());
            comboboxinicio.setSelectedIndex(Arrays.asList(sizes).indexOf("SG"));
            value.setInicio(value.getFin());
            comboboxfin.setSelectedIndex(Arrays.asList(sizes).indexOf("SG"));
        } else {
            fin.setValue(null);
            inicio.setValue(null);
            comboboxinicio.setSelectedItem(null);
            comboboxfin.setSelectedItem(null);
            value.setInicio(null);
            value.setFin(null);
        }

    }
}
