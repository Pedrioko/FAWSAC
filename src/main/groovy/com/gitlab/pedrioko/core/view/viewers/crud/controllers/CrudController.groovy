package com.gitlab.pedrioko.core.view.viewers.crud.controllers

import com.gitlab.pedrioko.core.lang.DateRange
import com.gitlab.pedrioko.core.lang.annotation.CrudOrderBy
import com.gitlab.pedrioko.core.view.api.OnEvent
import com.gitlab.pedrioko.core.view.api.OnQuery
import com.gitlab.pedrioko.core.view.enums.CrudEvents
import com.gitlab.pedrioko.core.view.enums.MessageType
import com.gitlab.pedrioko.core.view.enums.ParamMode
import com.gitlab.pedrioko.core.view.reflection.ReflectionJavaUtil
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils
import com.gitlab.pedrioko.core.view.util.ZKUtil
import com.gitlab.pedrioko.core.view.viewers.crud.grid.AlphabetFilter
import com.gitlab.pedrioko.services.CrudService
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Predicate
import com.querydsl.core.types.dsl.CollectionPath
import com.querydsl.core.types.dsl.DatePath
import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.core.types.dsl.StringPath
import lombok.Getter
import lombok.Setter
import org.apache.commons.collections4.CollectionUtils

import java.lang.reflect.Field
import java.util.function.Function
import java.util.stream.Collectors

import static com.gitlab.pedrioko.core.view.reflection.ReflectionJavaUtil.getIdValue
import static com.gitlab.pedrioko.core.view.util.ApplicationContextUtils.getBean

public class CrudController {

    private static final long serialVersionUID = 1L
    private final Class<?> klass
    private final transient CrudService crudService
    private final transient Map<String, Object> map = new HashMap<>()
    private final transient Map<String, Object> paramsroot = new HashMap<>()
    private final Map<CrudEvents, List<OnEvent>> onEvent = new HashMap<>()
    private final List<OnQuery> postEvent = new ArrayList<>()
    private @Getter
    @Setter
    String beginString = ""
    private List values
    private @Getter
    @Setter
    ParamMode paramMode = ParamMode.AND
    private @Getter
    @Setter
    Boolean reloadable
    private int offSet = 0
    private int limit = 16

    CrudController(Class<?> klass, List<?> values) {
        super()
        crudService = ApplicationContextUtils.getBean(CrudService.class)
        this.klass = klass
        this.values = values
    }

    private Class<?> getTypeClass() {
        return klass
    }

    void addValue(Object value) {
        if (!values.contains(value)) {
            ((ArrayList) values).add(value)
            this.onEvent.get(CrudEvents.ON_ADD).forEach { it -> it.doSomething() }
        } else
            ZKUtil.showMessage(ReflectionZKUtil.getLabel("onlist"), MessageType.WARNING)
    }

    void setValue(List value) {
        if (!CollectionUtils.isEqualCollection(values, value)) {
            values.clear()
            values.addAll(value)
            postEvent.forEach { it -> it.doSomething() }
        }

    }

    void setValue(ArrayList value) {
        setValue((List) value)
    }

    List<String> getValueIds() {
        return (List<String>) values.stream().map { it -> getIdValue(it) }.map { it -> it.toString() }.collect(Collectors.toList())
    }


    void setValueIds(List<String> value) {
        value = value.stream().map(Long.&parseLong as Function).collect(Collectors.toList())
        CrudService crudService = getBean(CrudService.class)
        PathBuilder<?> pathBuilder = crudService.getPathBuilder(getTypeClass())
        String idPropertyName = crudService.getIdPropertyName(klass)
        PathBuilder<Object> t = pathBuilder.get(idPropertyName)
        List fetch = crudService.query().from(pathBuilder).where(t.in((List) value)).fetch()
        setValue((ArrayList<?>) fetch)
    }

    Object getRoot(Object key) {
        return paramsroot.get(key)
    }

    Object putRoot(String key, Object value) {
        Object put = paramsroot.put(key, value)
        doQuery()
        return put
    }

    Object get(Object key) {
        return map.get(key)
    }

    Object put(String key, Object value) {
        Object put = map.put(key, value)
        doQuery()
        return put
    }

    void clearParams() {
        map.clear()
    }

    void doQuery() {
        PathBuilder pathBuilder = crudService.getPathBuilder(klass)
        Predicate where = getPredicate(pathBuilder)
        if (klass.isAnnotationPresent(CrudOrderBy.class)) {
            String value = klass.getAnnotation(CrudOrderBy.class).value()
            if (value != null) {
                Field field = ReflectionJavaUtil.getField(klass, value)
                OrderSpecifier asc = pathBuilder.getComparable(value, field.getType()).asc()
                setValue((ArrayList) (
                        where == null ?
                                crudService.query().from(pathBuilder).orderBy(asc).offset(offSet).limit(limit).fetch()
                                :
                                crudService.query().from(pathBuilder).where(where).offset(offSet).limit(limit).orderBy(asc).fetch())
                )

            }
        } else {
            List<?> fetch = limit != 0 ? jpaQuery.offset(offSet).limit(limit).fetch() : jpaQuery.fetch()
            setValue(map.isEmpty() ? (ArrayList) crudService.getAllOrder(klass) : (ArrayList) fetch)
        }

    }

    private Predicate getPredicate(PathBuilder pathBuilder) {
        Predicate where = null
        map.putAll(paramsroot)
        where = getPredicate(map, pathBuilder, where)
        return beginString != null && !beginString.isEmpty() ? crudService.getLikePredicate(beginString, ReflectionJavaUtil.getStringFields(klass), pathBuilder, where) : where
    }

    private Predicate getPredicate(Map<String, Object> map, PathBuilder pathBuilder, Predicate where) {
        for (Map.Entry<String, Object> v : map.entrySet()) {
            Object value = v.getValue()
            switch (value) {
                case { value instanceof String && (value as String).startsWith(AlphabetFilter.STARTWITH) }:
                    StringPath stringPath = pathBuilder.getString(v.getKey());
                    def substring = (value as String).substring(AlphabetFilter.STARTWITH.length())
                    def lowerCase = substring.toLowerCase()
                    where = where != null ? paramMode == ParamMode.AND ? stringPath.startsWith(substring).or(stringPath.startsWith(lowerCase)).and(where) : stringPath.startsWith((value as String).substring(AlphabetFilter.STARTWITH.length() - 1)).or(stringPath.startsWith(lowerCase)).or(where) : stringPath.startsWith(substring).or(stringPath.startsWith(lowerCase))
                    break
                case { value instanceof DateRange }:
                    DatePath<Date> date = pathBuilder.getDate(v.getKey(), Date.class)
                    where = where != null ? paramMode == ParamMode.AND ? date.between(((DateRange) value).getInicio(), ((DateRange) value).getFin()).and(where) : date.between(((DateRange) value).getInicio(), ((DateRange) value).getFin()).or(where) : date.between(((DateRange) value).getInicio(), ((DateRange) value).getFin())
                    break

                case { value instanceof Collection }:
                    CollectionPath collection = pathBuilder.getCollection(v.getKey(), value.getClass())
                    if (collection != null) {
                        if (!(value instanceof Collection))
                            where = where != null ? paramMode == ParamMode.AND ? collection.contains(value).and(where) : collection.contains(value).or(where) : collection.contains(value)
                        else
                            for (Object val : (Collection) value) {
                                if (val == null) {
                                    where = where != null ? paramMode == ParamMode.AND ? collection.isEmpty().and(where) : collection.isEmpty().or(where) : collection.isEmpty()
                                    break
                                } else
                                    where = where != null ? paramMode == ParamMode.AND ? collection.contains(val).and(where) : collection.contains(val).or(where) : collection.contains(val)
                            }
                    }
                    break
                default:
                    where = where != null ? paramMode == ParamMode.AND ? pathBuilder.get(v.getKey()).eq(value).and(where) : pathBuilder.get(v.getKey()).eq(value).or(where) : pathBuilder.get(v.getKey()).eq(value)
                    break

            }
        }


        return where
    }


    void addEventOnEvent(CrudEvents e, OnEvent o) {
        List<OnEvent> onEvents = this.onEvent.get(e)
        if (onEvents == null) {
            onEvents = new ArrayList<>()
        }
        onEvents.add(o)
        this.onEvent.put(e, onEvents)
    }


    void addEventPostQuery(OnQuery o) {
        this.postEvent.add(o)

    }

    void setContainsString(String beginString) {
        this.beginString = beginString
        doQuery()
    }

    void doQueryStringBegin(String field, String value) {
        if (value.isEmpty() || value.compareToIgnoreCase(AlphabetFilter.STARTWITH + "ALL") == 0) {
            map.remove(field)
            doQuery()
        } else {
            map.put(field, value)
            doQuery()
        }
    }

    void setPage(int offSet, int limit) {
        if (limit == 0)
            throw new IllegalArgumentException("Limit can't be 0")
        if (this.offSet != offSet || this.limit != limit) {
            this.offSet = offSet
            this.limit = limit
            doQuery()
        }
    }

    long getCount() {
        map.putAll(paramsroot)
        PathBuilder<?> pathBuilder = crudService.getPathBuilder(klass)
        return crudService.query().from(pathBuilder).where(getPredicate(map, pathBuilder, null)).fetchCount()


    }

}
