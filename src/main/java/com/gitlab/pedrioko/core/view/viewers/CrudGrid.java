package com.gitlab.pedrioko.core.view.viewers;

import com.gitlab.pedrioko.core.lang.FileEntity;
import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.api.OnEvent;
import com.gitlab.pedrioko.core.view.enums.CrudEvents;
import com.gitlab.pedrioko.core.view.reflection.ReflectionJavaUtil;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.Validate;
import com.gitlab.pedrioko.core.zk.component.Carousel;
import com.gitlab.pedrioko.core.zk.component.Video;
import com.gitlab.pedrioko.core.zk.component.model.CarouselItem;
import com.gitlab.pedrioko.services.StorageService;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.*;

import java.util.*;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = false)
public class CrudGrid extends Grid {

    private static final long serialVersionUID = 1L;
    private final transient List listitems = new ArrayList();
    private final Map<CrudEvents, List<OnEvent>> onEvent = new HashMap<>();
    private Columns columns;
    private Rows rows;
    private Class<?> klass;
    private Object selectValue;
    private String selectValueUUID = "";
    private CrudMenuContext popup;
    private StorageService storageService;
    private int cellsInRow = 4;

    private @Getter
    Long imageHeight;

    public CrudGrid(Class<?> klass) {
        super();
        this.klass = klass;
        init(klass);
    }

    public CrudGrid(Class<?> klass, List<Class<?>> all) {
        super();
        listitems.clear();
        listitems.addAll(all);
        init(klass);
    }

    private void init(Class<?> klass) {
        columns = new Columns();
        columns.setParent(this);
        rows = new Rows();
        rows.setParent(this);
        columns.appendChild(new Column("Prueba"));
        this.klass = klass;
        if (ReflectionZKUtil.isInEventListener()) {
            setAutopaging(true);
            setMold("paging");
            setPageSize(24);
        }
        setVflex("1");
        setHflex("1");
        setStyle("height:98;");
        rows.setHeight("135px");
        popup = new CrudMenuContext(klass, ApplicationContextUtils.getBeans(Action.class));
        storageService = ApplicationContextUtils.getBean(StorageService.class);
        imageHeight = 100L;
    }

    private void loadItems() {
        if (listitems != null) {
            this.getRows().getChildren().clear();
            int counter = 0;
            Row row = new Row();
            Map<String, Object> map = Validate.getGridViewFieldPreview(klass);
            Object value = map.get("value");
            String gridViewFieldName = Validate.getGridViewFieldName(klass);
            int size = listitems.size();
            for (int i = 0; i < size; i++) {
                Vlayout child = new Vlayout();
                Object obj = listitems.get(i);

                Object valueFieldObject = ReflectionJavaUtil.getValueFieldObject((String) value, obj);

                if ((boolean) map.get("isList")) {
                    List<FileEntity> listfiles = (List) valueFieldObject;
                    if (!listfiles.isEmpty()) {
                        Carousel carousel = new Carousel();
                        carousel.setCarouselItems(listfiles.stream().sorted(Comparator.comparingLong(x -> x.getId())).map(e -> {
                            CarouselItem carouselItem = new CarouselItem();
                            String url = ApplicationContextUtils.getBean(StorageService.class).getUrlFile(e.getFilename());
                            carouselItem.setEnlargedSrc(url);
                            carouselItem.setEnlargedHeight(imageHeight + "px");

                            return carouselItem;
                        }).collect(Collectors.toList()));
                        child.appendChild(carousel);

                    } else {
                        String urlFile = "/statics/files/" + ReflectionJavaUtil.getValueFieldObject((String) map.get("replaceValue"), obj);
                        if (urlFile.endsWith(".gif")) {
                            Image image = new Image();
                            image.setClass("img-responsive");
                            image.setStyle("margin: auto;");
                            image.setSrc(urlFile);
                            image.setHeight(imageHeight.toString() + "px");
                            child.appendChild(image);
                        } else {

                            Video image = new Video();
                            image.setSrc(urlFile);
                            //image.setClass("img-responsive");
                            //image.setStyle("margin: 0 auto; background: black;");
                            image.setHeight(imageHeight.toString() + "px");
                            child.appendChild(image);
                        }
                    }
                } else {
                    String urlFile = storageService.getUrlFile(((FileEntity) valueFieldObject));
                    Image image = new Image();
                    image.setClass("img-responsive");
                    image.setSrc(urlFile);
                    image.setStyle("margin: auto;");
                    image.setHeight(imageHeight.toString() + "px");
                    child.appendChild(image);
                }


                child.setClass("crud-grid-item");
                child.appendChild(new Label((String) ReflectionJavaUtil.getValueFieldObject(gridViewFieldName, obj)));
                child.setHeight("auto");
                int finalI = i;
                Div div = new Div();
                div.appendChild(child);
                child.addEventListener(Events.ON_CLICK, (e) -> {
                    onClick(child, finalI);
                    popup.open(child, "after_end");
                    getEvent(CrudEvents.ON_CLICK).forEach(OnEvent::doSomething);
                });
                child.addEventListener(Events.ON_RIGHT_CLICK, (e) -> {
                    onClick(child, finalI);
                    popup.open(child, "after_end");
                    getEvent(CrudEvents.ON_RIGHT_CLICK).forEach(OnEvent::doSomething);
                });
                row.appendChild(div);
                row.setHeight("auto");
                counter++;

                if (counter == this.cellsInRow || (size < this.cellsInRow && counter == size) || (i == size - 1)) {
                    rows.appendChild(row);
                    row = new Row();
                    counter = 0;
                }
            }
        }
        //  rows.getChildren().stream().sorted(Collections.reverseOrder());
        System.out.println("end load");

    }

    private boolean valideLimit(int base, int limit) {
        return base == limit - 1 ? true : valideLimit(base, limit - 1);
    }

    private void onClick(Vlayout child, int finalI) {
        selectValue = listitems.get(finalI);
        if (!selectValueUUID.isEmpty())
            Clients.evalJavaScript("var var1 = document.getElementById('" + selectValueUUID + "');" +
                    "if(var1 != null) {" +
                    "var1.style.border= '0px';" +
                    "}");


        Clients.evalJavaScript("document.getElementById('" + child.getUuid() + "').style.border=  '2px solid';" +
                "");

        selectValueUUID = child.getUuid();
    }

    private List<OnEvent> getEvent(CrudEvents events) {
        List<OnEvent> onEvents = this.onEvent.get(CrudEvents.ON_RIGHT_CLICK);
        return onEvents == null ? new ArrayList<>() : onEvents;
    }

    public <T> T getSelectedValue() {
        return (T) selectValue;
    }

    public void clearSelecion() {
        selectValue = null;
    }

    public List getValue() {
        return listitems;
    }

    public void setValue(List<?> all) {
        listitems.clear();
        listitems.addAll(all);
        loadItems();
    }


    public void addEventOnEvent(CrudEvents e, OnEvent o) {
        List<OnEvent> onEvents = this.onEvent.get(e);
        if (onEvents == null) {
            onEvents = new ArrayList<>();
        }
        onEvents.add(o);
        this.onEvent.put(e, onEvents);
    }

    public void update() {
        loadItems();

    }


    public void setImageHeight(long imageHeight) {
        this.imageHeight = imageHeight;
        update();
    }
}
