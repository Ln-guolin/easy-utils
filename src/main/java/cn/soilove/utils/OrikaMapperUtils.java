package cn.soilove.utils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory.Builder;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeFactory;

import java.util.List;
import java.util.Map;

/**
 * 实体映射工具类
 *
 * @author: Chen GuoLin
 * @create: 2020-01-30 12:41
 **/
public class OrikaMapperUtils {

    private static MapperFactory factory;
    private static MapperFacade mapper;

    private OrikaMapperUtils() {
    }

    public static <S, D> D map(S source, Class<D> destinationClass) {
        return mapper.map(source, destinationClass);
    }

    public static <S, D> D map(S source, Type<S> sourceType, Type<D> destinationType) {
        return mapper.map(source, sourceType, destinationType);
    }

    public static <S, D> D map(S source, Class<S> sourceClass, Class<D> destinationClass, Map<String,String> fieldMap) {
        ClassMapBuilder classMapBuilder = factory.classMap(sourceClass, destinationClass);
        fieldMap.forEach(classMapBuilder::field);
        classMapBuilder.byDefault().register();
        return factory.getMapperFacade().map(source, destinationClass);
    }

    public static <S, D> List<D> mapList(Iterable<S> sourceList, Class<S> sourceClass, Class<D> destinationClass) {
        return mapper.mapAsList(sourceList, TypeFactory.valueOf(sourceClass), TypeFactory.valueOf(destinationClass));
    }

    public static <S, D> List<D> mapList(Iterable<S> sourceList, Type<S> sourceType, Type<D> destinationType) {
        return mapper.mapAsList(sourceList, sourceType, destinationType);
    }

    public static <S, D> List<D> mapList(Iterable<S> sourceList, Class<S> sourceClass, Class<D> destinationClass, Map<String,String> fieldMap) {
        ClassMapBuilder classMapBuilder = factory.classMap(sourceClass, destinationClass);
        fieldMap.forEach(classMapBuilder::field);
        classMapBuilder.byDefault().register();
        return factory.getMapperFacade().mapAsList(sourceList, TypeFactory.valueOf(sourceClass), TypeFactory.valueOf(destinationClass));
    }

    public static <S, D> D[] mapArray(D[] destination, S[] source, Class<D> destinationClass) {
        return mapper.mapAsArray(destination, source, destinationClass);
    }

    public static <S, D> D[] mapArray(D[] destination, S[] source, Type<S> sourceType, Type<D> destinationType) {
        return mapper.mapAsArray(destination, source, sourceType, destinationType);
    }

    public static <E> Type<E> getType(Class<E> rawType) {
        return TypeFactory.valueOf(rawType);
    }

    static {
        factory = (new Builder()).build();
        mapper = factory.getMapperFacade();
    }
}
