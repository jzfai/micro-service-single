top.hugo.common.utils.MessageUtils

```java
package top.hugo.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import top.hugo.common.spring.SpringUtils;

/**
 * 获取i18n资源文件
 *
 * @author kuanghua
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageUtils {

    private static final MessageSource MESSAGE_SOURCE = SpringUtils.getBean(MessageSource.class);

    /**
     * 根据消息键和参数 获取消息 委托给spring messageSource
     *
     * @param code 消息键
     * @param args 参数
     * @return 获取国际化翻译值
     */
    public static String message(String code, Object... args) {
        return MESSAGE_SOURCE.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}

```

top.hugo.common.utils.StreamUtils

```java
package top.hugo.common.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * stream 流工具类
 *
 * @author kuanghua
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StreamUtils {

    /**
     * 将collection过滤
     *
     * @param collection 需要转化的集合
     * @param function   过滤方法
     * @return 过滤后的list
     */
    public static <E> List<E> filter(Collection<E> collection, Predicate<E> function) {
        if (CollUtil.isEmpty(collection)) {
            return CollUtil.newArrayList();
        }
        return collection.stream().filter(function).collect(Collectors.toList());
    }

    /**
     * 将collection拼接
     *
     * @param collection 需要转化的集合
     * @param function   拼接方法
     * @return 拼接后的list
     */
    public static <E> String join(Collection<E> collection, Function<E, String> function) {
        return join(collection, function, ",");
    }

    /**
     * 将collection拼接
     *
     * @param collection 需要转化的集合
     * @param function   拼接方法
     * @param delimiter  拼接符
     * @return 拼接后的list
     */
    public static <E> String join(Collection<E> collection, Function<E, String> function, CharSequence delimiter) {
        if (CollUtil.isEmpty(collection)) {
            return StringUtils.EMPTY;
        }
        return collection.stream().map(function).filter(Objects::nonNull).collect(Collectors.joining(delimiter));
    }

    /**
     * 将collection排序
     *
     * @param collection 需要转化的集合
     * @param comparing  排序方法
     * @return 排序后的list
     */
    public static <E> List<E> sorted(Collection<E> collection, Comparator<E> comparing) {
        if (CollUtil.isEmpty(collection)) {
            return CollUtil.newArrayList();
        }
        return collection.stream().sorted(comparing).collect(Collectors.toList());
    }

    /**
     * 将collection转化为类型不变的map<br>
     * <B>{@code Collection<V>  ---->  Map<K,V>}</B>
     *
     * @param collection 需要转化的集合
     * @param key        V类型转化为K类型的lambda方法
     * @param <V>        collection中的泛型
     * @param <K>        map中的key类型
     * @return 转化后的map
     */
    public static <V, K> Map<K, V> toIdentityMap(Collection<V> collection, Function<V, K> key) {
        if (CollUtil.isEmpty(collection)) {
            return MapUtil.newHashMap();
        }
        return collection.stream().collect(Collectors.toMap(key, Function.identity(), (l, r) -> l));
    }

    /**
     * 将Collection转化为map(value类型与collection的泛型不同)<br>
     * <B>{@code Collection<E> -----> Map<K,V>  }</B>
     *
     * @param collection 需要转化的集合
     * @param key        E类型转化为K类型的lambda方法
     * @param value      E类型转化为V类型的lambda方法
     * @param <E>        collection中的泛型
     * @param <K>        map中的key类型
     * @param <V>        map中的value类型
     * @return 转化后的map
     */
    public static <E, K, V> Map<K, V> toMap(Collection<E> collection, Function<E, K> key, Function<E, V> value) {
        if (CollUtil.isEmpty(collection)) {
            return MapUtil.newHashMap();
        }
        return collection.stream().collect(Collectors.toMap(key, value, (l, r) -> l));
    }

    /**
     * 将collection按照规则(比如有相同的班级id)分类成map<br>
     * <B>{@code Collection<E> -------> Map<K,List<E>> } </B>
     *
     * @param collection 需要分类的集合
     * @param key        分类的规则
     * @param <E>        collection中的泛型
     * @param <K>        map中的key类型
     * @return 分类后的map
     */
    public static <E, K> Map<K, List<E>> groupByKey(Collection<E> collection, Function<E, K> key) {
        if (CollUtil.isEmpty(collection)) {
            return MapUtil.newHashMap();
        }
        return collection
                .stream()
                .collect(Collectors.groupingBy(key, LinkedHashMap::new, Collectors.toList()));
    }

    /**
     * 将collection按照两个规则(比如有相同的年级id,班级id)分类成双层map<br>
     * <B>{@code Collection<E>  --->  Map<T,Map<U,List<E>>> } </B>
     *
     * @param collection 需要分类的集合
     * @param key1       第一个分类的规则
     * @param key2       第二个分类的规则
     * @param <E>        集合元素类型
     * @param <K>        第一个map中的key类型
     * @param <U>        第二个map中的key类型
     * @return 分类后的map
     */
    public static <E, K, U> Map<K, Map<U, List<E>>> groupBy2Key(Collection<E> collection, Function<E, K> key1, Function<E, U> key2) {
        if (CollUtil.isEmpty(collection)) {
            return MapUtil.newHashMap();
        }
        return collection
                .stream()
                .collect(Collectors.groupingBy(key1, LinkedHashMap::new, Collectors.groupingBy(key2, LinkedHashMap::new, Collectors.toList())));
    }

    /**
     * 将collection按照两个规则(比如有相同的年级id,班级id)分类成双层map<br>
     * <B>{@code Collection<E>  --->  Map<T,Map<U,E>> } </B>
     *
     * @param collection 需要分类的集合
     * @param key1       第一个分类的规则
     * @param key2       第二个分类的规则
     * @param <T>        第一个map中的key类型
     * @param <U>        第二个map中的key类型
     * @param <E>        collection中的泛型
     * @return 分类后的map
     */
    public static <E, T, U> Map<T, Map<U, E>> group2Map(Collection<E> collection, Function<E, T> key1, Function<E, U> key2) {
        if (CollUtil.isEmpty(collection) || key1 == null || key2 == null) {
            return MapUtil.newHashMap();
        }
        return collection
                .stream()
                .collect(Collectors.groupingBy(key1, LinkedHashMap::new, Collectors.toMap(key2, Function.identity(), (l, r) -> l)));
    }

    /**
     * 将collection转化为List集合，但是两者的泛型不同<br>
     * <B>{@code Collection<E>  ------>  List<T> } </B>
     *
     * @param collection 需要转化的集合
     * @param function   collection中的泛型转化为list泛型的lambda表达式
     * @param <E>        collection中的泛型
     * @param <T>        List中的泛型
     * @return 转化后的list
     */
    public static <E, T> List<T> toList(Collection<E> collection, Function<E, T> function) {
        if (CollUtil.isEmpty(collection)) {
            return CollUtil.newArrayList();
        }
        return collection
                .stream()
                .map(function)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 将collection转化为Set集合，但是两者的泛型不同<br>
     * <B>{@code Collection<E>  ------>  Set<T> } </B>
     *
     * @param collection 需要转化的集合
     * @param function   collection中的泛型转化为set泛型的lambda表达式
     * @param <E>        collection中的泛型
     * @param <T>        Set中的泛型
     * @return 转化后的Set
     */
    public static <E, T> Set<T> toSet(Collection<E> collection, Function<E, T> function) {
        if (CollUtil.isEmpty(collection) || function == null) {
            return CollUtil.newHashSet();
        }
        return collection
                .stream()
                .map(function)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }


    /**
     * 合并两个相同key类型的map
     *
     * @param map1  第一个需要合并的 map
     * @param map2  第二个需要合并的 map
     * @param merge 合并的lambda，将key  value1 value2合并成最终的类型,注意value可能为空的情况
     * @param <K>   map中的key类型
     * @param <X>   第一个 map的value类型
     * @param <Y>   第二个 map的value类型
     * @param <V>   最终map的value类型
     * @return 合并后的map
     */
    public static <K, X, Y, V> Map<K, V> merge(Map<K, X> map1, Map<K, Y> map2, BiFunction<X, Y, V> merge) {
        if (MapUtil.isEmpty(map1) && MapUtil.isEmpty(map2)) {
            return MapUtil.newHashMap();
        } else if (MapUtil.isEmpty(map1)) {
            map1 = MapUtil.newHashMap();
        } else if (MapUtil.isEmpty(map2)) {
            map2 = MapUtil.newHashMap();
        }
        Set<K> key = new HashSet<>();
        key.addAll(map1.keySet());
        key.addAll(map2.keySet());
        Map<K, V> map = new HashMap<>();
        for (K t : key) {
            X x = map1.get(t);
            Y y = map2.get(t);
            V z = merge.apply(x, y);
            if (z != null) {
                map.put(t, z);
            }
        }
        return map;
    }
}
```

top.hugo.common.utils.BeanCopyUtils

```java
package top.hugo.common.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.SimpleCache;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.cglib.core.Converter;

import java.util.List;
import java.util.Map;

/**
 * bean深拷贝工具(基于 cglib 性能优异)
 * <p>
 * 重点 cglib 不支持 拷贝到链式对象
 * 例如: 源对象 拷贝到 目标(链式对象)
 * 请区分好`浅拷贝`和`深拷贝`再做使用
 *
 * @author kuanghua
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanCopyUtils {

    /**
     * 单对象基于class创建拷贝
     *
     * @param source 数据来源实体
     * @param desc   描述对象 转换后的对象
     * @return desc
     */
    public static <T, V> V copy(T source, Class<V> desc) {
        if (ObjectUtil.isNull(source)) {
            return null;
        }
        if (ObjectUtil.isNull(desc)) {
            return null;
        }
        final V target = ReflectUtil.newInstanceIfPossible(desc);
        return copy(source, target);
    }

    /**
     * 单对象基于对象创建拷贝
     *
     * @param source 数据来源实体
     * @param desc   转换后的对象
     * @return desc
     */
    public static <T, V> V copy(T source, V desc) {
        if (ObjectUtil.isNull(source)) {
            return null;
        }
        if (ObjectUtil.isNull(desc)) {
            return null;
        }
        BeanCopier beanCopier = BeanCopierCache.INSTANCE.get(source.getClass(), desc.getClass(), null);
        beanCopier.copy(source, desc, null);
        return desc;
    }

    /**
     * 列表对象基于class创建拷贝
     *
     * @param sourceList 数据来源实体列表
     * @param desc       描述对象 转换后的对象
     * @return desc
     */
    public static <T, V> List<V> copyList(List<T> sourceList, Class<V> desc) {
        if (ObjectUtil.isNull(sourceList)) {
            return null;
        }
        if (CollUtil.isEmpty(sourceList)) {
            return CollUtil.newArrayList();
        }
        return StreamUtils.toList(sourceList, source -> {
            V target = ReflectUtil.newInstanceIfPossible(desc);
            copy(source, target);
            return target;
        });
    }

    /**
     * bean拷贝到map
     *
     * @param bean 数据来源实体
     * @return map对象
     */
    @SuppressWarnings("unchecked")
    public static <T> Map<String, Object> copyToMap(T bean) {
        if (ObjectUtil.isNull(bean)) {
            return null;
        }
        return BeanMap.create(bean);
    }

    /**
     * map拷贝到bean
     *
     * @param map       数据来源
     * @param beanClass bean类
     * @return bean对象
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> beanClass) {
        if (MapUtil.isEmpty(map)) {
            return null;
        }
        if (ObjectUtil.isNull(beanClass)) {
            return null;
        }
        T bean = ReflectUtil.newInstanceIfPossible(beanClass);
        return mapToBean(map, bean);
    }

    /**
     * map拷贝到bean
     *
     * @param map  数据来源
     * @param bean bean对象
     * @return bean对象
     */
    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        if (MapUtil.isEmpty(map)) {
            return null;
        }
        if (ObjectUtil.isNull(bean)) {
            return null;
        }
        BeanMap.create(bean).putAll(map);
        return bean;
    }

    /**
     * BeanCopier属性缓存<br>
     * 缓存用于防止多次反射造成的性能问题
     *
     * @author Looly
     * @since 5.4.1
     */
    public enum BeanCopierCache {
        /**
         * BeanCopier属性缓存单例
         */
        INSTANCE;

        private final SimpleCache<String, BeanCopier> cache = new SimpleCache<>();

        /**
         * 获得类与转换器生成的key在{@link BeanCopier}的Map中对应的元素
         *
         * @param srcClass    源Bean的类
         * @param targetClass 目标Bean的类
         * @param converter   转换器
         * @return Map中对应的BeanCopier
         */
        public BeanCopier get(Class<?> srcClass, Class<?> targetClass, Converter converter) {
            final String key = genKey(srcClass, targetClass, converter);
            return cache.get(key, () -> BeanCopier.create(srcClass, targetClass, converter != null));
        }

        /**
         * 获得类与转换器生成的key
         *
         * @param srcClass    源Bean的类
         * @param targetClass 目标Bean的类
         * @param converter   转换器
         * @return 属性名和Map映射的key
         */
        private String genKey(Class<?> srcClass, Class<?> targetClass, Converter converter) {
            final StringBuilder key = StrUtil.builder()
                    .append(srcClass.getName()).append('#').append(targetClass.getName());
            if (null != converter) {
                key.append('#').append(converter.getClass().getName());
            }
            return key.toString();
        }
    }
}
```

